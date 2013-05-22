package org.escoladeltreball.universerescue.game;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Platform extends Sprite {
	private Body bdBody;
	private float finX = this.getX();
	private boolean back;
	private final float width;
	private final float height;

	public Platform(float pX, float pY, ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager,
			Camera camera, PhysicsWorld physicsWorld) {
		super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
		this.setScale(2);
		this.width = this.getWidth() * this.getScaleX()
				/ PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
		this.height = this.getHeight() * this.getScaleY()
				/ PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
		this.createPhysics(camera, physicsWorld);
	}

	private void createPhysics(Camera camera, PhysicsWorld physicsWorld) {
		Vector2[] vector2s = {
				new Vector2(+0.50000f * width, +0.52941f * height),
				new Vector2(-0.50000f * width, +0.52941f * height) };
		bdBody = PhysicsFactory.createPolygonBody(physicsWorld, this, vector2s,
				BodyType.KinematicBody,
				PhysicsFactory.createFixtureDef(0, 0, 0));
		bdBody.setUserData("platform");
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(this,
				bdBody, true, false));
	}

	public void moveRightToLeft(int init, int limit) {
		bdBody.setUserData("movePlatform");
		if (finX > limit && !back) {
			finX -= 1;
			bdBody.setLinearVelocity(-1.7f, 0);
		} else if (finX == init) {
			back = false;
		} else if (finX == limit || back) {
			finX += 1;
			back = true;
			bdBody.setLinearVelocity(1.7f, 0);
		}
	}

	public void moveLeftToRight(int init, int limit) {
		bdBody.setUserData("movePlatform");
		if (finX < limit && !back) {
			finX += 1;
			bdBody.setLinearVelocity(1.7f, 0);
		} else if (finX == init) {
			back = false;
		} else if (finX == limit || back) {
			finX -= 1;
			back = true;
			bdBody.setLinearVelocity(-1.7f, 0);
		}
	}

	@Override
	protected void preDraw(GLState pGLState, Camera pCamera) {
		super.preDraw(pGLState, pCamera);
		pGLState.setDitherEnabled(true);
	}

}
