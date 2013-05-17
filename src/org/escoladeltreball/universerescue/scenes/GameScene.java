package org.escoladeltreball.universerescue.scenes;

import java.util.Iterator;
import java.util.LinkedList;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl.IAnalogOnScreenControlListener;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.extension.debugdraw.DebugRenderer;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.util.GLState;
import org.escoladeltreball.universerescue.GameActivity;
import org.escoladeltreball.universerescue.game.BulletPool;
import org.escoladeltreball.universerescue.game.CoolDown;
import org.escoladeltreball.universerescue.game.Enemy;
import org.escoladeltreball.universerescue.game.FlyEnemy;
import org.escoladeltreball.universerescue.game.Item;
import org.escoladeltreball.universerescue.game.Platform;
import org.escoladeltreball.universerescue.game.Player;
import org.escoladeltreball.universerescue.game.Wall;
import org.escoladeltreball.universerescue.managers.SceneManager.SceneType;

import android.hardware.SensorManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class GameScene extends BaseScene implements IOnSceneTouchListener,
		OnClickListener {

	// VARIABLES //

	/** Counter for player's score */
	private int score = 0;
	/** Counter for enemies killed */
	private int enemiesKilled = 0;
	/** Counter for goal enemies */
	private final int enemiesGoal = 30;
	/** Our game HUD */
	private HUD gameHUD;
	/** Displays the enemies remaining */
	private Text enemiesLeftText;
	/** Game's physics */
	private PhysicsWorld physics;
	/** The player */
	private Player player;
	/** Platforms */
	private Platform platform;
	private Platform platform2;
	/** Add Item */
	private boolean addItem;
	/** Item */
	private Item item;
	/** Check if scene is touched */
	private boolean isTouched = false;
	/** A bulletPool for manage bullet sprites */
	private BulletPool PLAYER_BULLET_POOL;
	private BulletPool FLYENEMY_BULLET_POOL;
	/** LinkedList for available bullet sprites */
	public LinkedList bulletList;
	/** FlyEnemy for test */
	private FlyEnemy fly;
	/** Enemy for test */
	private Enemy enemy;
	private Sprite b;

	// Heal parts
	private Rectangle healstate;
	private Sprite heal;

	@Override
	public void createScene() {
		camera.setBounds(0, 0, GameActivity.getWidth() * 2,
				GameActivity.getHeight());
		camera.setBoundsEnabled(true);
		createBackground();
		createHUD();
		createPhysics();
		this.createWalls();
		createPlayer();
		createControls();
		createPlatform();
		createBulletPool();
		createFlyEnemy();
		createEnemy();
		DebugRenderer debug = new DebugRenderer(physics, vbom);
		this.attachChild(debug);
		setOnSceneTouchListener(this);
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_GAME;
	}

	@Override
	public void disposeScene() {
		camera.setHUD(null);
		camera.setChaseEntity(null);
		player.detachSelf();
		platform.detachSelf();
		platform2.detachSelf();
		this.dispose();
		this.detachSelf();
		camera.setCenter(camera.getWidth() / 2, camera.getHeight() / 2);
	}

	/**
	 * Build a background for our game scene
	 */

	public void createBackground() {
		Sprite sprite = new Sprite(0, 0, manager.game_background, vbom) {
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) {
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}
		};
		sprite.setWidth(GameActivity.getWidth() * 2);
		sprite.setOffsetCenter(0, 0);
		this.attachChild(sprite);
	}

	public void createHUD() {
		// Build a new HUD for our game
		this.gameHUD = new HUD();
		// Set the HUD to the camera
		this.camera.setHUD(gameHUD);
		this.camera.getHUD().setVisible(false);
		// Create a new Text field for allow view how many enemies remaining
		this.enemiesLeftText = new Text(manager.camera.getWidth() / 2f,
				(this.camera.getHeight() / 2f) * 1.8f, manager.gameFont,
				String.valueOf(this.enemiesKilled) + "/"
						+ String.valueOf(this.enemiesGoal), manager.vbom);

		heal = new Sprite(this.camera.getXMin() + 150,
				(this.camera.getHeight() / 2f) * 1.8f, manager.life, vbom);
		healstate = new Rectangle(this.camera.getXMin() + 151,
				(this.camera.getHeight() / 2f) * 1.83f, 240, 22, vbom);
		healstate.setColor(255, 255, 255);

		// Put the Text to HUD
		this.camera.getHUD().attachChild(heal);
		this.camera.getHUD().attachChild(healstate);
		this.camera.getHUD().attachChild(this.enemiesLeftText);
		this.camera.getHUD().setVisible(true);
	}

	public void createWalls() {
		new Wall(GameActivity.getWidth(), 50, GameActivity.getWidth() * 2, 1,
				this.vbom, physics);
		new Wall(GameActivity.getWidth(), GameActivity.getHeight(),
				GameActivity.getWidth() * 2, 1, this.vbom, physics);
		new Wall(0, GameActivity.getHeight() / 2f, 1, GameActivity.getHeight(),
				this.vbom, physics);
		new Wall(GameActivity.getWidth() * 2, GameActivity.getHeight() / 2, 1,
				GameActivity.getHeight(), this.vbom, physics);
	}

	public void createPlayer() {
		this.player = new Player(20, 100, manager.playerSprite, this.vbom,
				camera, physics);
		this.attachChild(player);
	}

	public void createPlatform() {
		this.platform = new Platform(400f, 100f, manager.platformSprite,
				this.vbom, camera, physics);

		this.platform2 = new Platform(800f, 230f, manager.platformSprite,
				this.vbom, camera, physics);

		this.attachChild(platform);
		this.attachChild(platform2);
	}

	public void createFlyEnemy() {
		fly = new FlyEnemy(camera.getCenterX(), (camera.getHeight() / 4f) * 3,
				manager.playerSprite, vbom, camera, physics);
		this.attachChild(fly);
	}

	public void createEnemy() {
		enemy = new Enemy(camera.getBoundsXMax(), 100, manager.enemySprite,
				this.vbom, camera, physics);
		this.attachChild(enemy);

	}

	public void createBulletPool() {
		PLAYER_BULLET_POOL = new BulletPool(manager.bulletSprite, this);
		FLYENEMY_BULLET_POOL = new BulletPool(manager.flyEnemyBullet, this);
		bulletList = new LinkedList();
	}

	public void createControls() {
		manager.loadControls();
		AnalogOnScreenControl control = new AnalogOnScreenControl(0, 0, camera,
				manager.controlBaseRegion, manager.controlKnobRegion, 0.1f,
				200, vbom, new IAnalogOnScreenControlListener() {

					@Override
					public void onControlChange(
							BaseOnScreenControl pBaseOnScreenControl,
							float pValueX, float pValueY) {
						if (pValueX > 0) {
							player.setFlippedHorizontal(false);
						} else if (pValueX < 0) {
							player.setFlippedHorizontal(true);
						}
						player.run(pValueX);
						player.setDirection(pValueY);
					}

					@Override
					public void onControlClick(
							AnalogOnScreenControl pAnalogOnScreenControl) {
					}
				});
		Sprite controlBase = control.getControlBase();
		ButtonSprite button = new ButtonSprite(GameActivity.getWidth() - 60,
				60, manager.buttonA, vbom, this);
		button.setAlpha(0.5f);
		gameHUD.attachChild(button);
		gameHUD.registerTouchArea(button);
		// Transparency
		controlBase.setAlpha(0.5f);
		// center
		controlBase.setOffsetCenter(0, 0);
		control.getControlKnob().setScale(1.25f);
		this.setChildScene(control);
	}

	/**
	 * Add score to player's score
	 */

	public void addToScore(int i) {
		this.score += i;
	}

	/**
	 * Add to enemies killed
	 */

	public void addEnemiesKilled(int i) {
		if (this.enemiesKilled + i > this.enemiesGoal) {
			this.enemiesKilled = this.enemiesGoal;
		} else {
			this.enemiesKilled += i;
		}
		this.enemiesLeftText.setText(String.valueOf(this.enemiesKilled) + "/"
				+ String.valueOf(this.enemiesGoal));
	}

	/**
	 * Create our physics for the level
	 */

	public void createPhysics() {
		this.physics = new FixedStepPhysicsWorld(60, new Vector2(0,
				-SensorManager.GRAVITY_EARTH), false);
		registerUpdateHandler(this.physics);
		physics.setContactListener(new ContactListener() {

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
			}

			@Override
			public void endContact(Contact contact) {
			}

			@Override
			public void beginContact(Contact contact) {
				Fixture x1 = contact.getFixtureA();
				Fixture x2 = contact.getFixtureB();
				if (x1.getBody().getUserData().equals("player")
						|| x2.getBody().getUserData().equals("player")) {
					if (x1.getBody().getUserData().equals("item")
							|| x2.getBody().getUserData().equals("item")) {
						healstate.setWidth(healstate.getWidth() + 30);
						healstate.setX(camera.getCameraSceneWidth() - 650);
						item.removeItem();
					}
					if (x1.getBody().getUserData().equals("wall")
							|| x2.getBody().getUserData().equals("wall")) {
						player.setJump(false);
						player.setCurrentTileIndex(3);
					}
					if (x1.getBody().getUserData().equals("platform")
							|| x2.getBody().getUserData().equals("platform")) {
						player.setJump(false);
						player.setCurrentTileIndex(3);
						healstate.setWidth(210);
						healstate.setX(camera.getCameraSceneWidth() - 664);
					}
				}
			}
		});
	}

	/**
	 * Create Item
	 */

	public void createItem() {
		item = new Item(800f, camera.getHeight() - 30, manager.item, this.vbom,
				camera, physics);
		this.attachChild(item);
	}

	/**
	 * Do an action depends the kind of touch
	 */

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
			this.isTouched = true;
		} else if ((pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP)
				&& this.isTouched) {
			player.jump();
			this.isTouched = false;
		}
		return false;
	}

	@Override
	public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX,
			float pTouchAreaLocalY) {
		if (CoolDown.getInstance().timeHasPassed()) {
			Sprite fire = PLAYER_BULLET_POOL.obtainPoolItem();
			player.fire(physics, fire);
		}
	}

	/**
	 * UpdateHandler for check everymoment if the bullet goes out of screen (X
	 * and Y), then recycle it.
	 */

	IUpdateHandler detect = new IUpdateHandler() {
		@Override
		public void reset() {
		}

		@Override
		public void onUpdate(float pSecondsElapsed) {
			// iterating the targets
			Iterator it = bulletList.iterator();
			while (it.hasNext()) {
				Sprite b = (Sprite) it.next();
				if (b.getX() >= b.getWidth()
						|| b.getY() >= b.getHeight() + b.getHeight()
						|| b.getY() <= -b.getHeight()) {
					if (b.getParent().equals(PLAYER_BULLET_POOL)) {
						PLAYER_BULLET_POOL.recyclePoolItem(b);
						it.remove();
						continue;
					} else {
						FLYENEMY_BULLET_POOL.recyclePoolItem(b);
						it.remove();
						continue;
					}
				}
			}
		}
	};

	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		if (player.getX() >= 600 && !addItem) { // Cuando el jugador pasa esa
			// recta y no se ha creado aun el item
			// // se añade el item
			addItem = true;
			this.createItem();
		}

		if (fly.canAttack()) {
			Sprite fireEnemy = FLYENEMY_BULLET_POOL.obtainPoolItem();
			fly.attackPlayer(player, fireEnemy);
			// System.out.println("FIREEEEEEEEEEEEEEEEEEEEEEEEEEE");

		} else {
			if (CoolDown.getInstance().timeHasPassed()) {
				fly.move();
			}
		}
		Iterator it = bulletList.iterator();
		while (it.hasNext()) {
			b = (Sprite) it.next();
			if (b.collidesWith(enemy)) {
				activity.runOnUpdateThread(new Runnable() {

					@Override
					public void run() {
						Body bodyBullet = physics.getPhysicsConnectorManager()
								.findBodyByShape(b);
						enemy.takeDamage(20);
						System.out.println(enemy.getHP());
						bodyBullet.setActive(false);
						//physics.destroyBody(bodyBullet);
						b.detachSelf();
						System.gc();
					}
				});
			}
		}

		platform.moveX();
		enemy.runEnemy();
		super.onManagedUpdate(pSecondsElapsed);
	}

}