package org.escoladeltreball.universerescue.game;

import java.util.Random;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.MoveByModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.PathModifier;
import org.andengine.entity.modifier.PathModifier.IPathModifierListener;
import org.andengine.entity.modifier.PathModifier.Path;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.escoladeltreball.universerescue.managers.ResourcesManager;
import org.escoladeltreball.universerescue.scenes.GameScene;

import android.graphics.Point;
import android.graphics.PointF;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class FlyEnemy extends AnimatedSprite {

	// Attributes //

	// random boolean
	private Random random;

	// Scene reference
	private Camera camera;
	private PhysicsWorld physics;
	private GameScene scene;

	// Player reference
	private Player player;
	// Variable for exact position on enemy's attack
	private float playerX;
	private float playerY;

	// Enemy attributes
	// Position
	protected Body body;
	private float X;
	private float Y;
	private float minY;
	// attack
	private boolean canAttack = true;
	// randomPath
	private Path path;

	// Methods //

	public FlyEnemy(float pX, float pY, TiledTextureRegion pTiledTextureRegion,
			VertexBufferObjectManager pVertexBufferObject, Camera cam,
			PhysicsWorld physicsWorld, Player p, GameScene s) {
		super(pX, pY, pTiledTextureRegion, pVertexBufferObject);
		random = new Random();
		X = pX;
		Y = pY;
		camera = cam;
		player = p;
		scene = s;
		physics = physicsWorld;
		body = PhysicsFactory.createBoxBody(physics, this,
				BodyType.KinematicBody,
				PhysicsFactory.createFixtureDef(0, 0, 0));
		body.setUserData("enemy");
		this.setScale(0.8f);
		minY = camera.getHeight() / 2f;
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body,
				true, false) {

			@Override
			public void onUpdate(float pSecondsElapsed) {
				if (canAttack) {
					if (CoolDown.getInstance().timeHasPassed()) {
						attack();
						// canAttack = false;
					}
				}
			}

			@Override
			public void reset() {
				// TODO Auto-generated method stub
			}
		});
	}

	public void attack() {
		float tempX;
		float tempY = Y;

		// if true suposed move positive, false move negative
		if (random.nextBoolean()) {
			// Check if enemy goes out of screen, if it then move negative
			if (X + 60 < camera.getWidth()) {
				tempX = X + 50;
			} else {
				tempX = X - 50;
			}
		} else {
			// Check if enemy goes out of screen, if it then move positive
			if (X - 60 > 0) {
				tempX = X - 50;
			} else {
				tempX = X + 50;
			}

		}
		// random boolean for move Y or not
		if (random.nextBoolean()) {
			// randon boolean for move Y positive
			if (random.nextBoolean()) {
				// If it goes out of the screen then move Y negative
				if (Y + 35 < camera.getHeight()) {
					tempY = Y + 25;
				} else {
					tempY = Y - 25;
				}
			} else {
				// If it goes more than minY then move Y positive
				if (Y - 35 < minY) {
					tempY = Y + 25;
				} else {
					tempY = Y - 25;
				}
			}
		}
		// Create a new path
		path = new Path(2).to(X, Y).to(tempX, tempY);
		X = tempX;
		Y = tempY;
		// Set a new PathModifier for set action on path event
		PathModifier modifier = new PathModifier(0.5f, path,
				new IPathModifierListener() {
					@Override
					public void onPathStarted(final PathModifier pPathModifier,
							final IEntity pEntity) {
					}

					@Override
					public void onPathWaypointStarted(
							final PathModifier pPathModifier,
							final IEntity pEntity, final int pWaypointIndex) {
						// Move body to the position
						final Vector2 vector = new Vector2(X / 32, Y / 32);
						body.setTransform(vector, 0.0f);
					}

					@Override
					public void onPathWaypointFinished(
							final PathModifier pPathModifier,
							final IEntity pEntity, final int pWaypointIndex) {
						// When finish moving then attack player
						attackPlayer(player);
					}

					@Override
					public void onPathFinished(
							final PathModifier pPathModifier,
							final IEntity pEntity) {
					}
				});
		modifier.setAutoUnregisterWhenFinished(false);
		registerUpdateHandler(physics);
		registerEntityModifier(modifier);
	}

	public void attackPlayer(Player p) {
		playerX = p.getX() + p.getWidth() / 2;
		playerY = p.getY() + p.getHeight() / 2;
		float posX = playerX - this.getX();
		float posY = playerY - this.getY();

		Sprite bullet = new Sprite(0, 0,
				ResourcesManager.getInstance().bulletSprite,
				ResourcesManager.getInstance().vbom);
		if (playerX <= X) {
			bullet.setPosition(X - this.getWidth() / 2f, Y - this.getHeight());

		} else {
			bullet.setPosition(X + this.getWidth() / 2f, Y - this.getHeight());
		}
		scene.attachChild(bullet);

		MoveByModifier movMByod = new MoveByModifier(0.5f, posX, posY);

		bullet.registerEntityModifier(movMByod);
		canAttack = false;
	}
}
