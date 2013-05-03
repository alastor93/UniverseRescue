package org.escoladeltreball.universerescue.scenes;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.util.GLState;
import org.escoladeltreball.universerescue.managers.SceneManager.SceneType;

import com.badlogic.gdx.math.Vector2;

public class GameScene extends BaseScene implements IOnSceneTouchListener {

	// VARIABLES //
	
	/** Counter for player's score */ 
	private int score = 0;
	/** Counter for enemies killed */
	private int enemiesKilled = 0;
	/** Our game HUD */
	private HUD gameHUD;
	/** Displays the player's score */
	private Text scoreText;
	/** Game's physics */
	private PhysicsWorld physics;
	
	
	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void createScene() {
		createBackground();
		//createHUD();
		createPhysics();
		setOnSceneTouchListener(this);
	}

	@Override
	public void onBackKeyPressed() {
		// TODO Auto-generated method stub

	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_GAME;
	}

	@Override
	public void disposeScene() {
		// TODO Auto-generated method stub

	}
	
	/**
	 * Build a background for our game scene
	 */ 
	
	public void createBackground() {
		attachChild(new Sprite(camera.getWidth() / 2, camera.getHeight() / 2,
				manager.game_background, vbom) {
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) {
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}
		});
	}
	
	/**
	 * Add score to player's score
	 */ 
	
	public void addToScore(int i) {
		this.score += i;
	}
	
	/**
	 * Create our physics for the level
	 */
	
	public void createPhysics() {
		this.physics = new FixedStepPhysicsWorld(50, new Vector2(0, -5), false);
		registerUpdateHandler(this.physics);
	}

}