package org.escoladeltreball.universerescue.game;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class TeraEnemy extends Enemy {

	private float initX = this.getX();
	private float finX = this.getX();

	public TeraEnemy(float pX, float pY,
			ITiledTextureRegion pTiledTextureRegion,
			VertexBufferObjectManager VertexBufferObject, Camera camera,
			PhysicsWorld physicsWorld) {
		super(pX, pY, pTiledTextureRegion, VertexBufferObject, camera,
				physicsWorld);
		this.setScale(2);
		this.createPhysics(camera, physicsWorld);
		// TODO Auto-generated constructor stub
	}

	private void createPhysics(Camera camera, PhysicsWorld physicsWorld) {
		body = PhysicsFactory.createBoxBody(physicsWorld, this,
				BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 1));
		body.setUserData("teraEnemy");
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body,
				true, false));
	}

	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);
	}

	public void move() {
		if (finX > 0) {
			finX -= 1;
			this.setPosition(finX, 100f);
			body.setLinearVelocity(1.7f, 0);
		}
		if (finX == 0) {
			this.setPosition(initX, 100f);
			this.finX = initX;

		}

	}

	public void attack(Player p, Sprite bullet) {
		// TODO
	}

	public void takeDamage(int dmg) {
		if ((hp - dmg) <= 0) {
			hp = 0;
			this.detachSelf();
		} else {
			hp = hp - dmg;
		}
	}

}