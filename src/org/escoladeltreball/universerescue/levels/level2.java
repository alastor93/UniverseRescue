package org.escoladeltreball.universerescue.levels;

import java.util.LinkedList;
import java.util.Random;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.debugdraw.DebugRenderer;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.util.GLState;
import org.escoladeltreball.universerescue.GameActivity;
import org.escoladeltreball.universerescue.game.BulletPool;
import org.escoladeltreball.universerescue.game.Player;
import org.escoladeltreball.universerescue.game.Stalactite;
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

public class level2 extends GameScene {

	private PhysicsWorld physics;
	private Stalactite stalactite;
	private TeraEnemy teraEnemy;
	private boolean createNewStalactite = true;
	private int countEnemies;

	@Override
	public void createScene() {
		super.createScene();
		DebugRenderer debug = new DebugRenderer(physics, vbom);
		this.attachChild(debug);
		createPlayer();
		createStalactite();

	}

	@Override
	public void createBackground() {
		Sprite sprite = new Sprite(0, 0, manager.game_background2, vbom) {
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
		new Wall(GameActivity.getWidth(), 0, GameActivity.getWidth() * 2, 1,
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
		this.player = new Player(camera.getWidth() * 0.5f, 10, manager.playerSprite, this.vbom,
				camera, physics,this);
		this.attachChild(player);

	}

	@Override
	public void createBulletPool() {
		playerBulletList = new LinkedList();
		PLAYER_BULLET_POOL = new BulletPool(manager.bulletSprite, playerBulletList, this);

	}


	@Override
	public void createEnemy() {
		Random random = new Random();
		teraEnemy = new TeraEnemy(POSX[random.nextInt(2)], 10, manager.enemySprite2, this.vbom,
				camera, physics);
		this.attachChild(teraEnemy);

	}

	@Override
	public void createPhysics() {
		this.physics = new FixedStepPhysicsWorld(60, new Vector2(0,
				-SensorManager.GRAVITY_EARTH), false);
		registerUpdateHandler(this.physics);
		physics.setContactListener(new ContactListener() {

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
				if (areBodiesContacted("teraEnemy", "wall", contact)) {
					contact.setEnabled(true);
				} else if (areBodiesContacted("teraEnemy", "teraEnemy", contact)) {
					contact.setEnabled(false);
				}
			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
			}

			@Override
			public void endContact(Contact contact) {
				
			}

			@Override
			public void beginContact(Contact contact) {
				if (areBodiesContacted("bullet", "teraEnemy", contact)) {
					teraEnemy.setKilled(true);
					getBody(physics, teraEnemy).setActive(false);
					teraEnemy.animate(new long[] {700}, new int[]{7}, false,teraEnemy);
					addEnemiesKilled(1);
					countEnemies--;
				}
				if (areBodiesContacted("player", "teraEnemy", contact)) {
					player.setHp(player.getHp() - teraEnemy.getAt());
						teraEnemy.animate(new long[] { 50, 50, 50 }, 4, 6,
								false,teraEnemy);

					healstate.setWidth(player.getHp());
					player.setAttack(true);
					player.attacked();
					player.animate(new long[] { 600, 200 },
							new int[] { 14, 9 }, false, player);
				}
				if (areBodiesContacted("player", "wall", contact)) {
					player.setJump(false);
					player.setCurrentTileIndex(9);
				}
				if(areBodiesContacted("stalactite", "player", contact)){
					stalactite.removeStalac();
					createNewStalactite = false;
					player.setHp(player.getHp() - stalactite.getAt());
					healstate.setWidth(player.getHp());
					player.setAttack(true);
					player.stop();
					player.animate(new long[] { 600, 200 },
							new int[] { 14, 9 }, false, player);
				}
				
				if (areBodiesContacted("stalactite", "teraEnemy", contact)){
					stalactite.removeStalac();
					createNewStalactite = false;
				}
				
			}
		});
	}

	public void createStalactite() {
		stalactite = new Stalactite((float) Math.random()
				* camera.getWidth(), camera.getHeight() - 30,
				manager.stalactite, this.vbom, camera, physics);
		this.attachChild(stalactite);
	}

	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		if (countEnemies < 2) {
			createEnemy();
			countEnemies++;
		}
		if (stalactite.getY() <= 77) {
			stalactite.removeStalac();
			this.createStalactite();
		}else if (!createNewStalactite){
			this.createStalactite();
			createNewStalactite = true;
		}
		if (enemiesKilled == 30) {
			SceneManager.getInstance().showWinLayer(false);
		}
		teraEnemy.move();
		super.onManagedUpdate(pSecondsElapsed);
	}
}
