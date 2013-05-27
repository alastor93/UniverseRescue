package org.escoladeltreball.universerescue.levels;

import java.util.Iterator;
import java.util.LinkedList;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.util.GLState;
import org.andengine.util.adt.color.Color;
import org.escoladeltreball.universerescue.GameActivity;
import org.escoladeltreball.universerescue.game.BulletPool;
import org.escoladeltreball.universerescue.game.FinalBoss;
import org.escoladeltreball.universerescue.game.Platform;
import org.escoladeltreball.universerescue.game.Player;
import org.escoladeltreball.universerescue.game.Wall;
import org.escoladeltreball.universerescue.managers.SceneManager;
import org.escoladeltreball.universerescue.scenes.GameScene;

import android.hardware.SensorManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class level3 extends GameScene {
	// Attributes
	private Sprite enemyHeal;
	protected Rectangle healstateEnemy;
	private PhysicsWorld physics;
	private FinalBoss finalBoss;
	private Platform platform, platform2, platform3, platform4, platform5,
			platform6;

	@Override
	public void createScene() {
		super.createScene();
		createPlatform();
		createPlayer();
		createEnemy();
		createHealthEnemyBar();
		// Modify HUD for set Final Boss Text
		this.camera.getHUD().detachChild(this.enemiesLeftText);
		this.enemiesLeftText = new Text(manager.camera.getWidth() / 2f,
				(this.camera.getHeight() / 2f) * 1.8f, manager.bossFont,
				"FINAL BOSS", manager.vbom);
		this.camera.getHUD().attachChild(this.enemiesLeftText);
		SceneManager.getInstance().showIntroFinalBLayer(false);
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
		Sprite sprite = new Sprite(0, 0, manager.game_background3, vbom) {
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

	@Override
	public void createPlayer() {
		this.player = new Player(20, 100, manager.playerSprite, this.vbom,
				camera, physics, this);
		this.attachChild(player);
	}

	@Override
	public void createBulletPool() {
		playerBulletList = new LinkedList<Sprite>();
		PLAYER_BULLET_POOL = new BulletPool(manager.bulletSprite,
				playerBulletList, this);

	}

	@Override
	public void createEnemy() {
		final float initX = 1500;
		finalBoss = new FinalBoss(1500, 100, manager.finalBoss, this.vbom,
				camera, physics);
		this.attachChild(finalBoss);

	}

	@Override
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
				}
				if (areBodiesContacted("finalBoss", "platform", contact)) {
					contact.setEnabled(false);
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

			}

			@Override
			public void beginContact(Contact contact) {

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
				if (areBodiesContacted("player", "item", contact)) {
					if (player.getHp() + 20 < 240) {
						player.setHp(player.getHp() + 20);
					}
					item.removeItem();
				}
				if (areBodiesContacted("player", "finalBoss", contact)) {
					player.setHp(player.getHp() - finalBoss.getAt());
					healstate.setWidth(player.getHp());
					player.setAttack(true);
					player.attacked();
					player.animate(new long[] { 600, 200 },
							new int[] { 14, 9 }, false, player);
					finalBoss.animate(new long[] { 100, 100, 100, 100 }, 5, 8,
							false, finalBoss);

				}
				if (areBodiesContacted("bullet", "finalBoss", contact)) {
					finalBoss.jump();
					player.detachAttack(getBody(physics, fire));
					bulletToBeRecycled.add(fire);
					finalBoss.takeDamage(player.shoot());
					healstateEnemy.setWidth(finalBoss.getHP());
				}

			}
		});

	}

	/**
	 * Create and add to scene platforms
	 */
	public void createPlatform() {
		this.platform = new Platform(700f, 100f, manager.platformSprite,
				this.vbom, camera, physics);

		this.platform2 = new Platform(1200, 180f, manager.platformSprite,
				this.vbom, camera, physics);
		this.platform3 = new Platform(500, 180f, manager.platformSprite,
				this.vbom, camera, physics);
		this.platform4 = new Platform(1400, 100f, manager.platformSprite,
				this.vbom, camera, physics);
		this.platform5 = new Platform(900, 300f, manager.platformSprite,
				this.vbom, camera, physics);
		this.platform6 = new Platform(700, 280f, manager.platformSprite,
				this.vbom, camera, physics);
		this.attachChild(platform);
		this.attachChild(platform2);
		this.attachChild(platform3);
		this.attachChild(platform4);
		this.attachChild(platform5);
		this.attachChild(platform6);
		platform6.setVisible(false);
	}

	/**
	 * Add the healEnemyBar to the HUD
	 */
	public void createHealthEnemyBar() {
		enemyHeal = new Sprite(this.camera.getXMax() - 150,
				(this.camera.getHeight() / 2f) * 1.8f, manager.life, vbom);
		healstateEnemy = new Rectangle(this.camera.getXMax() - 270,
				(this.camera.getHeight() / 2f) * 1.83f, 240, 22, vbom);
		healstateEnemy.registerUpdateHandler(new IUpdateHandler() {

			@Override
			public void reset() {
			}

			@Override
			public void onUpdate(float pSecondsElapsed) {

				if (finalBoss.getHP() > 160) {
					healstateEnemy.setColor(Color.GREEN);
				} else if (finalBoss.getHP() <= 160 && finalBoss.getHP() > 80) {
					healstateEnemy.setColor(Color.YELLOW);
				} else {
					healstateEnemy.setColor(Color.RED);
				}
			}
		});
		healstateEnemy.setAnchorCenterX(0f);
		this.camera.getHUD().attachChild(enemyHeal);
		this.camera.getHUD().attachChild(healstateEnemy);
	}

	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		//execute the method for move the finalboss
		finalBoss.moveWithInt(player.getX());
		//Show the Win layer when the healenemybar is 0
		if (healstateEnemy.getWidth() == 0) {
			SceneManager.getInstance().showWinLayer(false);
		}
		//Show the game over layer when the healbar of player is 0
		if (player.getHp() <= 0) {
			SceneManager.getInstance().showLoseLayer(false);
		}
		//Set the platform invisible to visible
		if (player.collidesWith(platform6)) {
			platform6.setVisible(true);
		}
		//Add the potion item
		if (player.getHp() <= 150 && !addItem) {
			addItem = true;
			this.createItem(900, camera.getHeight() - 30, manager.item, physics);
		}
		super.onManagedUpdate(pSecondsElapsed);
	}

}