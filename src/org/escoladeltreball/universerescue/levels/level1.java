package org.escoladeltreball.universerescue.levels;

import java.util.LinkedList;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.debugdraw.DebugRenderer;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITextureRegion;
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
import org.escoladeltreball.universerescue.managers.SceneManager;
import org.escoladeltreball.universerescue.scenes.GameScene;

import android.hardware.SensorManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class level1 extends GameScene {

	private PhysicsWorld physics;
	private Platform platform, platform2, platform3;
	private TeraEnemy teraEnemy;
	private BulletPool FLYENEMY_BULLET_POOL;
	private FlyEnemy fly;
	private CoolDown coolDownEnemy;
	private LinkedList flyEnemyBulletList;

	public level1() {
		super();
		this.coolDownEnemy = new CoolDown();
	}

	@Override
	public void createScene() {
		super.createScene();
		DebugRenderer debug = new DebugRenderer(physics, vbom);
		this.attachChild(debug);
		this.createPlatform();
		this.createPlayer();
		this.createEnemy();
		this.createFlyEnemy();
	}

	@Override
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

	/**
	 * Create and add to scene platforms
	 */
	public void createPlatform() {
		this.platform = new Platform(800f, 120f, manager.platformSprite,
				this.vbom, camera, physics);

		this.platform2 = new Platform(1500, 210f, manager.platformSprite,
				this.vbom, camera, physics);
		this.platform3 = new Platform(10, 210f, manager.platformSprite,
				this.vbom, camera, physics);
		this.attachChild(platform);
		this.attachChild(platform2);
		this.attachChild(platform3);
	}

	public void createBulletPool() {
		playerBulletList = new LinkedList();
		flyEnemyBulletList = new LinkedList();
		PLAYER_BULLET_POOL = new BulletPool(manager.bulletSprite,playerBulletList, this);
		FLYENEMY_BULLET_POOL = new BulletPool(manager.flyEnemyBullet,flyEnemyBulletList, this);
		
	}

	public void createPhysics() {
		this.physics = new FixedStepPhysicsWorld(60, new Vector2(0,
				-SensorManager.GRAVITY_EARTH), false);
		registerUpdateHandler(this.physics);
		physics.setContactListener(new ContactListener() {

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
				if (areBodiesContacted("player", "platform", contact)) {
					if (player.getY() - player.getHeight() > platform.getY()
							+ platform.getHeight()) {
						contact.setEnabled(true);
					} else {
						contact.setEnabled(false);
					}
				} else if (areBodiesContacted("teraEnemy", "platform", contact)) {
					contact.setEnabled(false);
				}
				if (areBodiesContacted("player", "movePlatform", contact)) {
					if (player.getY() - player.getHeight() > platform2.getY()
							+ platform.getHeight()
							|| player.getY() - player.getHeight() > platform3
									.getY() + platform.getHeight()) {
						contact.setEnabled(true);
					} else {
						contact.setEnabled(false);
					}
				}
			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
			}

			@Override
			public void endContact(Contact contact) {
				if (areBodiesContacted("player", "platform", contact)) {
					if (player.getY() - player.getHeight() > platform.getY()) {
						player.setJump(true);
						player.setCurrentTileIndex(8);
					}
				}
				if (areBodiesContacted("player", "movePlatform", contact)) {
					if (player.getY() - player.getHeight() > platform2.getY()
							+ platform.getHeight()
							|| player.getY() - player.getHeight() > platform3
									.getY() + platform.getHeight()) {
						player.setJump(true);
						player.setCurrentTileIndex(8);
					}
				}
			}

			@Override
			public void beginContact(Contact contact) {
				if (areBodiesContacted("bullet", "teraEnemy", contact)) {
					teraEnemy.eliminateEnemy();
					addEnemiesKilled(1);
				}
				if (areBodiesContacted("player", "teraEnemy", contact)) {
					if (!teraEnemy.isFlippedHorizontal()) {
						teraEnemy.animate(new long[] { 50, 50, 50, 50 }, 4, 7,
								false, teraEnemy);
					}
					if (player.getHp() > 0) {
						player.setHp(player.getHp() - teraEnemy.getAt());
					}
					player.setAttack(true);
					player.attacked();
					player.animate(new long[] { 600, 200 },
							new int[] { 14, 9 }, false, player);
				}
				if (areBodiesContacted("player", "item", contact)) {
					if (player.getHp() > 240) {
						player.setHp(player.getHp() + 20);
					}
					item.removeItem();
				}
				if (areBodiesContacted("player", "wall", contact)) {
					player.setJump(false);
					player.setCurrentTileIndex(9);
				}
				if (areBodiesContacted("player", "platform", contact)) {
					if (player.getY() - player.getHeight() > platform.getY()
							+ platform.getHeight()) {
						player.setJump(false);
						player.setCurrentTileIndex(9);
					}
				}
				if (areBodiesContacted("player", "movePlatform", contact)) {
					if (player.getY() - player.getHeight() > platform2.getY()
							+ platform.getHeight()
							|| player.getY() - player.getHeight() > platform3
									.getY() + platform.getHeight()) {
						player.setJump(false);
						player.setCurrentTileIndex(9);
					}
				}
			}
		});
	}

	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		if (player.getHp() <= 100 && !addItem) {
			addItem = true;
			this.createItem(800, camera.getHeight() - 30, manager.item);
			GameActivity.writeIntToSharedPreferences(
					GameActivity.SHARED_PREFS_LEVEL_MAX_REACHED, 1);
		}
		if (enemiesKilled == 1 && !addItemArmour) {
			addItemArmour = true;
			this.createItem(800, camera.getHeight() - 30, manager.itemArmour);
		}
		if (player.getHp() <= 0) {
			SceneManager.getInstance().showLoseLayer(false);
		}
		if (fly.canAttack()) {
			Sprite fireEnemy = FLYENEMY_BULLET_POOL.obtainPoolItem();
			fly.attack(player, fireEnemy);
		} else {
			//FlyEnemy move after 4 seconds, so we create a small delay between attacks
			if (coolDownEnemy.timeHasPassed(4000)) {
				fly.move();
			}
		}
		for (int i = 0; i < this.playerBulletList.size(); i++) {
			Sprite bullet = (Sprite) this.playerBulletList.get(i);
			if (bullet.collidesWith(fly)) {
				fly.eliminateEnemy();
			}
			
		}
		healstate.setWidth(player.getHp());
		platform3.moveLeftToRight(10, 780);
		platform2.moveRightToLeft(1500, 820);
		teraEnemy.move();
		super.onManagedUpdate(pSecondsElapsed);
	}

	@Override
	public void createEnemy() {
		teraEnemy = new TeraEnemy(1500, 100, manager.enemySprite, this.vbom,
				camera, physics);
		this.attachChild(teraEnemy);
	}

	public void createFlyEnemy() {
		fly = new FlyEnemy(camera.getCenterX(), (camera.getHeight() / 4f) * 3,
				manager.flyEnemySprite.deepCopy(), vbom, camera, physics);
		this.attachChild(fly);
	}

	@Override
	public void createItem(float pX, float pY, ITextureRegion sprite) {
		item = new Item(pX, pY, sprite, this.vbom, camera, physics);
		this.attachChild(item);

	}

	@Override
	public void createPlayer() {
		this.player = new Player(20, 100, manager.playerSprite, this.vbom,
				camera, physics);
		this.attachChild(player);
	}

	@Override
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

}
