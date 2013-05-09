package org.escoladeltreball.universerescue.game;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class PlatformMoveX extends AnimatedSprite{
	private Body bdBody;
	private float finX = this.getX();
	private boolean back;



	public PlatformMoveX(float pX, float pY,
			ITiledTextureRegion pTiledTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager,
			Camera camera, PhysicsWorld physicsWorld) {
		super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
		this.createPhysics(camera, physicsWorld);
		//this.animate(200);
	}

	private void createPhysics(Camera camera, PhysicsWorld physicsWorld) {
		bdBody = PhysicsFactory.createBoxBody(physicsWorld, this,
				BodyType.KinematicBody, PhysicsFactory.createFixtureDef(0, 0, 0));
		bdBody.setUserData("platform");
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(this,
				bdBody, true, false));
	}
	
	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		if (finX < 600 && !back) {
			finX += 1;
			this.setPosition(finX, 100f);
			bdBody.setLinearVelocity(1.7f, 0);
		}else if (finX == 400){
			back = false;
		}else if (finX == 600 || back) {
			finX -=1;
			back = true;
			this.setPosition(finX, 100f);
			bdBody.setLinearVelocity(-1.7f, 0);
		}
	}
	 
	@Override
	protected void preDraw(GLState pGLState, Camera pCamera) {
		super.preDraw(pGLState, pCamera);
		pGLState.setDitherEnabled(true);
	}
}
