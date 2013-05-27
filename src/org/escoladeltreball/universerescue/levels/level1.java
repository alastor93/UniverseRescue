package org.escoladeltreball.universerescue.levels;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.debugdraw.DebugRenderer;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.util.GLState;
import org.escoladeltreball.universerescue.GameActivity;
import org.escoladeltreball.universerescue.game.BulletPool;
import org.escoladeltreball.universerescue.game.CoolDown;
import org.escoladeltreball.universerescue.game.FlyEnemy;
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
	private FlyEnemy fly;
	private CoolDown coolDownEnemy;
	private int countEnemies;
	private int countFlyEnemies;

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
		bulletToBeRecycled = new LinkedList<Sprite>();
		registerUpdateHandler(new IUpdateHandler() {
			@Override
			public void onUpdate(float pSecondsElapsed) {
				Iterator<Sprite> it = bulletToBeRecycled.iterator();
				while (it.hasNext()) {
					Sprite attack = (Sprite) it.next();
					PLAYER_BULLET_POOL.recyclePoolItem(attack);
					it.remove();
				}
			}

			@Override
			public void reset() {
				// TODO Auto-generated method stub

			}
		});
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

		this.platform2 = new Platform(1500f, 210f, manager.platformSprite,
				this.vbom, camera, physics);
		this.platform3 = new Platform(100f, 210f, manager.platformSprite,
				this.vbom, camera, physics);
		this.attachChild(platform);
		this.attachChild(platform2);
		this.attachChild(platform3);
	}

	@Override
	public void createPlayer() {
		this.player = new Player(GameActivity.getWidth() * 0.5f, 100,
				manager.playerSprite, this.vbom, camera, physics, this);
		this.attachChild(player);
	}

	public void createBulletPool() {
		playerBulletList = new LinkedList<Sprite>();
		PLAYER_BULLET_POOL = new BulletPool(manager.bulletSprite,
				playerBulletList, this);

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
				if (areBodiesContacted("teraEnemy", "wall", contact)) {
					contact.setEnabled(true);
				} else if (areBodiesContacted("teraEnemy", "teraEnemy", contact)) {
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
					teraEnemy.setKilled(true);
					player.detachAttack(getBody(physics, fire));
					bulletToBeRecycled.add(fire);
					getBody(physics, teraEnemy).setActive(false);
					teraEnemy.animate(new long[] { 300, 400 },
							new int[] { 8, 9 }, false, teraEnemy);
					addEnemiesKilled(1);
					countEnemies--;
				}
				if (areBodiesContacted("player", "teraEnemy", contact)) {
					if (!teraEnemy.isFlippedHorizontal()
							&& player.isFlippedHorizontal()
							|| teraEnemy.isFlippedHorizontal()
							&& !player.isFlippedHorizontal()) {
						teraEnemy.animate(new long[] { 50, 50, 50, 50 }, 4, 7,
								false, teraEnemy);
					}
					if (player.getHp() > 0 && !player.isSetShield()) {
						player.setHp(player.getHp() - teraEnemy.getAt());
					}else if(player.getHp() - teraEnemy.getAt() < 0 && !player.isSetShield()){
						player.setHp(player.getHp() - player.getHp());
					}
					player.setAttack(true);
					player.attacked();
					if (!player.isSetShield()) {
						player.animate(new long[] { 600, 200 }, new int[] { 14,
								9 }, false, player);
					} else {
						player.animate(new long[] { 800 }, new int[] { 8 },
								false, player);
					}
				}
				if (areBodiesContacted("player", "item", contact)) {
					if (item.getTextureRegion().equals(manager.item)) {
						item.healt(player);
					} else {
						item.setShield(player);
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
		if (countEnemies < 1) {
			createEnemy();
			countEnemies++;
		}
		if (countFlyEnemies < 1) {
			createFlyEnemy();
			countFlyEnemies++;
		}
		if (player.getHp() <= Player.MAXHP * 0.25 && !addItem
				&& enemiesKilled >= 15) {
			addItem = true;
			this.createItem(800, camera.getHeight() - 30, manager.item, physics);
		}
		if (player.getHp() <= Player.MAXHP * 0.5
				&& player.getHp() > Player.MAXHP * 0.25 && enemiesKilled >= 15
				&& !addItemArmour) {
			addItemArmour = true;
			this.createItem(800, camera.getHeight() - 30, manager.itemArmour,
					physics);
		}
		if (player.getHp() <= 0) {
			SceneManager.getInstance().showLoseLayer(false);
		}
		if (enemiesKilled == 30) {
			SceneManager.getInstance().showWinLayer(false);
		}
		if (fly.canAttack()) {
			fly.attack(player);
		} else {
			// FlyEnemy move after 4 seconds, so we create a small delay between
			// attacks
			if (coolDownEnemy.timeHasPassed(4000)) {
				fly.move();
			}
		}
		for (int i = 0; i < this.playerBulletList.size(); i++) {
			Sprite bullet = (Sprite) this.playerBulletList.get(i);
			if (bullet.collidesWith(fly) && !fly.isContact()) {
				fly.setKilled(true);
				player.detachAttack(getBody(physics,fire));
				bulletToBeRecycled.add(fire);
				fly.animate(new long[] { 300, 600 }, 3, 4, false, fly);
				addEnemiesKilled(1);
				countFlyEnemies--;
				fly.setContact(true);
			}
		}
		healstate.setWidth(player.getHp());
		teraEnemy.move();
		platform2.move();
		platform3.move();
		super.onManagedUpdate(pSecondsElapsed);
	}

	@Override
	public void createEnemy() {
		Random random = new Random();
		teraEnemy = new TeraEnemy(POSX[random.nextInt(2)], 100,
				manager.enemySprite, this.vbom, camera, physics);
		this.attachChild(teraEnemy);
	}

	public void createFlyEnemy() {
		Random random = new Random();
		fly = new FlyEnemy(random.nextInt((int) camera.getBoundsWidth()),
				(camera.getHeight() / 4f) * 3,
				manager.flyEnemySprite.deepCopy(), vbom, camera, physics, this,
				manager.flyEnemyBullet);
		this.attachChild(fly);
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
