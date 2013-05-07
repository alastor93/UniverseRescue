package org.escoladeltreball.universerescue.game;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.math.MathUtils;
import org.escoladeltreball.universerescue.managers.ResourcesManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;

public class Player extends AnimatedSprite {
	private Body dynamicBody;
	private Body bulletBody;
	private boolean isJump;
	private double directionX;
	private double directionY;

	public void setDirection(double directionX,double directionY) {
		this.directionX = directionX;
		this.directionY = directionY;
	}

	public Player(float pX, float pY, ITiledTextureRegion pTiledTextureRegion,
			VertexBufferObjectManager VertexBufferObject, Camera camera,
			PhysicsWorld physicsWorld) {
		super(pX, pY, pTiledTextureRegion, VertexBufferObject);
		this.setScale(2);
		this.createPhysics(camera, physicsWorld);
		camera.setChaseEntity(this);
	}

	private void createPhysics(Camera camera, PhysicsWorld physicsWorld) {
		dynamicBody = PhysicsFactory.createBoxBody(physicsWorld, this,
				BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 1));
		dynamicBody.setUserData("player");
		dynamicBody.setFixedRotation(true);
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(this,
				dynamicBody, true, false));
		physicsWorld.setContactListener(new ContactListener() {

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
				// TODO Auto-generated method stub

			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
				// TODO Auto-generated method stub

			}

			@Override
			public void endContact(Contact contact) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beginContact(Contact contact) {
				isJump = false;
			}
		});
	}

	public void jump() {
		if (!isJump) {
			dynamicBody.setLinearVelocity(new Vector2(dynamicBody
					.getLinearVelocity().y, 6));
			isJump = true;
		}
	}

	public void fire(Scene scene, PhysicsWorld physicsWorld) {
		Sprite bullet = new Sprite(this.getX() + 60, this.getY(),
				ResourcesManager.getInstance().bulletSprite,
				this.getVertexBufferObjectManager());
		Vector2 velocity = Vector2Pool.obtain(10, 0);
		if (directionX < 0) {
			bullet = new Sprite(this.getX() - 60, this.getY(),
					ResourcesManager.getInstance().bulletSprite,
					this.getVertexBufferObjectManager());
			bullet.setFlippedHorizontal(true);
			velocity = Vector2Pool.obtain(-10, 0);
		} else if (directionY > 0) {
			bullet = new Sprite(this.getX(), this.getY() + 60,
					ResourcesManager.getInstance().bulletVSprite,
					this.getVertexBufferObjectManager());
			velocity = Vector2Pool.obtain(0, 10);
		}
		bullet.setRotation(this.getRotation());

		final FixtureDef bulletFixtureDef1 = PhysicsFactory.createFixtureDef(0,
				0, 0);
		this.bulletBody = PhysicsFactory.createBoxBody(physicsWorld, bullet,
				BodyType.KinematicBody, bulletFixtureDef1);
		this.bulletBody.setLinearVelocity(velocity);
		Vector2Pool.recycle(velocity);
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(bullet,
				this.bulletBody, true, false));
		scene.attachChild(bullet);
	}

	public void run(float pValueX) {
		dynamicBody.setLinearVelocity(pValueX * 10,
				dynamicBody.getLinearVelocity().y);
		this.animate(300, true);
	}
}
