package org.escoladeltreball.universerescue.game;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.AnimatedSprite.IAnimationListener;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.escoladeltreball.universerescue.managers.ResourcesManager;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class TeraEnemy extends Enemy implements IAnimationListener {

	private float initX = this.getX();
	private boolean back;
	private int limit;
	private float pX;
	private PhysicsConnector physicsConnector;

	public TeraEnemy(float pX, float pY,
			ITiledTextureRegion pTiledTextureRegion,
			VertexBufferObjectManager VertexBufferObject, Camera camera,
			PhysicsWorld physicsWorld) {
		super(pX, pY, pTiledTextureRegion, VertexBufferObject, camera,
				physicsWorld);
		this.setScale(1.3f);
		this.at = 20;
		this.pX = pX;
		this.createPhysics(camera, physicsWorld);
		if (pX == 20) {
			this.setFlippedHorizontal(true);
		}
		this.animate(new long[] { 200, 200, 200 }, 1, 3, true);
	}

	private void createPhysics(Camera camera, PhysicsWorld physicsWorld) {
		body = PhysicsFactory.createBoxBody(physicsWorld, this,
				BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 1));
		body.setUserData("teraEnemy");
		this.physicsConnector = new PhysicsConnector(this, body, true, false);
		physicsWorld.registerPhysicsConnector(physicsConnector);
	}

	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);
	}
	
	public void calculateMove() {
		if (pX == 20) {
			limit = 1500;
		} else {
			limit = 20;
		}
	}

	public void move() {
		if (this.getX() - this.getWidth() > limit && !back) {
			this.setFlippedHorizontal(false);
			body.setLinearVelocity(-1.7f, 0);
		} else if (this.getX() == initX) {
			back = false;
		} else if (this.getX() - this.getWidth() <= limit || back) {
			this.setFlippedHorizontal(true);
			back = true;
			body.setLinearVelocity(1.7f, 0);
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

	public void eliminateEnemy() {
		ResourcesManager.getInstance().activity
				.runOnUpdateThread(new Runnable() {

					@Override
					public void run() {
						physics.unregisterPhysicsConnector(physicsConnector);
						takeDamage(20);
						body.setActive(false);
						physics.destroyBody(body);
						detachSelf();
						System.gc();
					}
				});
	}

	@Override
	public void onAnimationStarted(AnimatedSprite pAnimatedSprite,
			int pInitialLoopCount) {
	}

	@Override
	public void onAnimationFrameChanged(AnimatedSprite pAnimatedSprite,
			int pOldFrameIndex, int pNewFrameIndex) {
	}

	@Override
	public void onAnimationLoopFinished(AnimatedSprite pAnimatedSprite,
			int pRemainingLoopCount, int pInitialLoopCount) {
	}

	@Override
	public void onAnimationFinished(AnimatedSprite pAnimatedSprite) {
		this.animate(new long[] { 200, 200, 200 }, 1, 3, true);
		
	}

}