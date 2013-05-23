package org.escoladeltreball.universerescue.levels;

import java.util.LinkedList;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.debugdraw.DebugRenderer;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.util.GLState;
import org.escoladeltreball.universerescue.GameActivity;
import org.escoladeltreball.universerescue.game.BulletPool;
import org.escoladeltreball.universerescue.game.Player;
import org.escoladeltreball.universerescue.game.Stalactite;
import org.escoladeltreball.universerescue.game.TeraEnemy;
import org.escoladeltreball.universerescue.game.Wall;
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
	private boolean back,createNewStalactite = true;

	@Override
	public void createScene() {
		super.createScene();
		DebugRenderer debug = new DebugRenderer(physics, vbom);
		this.attachChild(debug);
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
				camera, physics);
		this.attachChild(player);

	}

	@Override
	public void createBulletPool() {
		PLAYER_BULLET_POOL = new BulletPool(manager.bulletSprite, this);
		bulletList = new LinkedList();

	}


	@Override
	public void createEnemy() {
		final float  initX = 1500;
		teraEnemy = new TeraEnemy(1500, 100, manager.enemySprite2, this.vbom,
				camera, physics){
			@Override
			public void move(){
				if (this.getX() - this.getWidth() > 100 && !back) {
					this.setFlippedHorizontal(false);
					body.setLinearVelocity(-1.7f, 0);
				} else if (this.getX() == initX) {
					back = false;
				} else if (this.getX() - this.getWidth() <= 100 || back) {
					this.setFlippedHorizontal(true);
					back = true;
					body.setLinearVelocity(1.7f, 0);
				}
				
			}
		};
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
					teraEnemy.eliminateEnemy();
					addEnemiesKilled(1);
				}
				if (areBodiesContacted("player", "teraEnemy", contact)) {
					player.setHp(player.getHp() - teraEnemy.getAt());
					if (!teraEnemy.isFlippedHorizontal()) {
						teraEnemy.animate(new long[] { 50, 50, 50, 50 }, 4, 7,
								false,teraEnemy);
					}
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
					player.attacked();
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

	@Override
	public void createItem() {
		// TODO Auto-generated method stub

	}

	public void createStalactite() {
		stalactite = new Stalactite((float) Math.random()
				* camera.getWidth(), camera.getHeight() - 30,
				manager.stalactite, this.vbom, camera, physics);
		this.attachChild(stalactite);
	}

	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		if (stalactite.getY() <= 77) {
			stalactite.removeStalac();
			this.createStalactite();
		}else if (!createNewStalactite){
			this.createStalactite();
			createNewStalactite = true;
		}
		teraEnemy.move();
		super.onManagedUpdate(pSecondsElapsed);
	}

}
