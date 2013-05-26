package org.escoladeltreball.universerescue.game;

import java.util.Random;

import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.IEntityFactory;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.modifier.MoveByModifier;
import org.andengine.entity.modifier.PathModifier;
import org.andengine.entity.modifier.PathModifier.IPathModifierListener;
import org.andengine.entity.modifier.PathModifier.Path;
import org.andengine.entity.particle.ParticleSystem;
import org.andengine.entity.particle.emitter.PointParticleEmitter;
import org.andengine.entity.particle.initializer.VelocityParticleInitializer;
import org.andengine.entity.particle.modifier.AlphaParticleModifier;
import org.andengine.entity.particle.modifier.RotationParticleModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.AnimatedSprite.IAnimationListener;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;
import org.andengine.util.modifier.IModifier;
import org.escoladeltreball.universerescue.levels.level1;
import org.escoladeltreball.universerescue.managers.ResourcesManager;

public class FlyEnemy extends Enemy implements IAnimationListener{

	// Attributes //

	// random boolean
	private Random random;

	// Position
	private float minY;
	private float tempX;
	private float tempY;

	// Player position
	private Player player;
	private float playerX;
	private float playerY;

	// Sprite bullet attack
	private Sprite bulletAttack = null;

	// randomPath
	private Path path;
	private level1 scene;

	private boolean killed;
	private boolean contact;
	

	// Methods //

	public FlyEnemy(float pX, float pY, TiledTextureRegion pTiledTextureRegion,
			VertexBufferObjectManager pVertexBufferObject, BoundCamera cam,
			PhysicsWorld physicsWorld, level1 s) {
		super(pX, pY, pTiledTextureRegion, pVertexBufferObject, cam,
				physicsWorld);
		this.canAttack = false;
		scene = s;
		random = new Random();
		this.setScale(2f);
		this.animate(new long[] { 200, 200, 200 }, 0, 2, true);
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
		// Set a new PathModifier for set action on path event
		PathModifier modifier = new PathModifier(0.1f, path, null,
				new IPathModifierListener() {
					@Override
					public void onPathStarted(final PathModifier pPathModifier,
							final IEntity pEntity) {
					}

					@Override
					public void onPathWaypointStarted(
							final PathModifier pPathModifier,
							final IEntity pEntity, final int pWaypointIndex) {
						// If enemy may moves left then flip himself
						if (X > tempX) {
							setFlippedHorizontal(true);
							setCurrentTileIndex(2);
							// If enemy may moves right then do not flip himself
						} else if (X < tempX) {
							setFlippedHorizontal(false);
							setCurrentTileIndex(2);
						}
					}

					@Override
					public void onPathWaypointFinished(
							final PathModifier pPathModifier,
							final IEntity pEntity, final int pWaypointIndex) {

					}

					@Override
					public void onPathFinished(
							final PathModifier pPathModifier,
							final IEntity pEntity) {
						// When finish to move then attack player
						canAttack = true;
						path = null;
						X = tempX;
						Y = tempY;
					}
				});

		this.registerEntityModifier(modifier);
	}

	public void attack(Player p, Sprite bullet) {
		player = p;
		playerX = p.getX();
		playerY = p.getY() - p.getHeight() / 2f;
		bulletAttack = bullet;

		if (playerX < X) {
			bulletAttack.setPosition(X - this.getWidth() / 2f, Y);
			this.setFlippedHorizontal(true);

		} else if (playerX > X) {
			bulletAttack.setPosition(X + this.getWidth() / 2f, Y);
			this.setFlippedHorizontal(false);
		} else {
			bulletAttack.setPosition(X, Y);
		}
		this.animate(new long[] { 200, 200, 200, 200, 200 }, 5, 9, false,this);
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
						// Create an explosion and set Invisible the bullet
						// attack on screen
						pItem.setVisible(false);
						createExplosion(pItem);

					}

				});

		bulletAttack.registerEntityModifier(movMByod);
		canAttack = false;
	}

	public void setKilled(boolean killed) {
		this.killed = killed;
	}

	public void takeDamage(int dmg) {
	}

	public void createExplosion(final IEntity target) {
		int nNumPart = 15;
		int mTimePart = 2;
		PointParticleEmitter particle = new PointParticleEmitter(target.getX(),
				target.getY());
		IEntityFactory<Rectangle> recFact = new IEntityFactory<Rectangle>() {
			@Override
			public Rectangle create(float pX, float pY) {
				Rectangle rect = new Rectangle(target.getX(), target.getY(), 5,
						5, ResourcesManager.getInstance().vbom);
				rect.setColor(Color.PINK);
				return rect;
			}
		};

		// Create a ParticleSysten which we will set the properties to our
		// explosion, like number of rectangles, velocity
		// Alpha modifier, rotation modifier, and much more
		final ParticleSystem<Rectangle> particleSystem = new ParticleSystem<Rectangle>(
				recFact, particle, 500, 500, nNumPart);
		particleSystem
				.addParticleInitializer(new VelocityParticleInitializer<Rectangle>(
						-50, 50, -50, 50));
		particleSystem
				.addParticleModifier(new AlphaParticleModifier<Rectangle>(0,
						0.3f * mTimePart, 1, 0));
		particleSystem
				.addParticleModifier(new RotationParticleModifier<Rectangle>(0,
						mTimePart, 0, 360));
		// Add the partycle system getting the parent of the bullet attack, so
		// our scene
		target.getParent().attachChild(particleSystem);
		// Register an Update Handler for detach particle system after the
		// giving seconds
		target.getParent().registerUpdateHandler(
				new TimerHandler(mTimePart, new ITimerCallback() {

					@Override
					public void onTimePassed(TimerHandler pTimerHandler) {
						ResourcesManager.getActivity().runOnUpdateThread(
								new Runnable() {
									@Override
									public void run() {
										// Detach partycle system and bullet
										// from our scene
										particleSystem.detachSelf();
										scene.flyEnemyBulletList.add((Sprite) target);
										bulletAttack = null;
										contact = false;
									}
								});

					}

				}));
	}

	/**
	 * Detach this enemy from our scene
	 */
	public void eliminateEnemy() {
		ResourcesManager.getInstance().activity
				.runOnUpdateThread(new Runnable() {

					@Override
					public void run() {
						detachSelf();
					}
				});
	}
	
	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		if (bulletAttack != null) {
			if (bulletAttack.isVisible()) {
				if (bulletAttack.collidesWith(player)) {
					createExplosion(bulletAttack);
					if (player.getHp() > 0 && !contact && !player.isSetShield()) {
						player.setHp(player.getHp() - 5);
						contact = true;
					}
				}
			}
		}
		super.onManagedUpdate(pSecondsElapsed);
	}

	public boolean isContact() {
		return contact;
	}

	public void setContact(boolean contact) {
		this.contact = contact;
	}

	@Override
	public void onAnimationStarted(AnimatedSprite pAnimatedSprite,
			int pInitialLoopCount) {
	}

	@Override
	public void onAnimationFrameChanged(AnimatedSprite pAnimatedSprite,
			int pOldFrameIndex, int pNewFrameIndex) {
	}

	@Override
	public void onAnimationLoopFinished(AnimatedSprite pAnimatedSprite,
			int pRemainingLoopCount, int pInitialLoopCount) {
	}

	@Override
	public void onAnimationFinished(AnimatedSprite pAnimatedSprite) {
		if (killed) {
			this.eliminateEnemy();
		} else {
			this.animate(new long[] { 200, 200, 200 }, 0, 2, true);
		}
	}

}