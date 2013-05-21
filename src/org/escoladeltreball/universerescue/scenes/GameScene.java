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
import org.escoladeltreball.universerescue.game.FlyEnemy;
import org.escoladeltreball.universerescue.game.Item;
import org.escoladeltreball.universerescue.game.Platform;
import org.escoladeltreball.universerescue.game.Player;
import org.escoladeltreball.universerescue.game.TeraEnemy;
import org.escoladeltreball.universerescue.game.Wall;
import org.escoladeltreball.universerescue.managers.SceneManager.SceneType;

import android.hardware.SensorManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public abstract class GameScene extends BaseScene implements IOnSceneTouchListener,
		OnClickListener {

	// VARIABLES //

	/** Counter for player's score */
	private int score;
	/** Counter for enemies killed */
	protected int enemiesKilled;
	/** Counter for goal enemies */
	private static final int ENEMIESGOAL = 30;
	/** Our game HUD */
	private HUD gameHUD;
	/** Displays the enemies remaining */
	private Text enemiesLeftText;
	/** The player */
	protected Player player;
	/** Add Item */
	protected boolean addItem;
	/** Item */
	protected Item item;
	/** Check if scene is touched */
	private boolean isTouched;
	/** A bulletPool for manage bullet sprites */
	protected BulletPool PLAYER_BULLET_POOL;
	/** LinkedList for available bullet sprites */
	public LinkedList bulletList;

	// Heal parts
	protected Rectangle healstate;
	private Sprite heal;

	// CoolDowns
	private CoolDown coolDownPlayer;
	

	public GameScene() {
		super();
		this.coolDownPlayer = new CoolDown();
		camera.setBounds(0, 0, GameActivity.getWidth() * 2,
				GameActivity.getHeight());
		camera.setBoundsEnabled(true);
	}

	@Override
	public void createScene() {
		this.createBackground();
		this.createHUD();
		this.createPhysics();
		this.createWalls();
		this.createPlayer();
		this.createControls();
		this.createPlatform();
		this.createBulletPool();
		this.createFlyEnemy();
		this.createEnemy();
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
		this.dispose();
		this.detachSelf();
		camera.setCenter(camera.getWidth() / 2, camera.getHeight() / 2);
	}

	/**
	 * Create  and add the Background of scene 
	 */
	public abstract void createBackground();

	
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
						+ String.valueOf(ENEMIESGOAL), manager.vbom);

		heal = new Sprite(this.camera.getXMin() + 150,
				(this.camera.getHeight() / 2f) * 1.8f, manager.life, vbom);
		healstate = new Rectangle(30, (this.camera.getHeight() / 2f) * 1.83f,
				240, 22, vbom);
		healstate.setAnchorCenterX(0f);
		healstate.setColor(255, 255, 255);

		// Put the Text to HUD
		this.camera.getHUD().attachChild(heal);
		this.camera.getHUD().attachChild(healstate);
		this.camera.getHUD().attachChild(this.enemiesLeftText);
		this.camera.getHUD().setVisible(true);
	}

	/**
	 * Create the walls that limit the scene
	 */
	public abstract void createWalls();

	/**
	 * Create and add to scene the main player 
	 */
	public abstract void createPlayer();
	
	public abstract void createBulletPool();

	/**
	 * Create and add to scene platforms
	 */
	public abstract void createPlatform();

	public abstract void createFlyEnemy();

	public abstract void createEnemy();

	

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
	 * Plus i in the counter of enemies killed
	 * @param i the number of enemies killed
	 */
	public void addEnemiesKilled(int i) {
		if (this.enemiesKilled + i > this.ENEMIESGOAL) {
			this.enemiesKilled = this.ENEMIESGOAL;
		} else {
			this.enemiesKilled += i;
		}
		this.enemiesLeftText.setText(String.valueOf(this.enemiesKilled) + "/"
				+ String.valueOf(this.ENEMIESGOAL));
	}

	/**
	 * Create our physics for the level
	 */

	public abstract void createPhysics();

	/**
	 * Create Item
	 */

	public abstract void createItem();

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
		if (coolDownPlayer.timeHasPassed()) {
			Sprite fire = PLAYER_BULLET_POOL.obtainPoolItem();
			player.fire(fire);
		}
	}

	/**
	 * UpdateHandler for check everymoment if the bullet goes out of screen (X
	 * and Y), then recycle it.
	 */

//	IUpdateHandler detect = new IUpdateHandler() {
//		@Override
//		public void reset() {
//		}
//
//		@Override
//		public void onUpdate(float pSecondsElapsed) {
//			// iterating the targets
//			Iterator it = bulletList.iterator();
//			while (it.hasNext()) {
//				Sprite b = (Sprite) it.next();
//				if (b.getX() >= b.getWidth()
//						|| b.getY() >= b.getHeight() + b.getHeight()
//						|| b.getY() <= -b.getHeight()) {
//					if (b.getParent().equals(PLAYER_BULLET_POOL)) {
//						PLAYER_BULLET_POOL.recyclePoolItem(b);
//						it.remove();
//						continue;
//					} else {
//						FLYENEMY_BULLET_POOL.recyclePoolItem(b);
//						it.remove();
//						continue;
//					}
//				}
//			}
//		}
//	};



	public boolean areBodiesContacted(String pBody1, String pBody2,
			Contact pContact) {
		if (pContact.getFixtureA().getBody().getUserData().equals(pBody1)
				|| pContact.getFixtureB().getBody().getUserData()
						.equals(pBody1))
			if (pContact.getFixtureA().getBody().getUserData().equals(pBody2)
					|| pContact.getFixtureB().getBody().getUserData()
							.equals(pBody2))
				return true;
		return false;
	}

}
