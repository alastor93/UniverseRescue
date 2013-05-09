package org.escoladeltreball.universerescue.game;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Wall extends Rectangle {
	private Body staticBody;

	public Wall(float pX, float pY, float pWidth, float pHeight,
			VertexBufferObjectManager pVertexBufferObjectManager,PhysicsWorld mPhysicsWorld) {
		super(pX, pY, pWidth, pHeight, pVertexBufferObjectManager);
		this.createPhysics(mPhysicsWorld);
	}
	
	private void createPhysics(PhysicsWorld mPhysicsWorld){
		staticBody = PhysicsFactory.createBoxBody(mPhysicsWorld,this, BodyType.StaticBody,PhysicsFactory.createFixtureDef(0, 0, 0));
		staticBody.setUserData("wall");
	}

}
