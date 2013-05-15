package org.escoladeltreball.universerescue.game;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.escoladeltreball.universerescue.managers.ResourcesManager;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Item extends Sprite {

	private Body itemBody;
	private PhysicsWorld physicsWorld;
	private PhysicsConnector physicsConnector;

	public Item(float pX, float pY, ITextureRegion pTiledTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager,
			Camera camera, PhysicsWorld physicsWorld) {
		super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
		this.physicsWorld = physicsWorld;
		this.createPhysics(camera);
	}

	private void createPhysics(Camera camera) {
		itemBody = PhysicsFactory.createBoxBody(physicsWorld, this,
				BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));
		itemBody.setUserData("item");
		physicsConnector = new PhysicsConnector(this, itemBody, true, false);
		physicsWorld.registerPhysicsConnector(physicsConnector);
	}
	
	public void removeItem(){
		ResourcesManager.getInstance().activity.runOnUpdateThread(new Runnable() {
			
			@Override
			public void run() {
				physicsWorld.unregisterPhysicsConnector(physicsConnector);
				itemBody.setActive(false);
				physicsWorld.destroyBody(itemBody);
				detachSelf();
				System.gc();
			}
		});
	}
}
