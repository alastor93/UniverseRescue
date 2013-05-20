package org.escoladeltreball.universerescue.game;

import java.util.Random;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.modifier.MoveByModifier;
import org.andengine.entity.modifier.PathModifier;
import org.andengine.entity.modifier.PathModifier.IPathModifierListener;
import org.andengine.entity.modifier.PathModifier.Path;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.modifier.IModifier;
import org.escoladeltreball.universerescue.managers.ResourcesManager;

public class FlyEnemy extends Enemy {

	// Attributes //

	// random boolean
	private Random random;

	// Position
	private float minY;
	private float tempX;
	private float tempY;

	// Player position
	float playerX;
	float playerY;

	// randomPath
	private Path path;

	// Methods //

	public FlyEnemy(float pX, float pY, TiledTextureRegion pTiledTextureRegion,
			VertexBufferObjectManager pVertexBufferObject, Camera cam,
			PhysicsWorld physicsWorld) {
		super(pX, pY, pTiledTextureRegion, pVertexBufferObject, cam,
				physicsWorld);
		random = new Random();
		this.setScale(2f);
		// body = PhysicsFactory.createBoxBody(physics, this,
		// BodyType.KinematicBody,
		// PhysicsFactory.createFixtureDef(0, 0, 0));
		// body.setUserData("flyenemy");
		minY = camera.getHeight() / 2f;
		// physicsWorld.registerPhysicsConnector(new PhysicsConnector(this,
		// body,
		// true, false));
	}

	public void move() {
		// if true suposed move positive, false move negative
		if (random.nextBoolean()) {
			// Check if enemy goes out of screen, if it then move negative
			if (X + 80 < camera.getWidth()) {
				tempX = X + 70;
			} else {
				tempX = X - 70;
			}
		} else {
			// Check if enemy goes out of screen, if it then move positive
			if (X - 80 > 0) {
				tempX = X - 70;
			} else {
				tempX = X + 70;
			}

		}
		// random boolean for move Y positive or negative
		if (random.nextBoolean()) {
			// If it goes out of the screen then move Y negative
			if (Y + 80 < camera.getHeight()) {
				tempY = Y + 70;
			} else {
				tempY = Y - 70;
			}
		} else {
			// If it goes more than minY then move Y positive
			if (Y - 80 < minY) {
				tempY = Y + 70;
			} else {
				tempY = Y - 70;
			}
		}
		// MoveXModifier modifier = new MoveXModifier(2, X, tempX, new
		// IEntityModifierListener() {
		//
		// @Override
		// public void onModifierStarted(IModifier<IEntity> pModifier, IEntity
		// pItem) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// @Override
		// public void onModifierFinished(IModifier<IEntity> pModifier, IEntity
		// pItem) {
		// canAttack = true;
		// X = tempX;
		//
		// }
		// });
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
						// final Vector2 vector = new Vector2(X / 32, Y / 32);
						// body.setTransform(vector, 0.0f);
					}

					@Override
					public void onPathWaypointFinished(
							final PathModifier pPathModifier,
							final IEntity pEntity, final int pWaypointIndex) {
						// When finish to move then attack player
						canAttack = true;
						path = null;
					}

					@Override
					public void onPathFinished(
							final PathModifier pPathModifier,
							final IEntity pEntity) {
					}
				});

		this.registerEntityModifier(modifier);
	}

	public void attack(Player p, Sprite bullet) {
		playerX = p.getX();
		playerY = p.getY() - p.getHeight() / 2f;

		if (playerX < X) {
			bullet.setPosition(X - this.getWidth() / 2f, Y);

		} else if (playerX > X) {
			bullet.setPosition(X + this.getWidth() / 2f, Y);
		} else {
			bullet.setPosition(X, Y);
		}
		float posX = playerX - bullet.getX();
		float posY = playerY - bullet.getY();
		// scene.attachChild(bullet);

		MoveByModifier movMByod = new MoveByModifier(1f, posX, posY,
				new IEntityModifierListener() {

					@Override
					public void onModifierStarted(IModifier<IEntity> pModifier,
							IEntity pItem) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onModifierFinished(
							IModifier<IEntity> pModifier, final IEntity pItem) {
						// Delete from scene the enemy attack bullet
						ResourcesManager.getActivity().runOnUpdateThread(
								new Runnable() {
									@Override
									public void run() {
										pItem.detachSelf();
									}
								});
					}

				});

		bullet.registerEntityModifier(movMByod);
		canAttack = false;
	}

	public void takeDamage(int dmg) {
	}

}