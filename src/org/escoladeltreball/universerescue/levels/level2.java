package org.escoladeltreball.universerescue.levels;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.util.GLState;
import org.escoladeltreball.universerescue.GameActivity;
import org.escoladeltreball.universerescue.game.Enemy;
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

	@Override
	public void createScene() {
		// TODO Auto-generated method stub
		super.createScene();
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
		// TODO Auto-generated method stub

	}

	@Override
	public void createPlatform() {
		// TODO Auto-generated method stub

	}

	@Override
	public void createFlyEnemy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void createEnemy() {
		TeraEnemy teraEnemy = new TeraEnemy(1500, 100, manager.enemySprite2, this.vbom,
				camera, physics);
		this.attachChild(teraEnemy);

	}

	@Override
	public void createPhysics() {
		this.physics = new FixedStepPhysicsWorld(60, new Vector2(0,
				-SensorManager.GRAVITY_EARTH), false);
		registerUpdateHandler(this.physics);
	}

	@Override
	public void createItem() {
		// TODO Auto-generated method stub

	}

	public void createStalactite() {
		stalactite = new Stalactite(600f, camera.getHeight() - 30,
				manager.stalactite, this.vbom, camera, physics);
		this.attachChild(stalactite);
	}
	

}
