package org.escoladeltreball.universerescue.game;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.physics.box2d.Body;

public abstract class Enemy extends AnimatedSprite {

	// Scene reference
	protected Camera camera;
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

	public Enemy(float pX, float pY, ITiledTextureRegion pTiledTextureRegion,
			VertexBufferObjectManager pVertexBufferObject, Camera cam,
			PhysicsWorld physicsWorld) {
		super(pX, pY, pTiledTextureRegion, pVertexBufferObject);
		X = pX;
		Y = pY;
		camera = cam;
		physics = physicsWorld;
	}

	public abstract void move();

	public abstract void attack(Player p, Sprite bullet);

	public abstract void takeDamage(int dmg);

	public boolean canAttack() {
		return this.canAttack;
	}

	public int getHP() {
		return hp;
	}
}
