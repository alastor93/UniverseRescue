package org.escoladeltreball.universerescue.game;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.shape.IShape;
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
import org.escoladeltreball.universerescue.managers.ResourcesManager;
import org.escoladeltreball.universerescue.managers.SFXManager;
import org.escoladeltreball.universerescue.scenes.GameScene;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class Player extends AnimatedSprite implements IAnimationListener {
	private Body dynamicBody;
	private Body bulletBody;
	private int hp;
	private boolean isJump;
	private boolean isFire;
	private boolean isAttacked;
	private double directionY;
	private int numSteps;
	private PhysicsWorld physicsWorld;
	private GameScene scene;
	

	public void setDirection(double directionY) {
		this.directionY = directionY;
	}

	public Player(float pX, float pY, ITiledTextureRegion pTiledTextureRegion,
			VertexBufferObjectManager VertexBufferObject, Camera camera,
			PhysicsWorld physicsWorld, GameScene s) {
		super(pX, pY, pTiledTextureRegion, VertexBufferObject);
		scene = s;
		this.setScale(2);
		this.setHp(240);
		this.physicsWorld = physicsWorld;
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
		Vector2 vector2 = Vector2Pool.obtain(0, 7.5f);
		if (!isJump) {
			dynamicBody.setLinearVelocity(vector2);
			Vector2Pool.recycle(vector2);
			this.animate(new long[] { 200, 200, 200 }, new int[] { 6, 7, 8 },
					false);
			isJump = true;
		}
	}

	public void attacked() {
		float pX = -10;
		float py = 3;
		if (this.isFlippedHorizontal()) {
			pX = 10;
		}
		Vector2 vector2 = Vector2Pool.obtain(pX, py);
		dynamicBody.setLinearVelocity(vector2);
		Vector2Pool.recycle(vector2);
	}

	public synchronized void fire(Sprite sprite) {
		isFire = true;
		SFXManager.playShoot(1f, 0.5f);
		sprite.setPosition(this.getX() + 95, this.getY());
		Vector2 velocity = Vector2Pool.obtain(10, 0);
		if (isFlippedHorizontal() && directionY <= 0) {
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
			this.animate(new long[] { 200, 200 }, new int[] { 13, 8 }, false,
					this);
		} else if (!isJump && directionY > 0) {
			this.animate(new long[] { 300, 300 }, new int[] { 10, 9 }, false,
					this);
		} else if (isJump && directionY > 0) {
			this.animate(new long[] { 300, 300 }, new int[] { 12, 9 }, false,
					this);
		} else {
			this.animate(new long[] { 300, 300 }, new int[] { 11, 9 }, false,
					this);

		}
	}

	public void run(float pValueX) {
		Vector2 velocity = Vector2Pool.obtain(pValueX * 10,
				dynamicBody.getLinearVelocity().y);
		if (pValueX != 0 && !isJump && !isFire) {
			numSteps = numSteps > 5 ? 0 : numSteps;
			this.setCurrentTileIndex(numSteps);
			numSteps++;
		} else if (pValueX == 0 && !isJump && !isFire && !isAttacked) {
			this.setCurrentTileIndex(9);
		}
		if (!isFire) {
			dynamicBody.setLinearVelocity(velocity);
			Vector2Pool.recycle(velocity);
		} else {
			dynamicBody.setLinearVelocity(0, 0);
		}
	}

	public boolean isAttacked() {
		return isAttacked;
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
		isAttacked = false;
	}
	
	public void stop() {
		dynamicBody.setLinearVelocity(0, 0);
	}

	public void setJump(boolean isJump) {
		this.isJump = isJump;
	}

	public void setAttack(boolean isAttacked) {
		this.isAttacked = isAttacked;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}
	
	public int shoot(){
		return 20;
	}
	
	public void detachAttack(final Body currentBody) {
		ResourcesManager.getActivity().runOnUpdateThread(new Runnable() {

			@Override
			public void run() {
				physicsWorld.unregisterPhysicsConnector(physicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape((IShape) scene.getPlayerAttack()));
				bulletBody.setActive(false);
				physicsWorld.destroyBody(currentBody);

			}

		});
	}
	
	public Body getBulletBody() {
		return this.bulletBody;
	}
}
