package org.escoladeltreball.universerescue.game;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.AnimatedSprite.IAnimationListener;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class Player extends AnimatedSprite implements IAnimationListener {
	private Body dynamicBody;
	private Body bulletBody;
	private boolean isJump;
	private boolean isFire;
	private double directionY;
	private int numSteps;

	public void setDirection(double directionY) {
		this.directionY = directionY;
	}

	public Player(float pX, float pY, ITiledTextureRegion pTiledTextureRegion,
			VertexBufferObjectManager VertexBufferObject, Camera camera,
			PhysicsWorld physicsWorld) {
		super(pX, pY, pTiledTextureRegion, VertexBufferObject);
		this.setScale(2);
		this.createPhysics(camera, physicsWorld);
		camera.setChaseEntity(this);
	}

	@Override
	protected void preDraw(GLState pGLState, Camera pCamera) {
		super.preDraw(pGLState, pCamera);
		pGLState.enableDither();
	}

	private void createPhysics(Camera camera, PhysicsWorld physicsWorld) {
		dynamicBody = PhysicsFactory.createBoxBody(physicsWorld, this,
				BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 1));
		dynamicBody.setUserData("player");
		dynamicBody.setFixedRotation(true);
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(this,
				dynamicBody, true, false));
	}

	public void jump() {
		if (!isJump) {
			dynamicBody.setLinearVelocity(new Vector2(0, 7.5f));
			this.animate(new long[] { 200, 200, 200 }, new int[] { 6, 7, 8 },
					false);
			isJump = true;
		}
	}

	public synchronized void fire(PhysicsWorld physicsWorld, Sprite sprite) {
		isFire = true;
		sprite.setPosition(this.getX() + 95, this.getY());
		Vector2 velocity = Vector2Pool.obtain(10, 0);
		if (isFlippedHorizontal()) {
			sprite.setFlippedHorizontal(true);
			sprite.setPosition(this.getX() - 95, this.getY());
			velocity = Vector2Pool.obtain(-10, 0);
		} else if (directionY > 0) {
			sprite.setPosition(this.getX(), this.getY() + 95);
			sprite.setRotation(-90);
			velocity = Vector2Pool.obtain(0, 10);
		}
		final FixtureDef bulletFixtureDef1 = PhysicsFactory.createFixtureDef(0,
				0, 0);
		this.bulletBody = PhysicsFactory.createBoxBody(physicsWorld, sprite,
				BodyType.KinematicBody, bulletFixtureDef1);
		this.bulletBody.setUserData("bullet");
		this.bulletBody.setLinearVelocity(velocity);
		Vector2Pool.recycle(velocity);
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(sprite,
				this.bulletBody, true, false));
		if (isJump && directionY == 0) {
			this.animate(new long[] { 200, 200}, new int[] { 13, 8}, false, this);
		}else if(!isJump && directionY > 0){
			this.animate(new long[] { 300, 300}, new int[] { 10,
					9}, false, this);
		}else if(isJump && directionY > 0){
			this.animate(new long[] { 300, 300}, new int[] { 12,
					9}, false, this);
		} else {
			this.animate(new long[] { 300, 300}, new int[] { 11,
					9}, false, this);

		}
	}

	public void run(float pValueX) {
		if (pValueX != 0 && !isJump && !isFire) {
			numSteps = numSteps > 5 ? 0 : numSteps;
			this.setCurrentTileIndex(numSteps);
			numSteps++;
		} else if (pValueX == 0 && !isJump && !isFire) {
			this.setCurrentTileIndex(9);
		}
		if (isFire) {
			Vector2 velocity = Vector2Pool.obtain(0, 0);
			dynamicBody.setLinearVelocity(velocity);
			Vector2Pool.recycle(velocity);
		} else {
			Vector2 velocity = Vector2Pool.obtain(pValueX * 10,
					dynamicBody.getLinearVelocity().y);
			dynamicBody.setLinearVelocity(velocity);
			Vector2Pool.recycle(velocity);
		}
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
		isFire = false;
	}

	public void setJump(boolean isJump) {
		this.isJump = isJump;
	}
}
