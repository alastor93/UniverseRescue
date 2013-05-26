package org.escoladeltreball.universerescue.game;

import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.physics.box2d.Body;

public abstract class Enemy extends AnimatedSprite {

	// Scene reference
	protected BoundCamera camera;
	protected PhysicsWorld physics;

	// Enemy attributes
	// Position
	protected Body body;
	protected float X;
	protected float Y;

	// attack
	protected boolean canAttack = true;
	protected int damage = 10;
	protected int hp = 100;
	protected int at;

	public Enemy(float pX, float pY, ITiledTextureRegion pTiledTextureRegion,
			VertexBufferObjectManager pVertexBufferObject, BoundCamera cam,
			PhysicsWorld physicsWorld) {
		super(pX, pY, pTiledTextureRegion, pVertexBufferObject);
		X = pX;
		Y = pY;
		camera = cam;
		physics = physicsWorld;
	}
	
	@Override
	protected void preDraw(GLState pGLState, Camera pCamera) {
		super.preDraw(pGLState, pCamera);
		pGLState.enableDither();
	}

	/**
	 * the enemy move to the limits declarated in the vars
	 * using the methods movelefttoright and moverighttoleft
	 */
	public abstract void move();

	public abstract void attack(Player p, Sprite bullet);

	public abstract void takeDamage(int dmg);

	public boolean canAttack() {
		return this.canAttack;
	}

	public int getHP() {
		return hp;
	}
	
	public int getAt() {
		return at;
	}
}
