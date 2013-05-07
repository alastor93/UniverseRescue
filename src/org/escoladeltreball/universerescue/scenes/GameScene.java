package org.escoladeltreball.universerescue.scenes;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl.IAnalogOnScreenControlListener;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.ButtonSprite.OnClickListener;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.util.GLState;
import org.escoladeltreball.universerescue.GameActivity;
import org.escoladeltreball.universerescue.game.Platform;
import org.escoladeltreball.universerescue.game.PlatformMoveX;
import org.escoladeltreball.universerescue.game.Player;
import org.escoladeltreball.universerescue.game.Wall;
import org.escoladeltreball.universerescue.managers.SceneManager.SceneType;

import com.badlogic.gdx.math.Vector2;

public class GameScene extends BaseScene implements IOnSceneTouchListener,
		OnClickListener {

	// VARIABLES //

	/** Counter for player's score */
	private int score = 0;
	/** Counter for enemies killed */
	private int enemiesKilled = 0;
	/** Counter for goal enemies */
	private final int enemiesGoal = 30;
	/** Our game HUD */
	private HUD gameHUD;
	/** Displays the enemies remaining */
	private Text enemiesLeftText;
	/** Game's physics */
	private PhysicsWorld physics;
	/** The player */
	private Player player;
	/** Platforms */
	private PlatformMoveX platform;
	private Platform platform2;
	/** Check if scene is touched */
	private boolean isTouched = false;

	@Override
	public void createScene() {
		camera.setBounds(0, 0, GameActivity.getWidth() * 2,
				GameActivity.getHeight());
		camera.setBoundsEnabled(true);
		createBackground();
		createHUD();
		createPhysics();
		createPlayer();
		createControls();
		createPlatform();
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
		Sprite sprite = new Sprite(0, 0, manager.game_background, vbom) {
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) {
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}
		};
		sprite.setWidth(GameActivity.getWidth() * 2);
		sprite.setOffsetCenter(0, 0);
		this.attachChild(sprite);
	}

	public void createHUD() {
		// Build a new HUD for our game
		this.gameHUD = new HUD();
		// Set the HUD to the camera
		this.camera.setHUD(gameHUD);
		this.camera.getHUD().setVisible(false);
		// Create a new Text field for allow view how many enemies remaining
		this.enemiesLeftText = new Text(manager.camera.getWidth() / 2f,
				(this.camera.getHeight() / 2f) * 1.8f, manager.gameFont,
				String.valueOf(this.enemiesKilled) + "/"
						+ String.valueOf(this.enemiesGoal), manager.vbom);
		Sprite heal = new Sprite(manager.camera.getXMin() +50,
				(this.camera.getHeight() / 2f) * 1.8f, manager.life, vbom);
		
		// Put the Text to HUD
		this.camera.getHUD().attachChild(heal);
		this.camera.getHUD().attachChild(this.enemiesLeftText);
		this.camera.getHUD().setVisible(true);
	}

	public void createPlayer() {
		Wall ground = new Wall(GameActivity.getWidth(), 0,
				GameActivity.getWidth() * 2, 1, this.vbom, physics);
		Wall left = new Wall(0, GameActivity.getHeight() / 2f, 1,
				GameActivity.getHeight(), this.vbom, physics);
		Wall right = new Wall(GameActivity.getWidth() * 2,
				GameActivity.getHeight() / 2, 1, GameActivity.getHeight(),
				this.vbom, physics);
		this.attachChild(ground);
		this.attachChild(left);
		this.attachChild(right);
		this.player = new Player(10, 10, manager.playerSprite, this.vbom,
				camera, physics);
		this.attachChild(player);
	}
	
	public void createPlatform() {
		this.platform = new PlatformMoveX(400f, 100f, manager.platformSprite,
				this.vbom, camera, physics);
			
			this.platform2 = new Platform(800f, 200f, manager.platformSprite,
					this.vbom, camera, physics);
		
		this.attachChild(platform);
		this.attachChild(platform2);
	}

	public void createControls() {
		manager.loadControls();
		AnalogOnScreenControl control = new AnalogOnScreenControl(0, 0, camera,
				manager.controlBaseRegion, manager.controlKnobRegion, 0.1f,
				200, vbom, new IAnalogOnScreenControlListener() {

					@Override
					public void onControlChange(
							BaseOnScreenControl pBaseOnScreenControl,
							float pValueX, float pValueY) {
						player.run(pValueX);
						player.setDirection(pValueX,pValueY);
					}

					@Override
					public void onControlClick(
							AnalogOnScreenControl pAnalogOnScreenControl) {
					}
				});
		Sprite controlBase = control.getControlBase();
		ButtonSprite button = new ButtonSprite(GameActivity.getWidth() - 60,
				60, manager.buttonA, vbom, this);
		button.setAlpha(0.5f);
		gameHUD.attachChild(button);
		gameHUD.registerTouchArea(button);
		// Transparency
		controlBase.setAlpha(0.5f);
		// center
		controlBase.setOffsetCenter(0, 0);
		control.getControlKnob().setScale(1.25f);
		this.setChildScene(control);
	}

	/**
	 * Add score to player's score
	 */

	public void addToScore(int i) {
		this.score += i;
	}

	/**
	 * Add to enemies killed
	 */

	public void addEnemiesKilled(int i) {
		if (this.enemiesKilled + i > this.enemiesGoal) {
			this.enemiesKilled = this.enemiesGoal;
		} else {
			this.enemiesKilled += i;
		}
		this.enemiesLeftText.setText(String.valueOf(this.enemiesKilled) + "/"
				+ String.valueOf(this.enemiesGoal));
	}

	/**
	 * Create our physics for the level
	 */

	public void createPhysics() {
		this.physics = new FixedStepPhysicsWorld(50, new Vector2(0, -5), false);
		registerUpdateHandler(this.physics);
	}

	/**
	 * Do an action depends the kind of touch
	 */

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
			this.isTouched = true;
		} else if ((pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP)
				&& this.isTouched) {
			player.jump();
			this.isTouched = false;
		}
		return false;
	}

	@Override
	public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX,
			float pTouchAreaLocalY) {
		player.fire(this, physics);

	}

}