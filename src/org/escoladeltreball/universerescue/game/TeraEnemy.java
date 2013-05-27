package org.escoladeltreball.universerescue.game;

import org.andengine.engine.camera.BoundCamera;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.AnimatedSprite.IAnimationListener;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.escoladeltreball.universerescue.managers.ResourcesManager;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class TeraEnemy extends Enemy implements IAnimationListener {
	//Attributes
	private float initX;
	private boolean back;
	private final float LIMIT_LEFT = camera.getBoundsWidth() - 20;
	private final float LIMIT_RIGHT = 20;
	private boolean killed;
	private PhysicsConnector physicsConnector;

	//Constructor
	public TeraEnemy(float pX, float pY,
			ITiledTextureRegion pTiledTextureRegion,
			VertexBufferObjectManager VertexBufferObject, BoundCamera camera,
			PhysicsWorld physicsWorld) {
		super(pX, pY, pTiledTextureRegion, VertexBufferObject, camera,
				physicsWorld);
		this.setScale(1.3f);
		this.at = 20;
		this.initX = this.getX();
		this.createPhysics(physicsWorld);
		this.animate(new long[] { 200, 200, 200 }, 1, 3, true);
	}

	/**
	 * Create the physics of the enemy
	 * @param physicsWorld
	 */
	private void createPhysics(PhysicsWorld physicsWorld) {
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

	@Override
	public void move() {
		if (initX == LIMIT_RIGHT) {
			this.moveLeftToRight(LIMIT_LEFT);
		} else {
			this.moveRightToLeft(LIMIT_RIGHT);
		}
	}

	/**
	 * the enemy move right to left 
	 * @param limit
	 */
	public void moveRightToLeft(float limit) {
		if (this.getX() - this.getWidth() * 0.5f > limit && !back) {
			this.setFlippedHorizontal(true);
			body.setLinearVelocity(-1.7f, 0);
		} else if (this.getX() == initX) {
			back = false;
		} else if (this.getX() - this.getWidth() * 0.5f <= limit || back) {
			this.setFlippedHorizontal(false);
			back = true;
			body.setLinearVelocity(1.7f, 0);
		}
	}

	/**
	 * the enemy move left to right 
	 * @param limit 
	 */
	public void moveLeftToRight(float limit) {
		if (this.getX() + this.getWidth() * 0.5f < limit && !back) {
			this.setFlippedHorizontal(false);
			body.setLinearVelocity(1.7f, 0);
		} else if (this.getX() == initX) {
			back = false;
		} else if (this.getX() + this.getWidth() * 0.5f >= limit || back) {
			this.setFlippedHorizontal(true);
			back = true;
			body.setLinearVelocity(-1.7f, 0);
		}
	}

	/**
	 * the enemy take damage 
	 * @param dmg (the life he lose)
	 */
	public void takeDamage(int dmg) {
		if ((hp - dmg) <= 0) {
			hp = 0;
			this.detachSelf();
		} else {
			hp = hp - dmg;
		}
	}

	/**
	 * Remove the enemy
	 */
	public void eliminateEnemy() {
		ResourcesManager.getInstance().activity
				.runOnUpdateThread(new Runnable() {

					@Override
					public void run() {
						physics.unregisterPhysicsConnector(physicsConnector);
						body.setActive(false);
						physics.destroyBody(body);
						detachSelf();
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
		if (killed) {
			this.eliminateEnemy();
		} else {
			this.animate(new long[] { 200, 200, 200 }, 1, 3, true);
		}
	}

	/**
	 * Set the content of the param to the var
	 * @param killed
	 */
	public void setKilled(boolean killed) {
		this.killed = killed;
	}

	@Override
	public void attack(Player p) {
		// TODO Auto-generated method stub
		
	}



}
