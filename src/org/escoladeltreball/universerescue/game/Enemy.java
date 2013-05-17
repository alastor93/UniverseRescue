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

public class Enemy extends AnimatedSprite {
	private Body dynamicBody;
	private float initX = this.getX();
	private float finX = this.getX();
	private int damage = 10;
	private int hp = 80;


	public Enemy(float pX, float pY, ITiledTextureRegion pTiledTextureRegion,
			VertexBufferObjectManager VertexBufferObject, Camera camera,
			PhysicsWorld physicsWorld) {
		super(pX, pY, pTiledTextureRegion, VertexBufferObject);
		this.setScale(2);
		this.createPhysics(camera, physicsWorld);
		// TODO Auto-generated constructor stub
	}

	private void createPhysics(Camera camera, PhysicsWorld physicsWorld) {
		dynamicBody = PhysicsFactory.createBoxBody(physicsWorld, this,
				BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 1));
		dynamicBody.setUserData("enemy");
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(this,
				dynamicBody, true, false));
	}

	@Override
	protected void preDraw(GLState pGLState, Camera pCamera) {
		super.preDraw(pGLState, pCamera);
		pGLState.enableDither();
	}


	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);
	}

	public void runEnemy() {
		if (finX > 0) {
			finX -= 1;
			this.setPosition(finX, 100f);
			dynamicBody.setLinearVelocity(1.7f, 0);
		}
		if (finX == 0) {
			this.setPosition(initX, 100f);
			this.finX = initX;

		}

	}
	
	public int  attack(){
		return damage;	
	}
	
	public void takeDamage(int dmg){
		if ((hp - dmg) <= 0){
			hp = 0;
			this.detachSelf();
		}else {
			hp = hp - dmg;
		}
	}
	
	public int getHP(){
		return hp;
	}

}