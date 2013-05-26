package org.escoladeltreball.universerescue.game;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.vbo.ISpriteVertexBufferObject;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.escoladeltreball.universerescue.managers.ResourcesManager;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Stalactite extends Sprite {
	//Attributes
	private Body stalactiteBody;
	private PhysicsWorld physicsWorld;
	private PhysicsConnector physicsConnector;
	private int at = 10;

	//Constructor
	public Stalactite(float pX, float pY, ITextureRegion pTiledTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager,
			Camera camera, PhysicsWorld physicsWorld) {
		super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
		this.physicsWorld = physicsWorld;
		this.createPhysics(camera);
	}

	/**
	 * Create the physics of the stalactite
	 * @param camera
	 */
	private void createPhysics(Camera camera) {
		stalactiteBody = PhysicsFactory.createBoxBody(physicsWorld, this,
				BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));
		stalactiteBody.setUserData("stalactite");
		physicsConnector = new PhysicsConnector(this, stalactiteBody, true,
				false);
		physicsWorld.registerPhysicsConnector(physicsConnector);
	}
	
	/**
	 * Get the attack of the stalactite
	 * @return dmg of the stalactite
	 */
	public int getAt(){
		return at;
	}

	/**
	 * Remove the stalactite
	 */
	public void removeStalac() {
		ResourcesManager.getInstance().activity
				.runOnUpdateThread(new Runnable() {

					@Override
					public void run() {
						physicsWorld
								.unregisterPhysicsConnector(physicsConnector);
						stalactiteBody.setActive(false);
						physicsWorld.destroyBody(stalactiteBody);
						detachSelf();
					}
				});
	}

}
