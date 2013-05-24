package org.escoladeltreball.universerescue.game;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.AnimatedSprite.IAnimationListener;
import org.andengine.entity.sprite.vbo.ITiledSpriteVertexBufferObject;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.opengl.shader.ShaderProgram;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.escoladeltreball.universerescue.managers.ResourcesManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class FinalBoss extends Enemy implements IAnimationListener {

	private float initX = this.getX();
	private boolean back;
	private PhysicsConnector physicsConnector;

	public FinalBoss(float pX, float pY,
			ITiledTextureRegion pTiledTextureRegion,
			VertexBufferObjectManager pVertexBufferObject, Camera cam,
			PhysicsWorld physicsWorld) {
		super(pX, pY, pTiledTextureRegion, pVertexBufferObject, cam,
				physicsWorld);
		this.hp = 200;
		this.at = 40;
		this.createPhysics(camera, physicsWorld);
		this.animate(new long[] { 50, 50, 50, 50 }, 1, 4, true);
		// TODO Auto-generated constructor stub
	}

	private void createPhysics(Camera camera, PhysicsWorld physicsWorld) {
		body = PhysicsFactory.createBoxBody(physicsWorld, this,
				BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 1));
		body.setUserData("finalBoss");
		this.physicsConnector = new PhysicsConnector(this, body, true, false);
		physicsWorld.registerPhysicsConnector(physicsConnector);
	}

	@Override
	public void move() {
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

	@Override
	public void attack(Player p, Sprite bullet) {
		// TODO Auto-generated method stub

	}

	@Override
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
						System.out.println(getHP());
						body.setActive(false);
						physics.destroyBody(body);
						detachSelf();
						System.gc();
					}
				});
	}

	public void jump() {
		Vector2 vector2 = Vector2Pool.obtain(0, 7.5f);
		body.setLinearVelocity(vector2);
		Vector2Pool.recycle(vector2);
		this.animate(new long[] { 200, 200, 200 },9,11,false,this);

	}

	@Override
	public void onAnimationStarted(AnimatedSprite pAnimatedSprite,
			int pInitialLoopCount) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationFrameChanged(AnimatedSprite pAnimatedSprite,
			int pOldFrameIndex, int pNewFrameIndex) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationLoopFinished(AnimatedSprite pAnimatedSprite,
			int pRemainingLoopCount, int pInitialLoopCount) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationFinished(AnimatedSprite pAnimatedSprite) {
		this.animate(new long[] { 50, 50, 50, 50 }, 1, 4, true);
	}

}
