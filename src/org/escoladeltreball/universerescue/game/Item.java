package org.escoladeltreball.universerescue.game;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
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

	// Attributes
	private Body itemBody;
	private PhysicsWorld physicsWorld;
	private PhysicsConnector physicsConnector;

	// Constructor
	public Item(float pX, float pY, ITextureRegion pTiledTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager,
			Camera camera, PhysicsWorld physicsWorld) {
		super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
		this.physicsWorld = physicsWorld;
		this.createPhysics();
	}

	/**
	 * Create the physics of the item
	 * 
	 * @param camera
	 */
	private void createPhysics() {
		itemBody = PhysicsFactory.createBoxBody(physicsWorld, this,
				BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));
		itemBody.setUserData("item");
		physicsConnector = new PhysicsConnector(this, itemBody, true, false);
		physicsWorld.registerPhysicsConnector(physicsConnector);
	}

	/**
	 * Remove the item
	 */
	public void removeItem() {
		ResourcesManager.getInstance().activity
				.runOnUpdateThread(new Runnable() {

					@Override
					public void run() {
						physicsWorld
								.unregisterPhysicsConnector(physicsConnector);
						itemBody.setActive(false);
						physicsWorld.destroyBody(itemBody);
						detachSelf();
						System.gc();
					}
				});
	}

	public void healt(Player player) {
		if (player.getHp() < 240 && player.getHp() + 20 <= 240) {
			player.setHp(player.getHp() + 20);
		} else if (player.getHp() + 20 > 240) {
			int subtraction = 240 - player.getHp();
			player.setHp(player.getHp() + subtraction);
		}
	}
	
	public void setShield(Player player) {
		Sprite sprite = new Sprite(20, 20,ResourcesManager.getInstance().shield, ResourcesManager.getInstance().vbom);
		player.attachChild(sprite);
		player.setShield(true);
	}
	
	public void eliminateTimePassed() {
		this.registerUpdateHandler(new TimerHandler(20, true, new ITimerCallback() {
			
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				removeItem();
			}
		}));
		
	}
}
