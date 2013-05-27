package org.escoladeltreball.universerescue.managers;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.scene.CameraScene;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.ui.IGameInterface.OnCreateSceneCallback;
import org.escoladeltreball.universerescue.GameActivity;
import org.escoladeltreball.universerescue.layers.ControlsLayer;
import org.escoladeltreball.universerescue.layers.GameOverLayer;
import org.escoladeltreball.universerescue.layers.IntroFB;
import org.escoladeltreball.universerescue.layers.Layer;
import org.escoladeltreball.universerescue.layers.OptionsLayer;
import org.escoladeltreball.universerescue.layers.WinLayer;
import org.escoladeltreball.universerescue.levels.LevelSelector;
import org.escoladeltreball.universerescue.levels.Level1;
import org.escoladeltreball.universerescue.levels.Level2;
import org.escoladeltreball.universerescue.levels.Level3;
import org.escoladeltreball.universerescue.scenes.BaseScene;
import org.escoladeltreball.universerescue.scenes.GameScene;
import org.escoladeltreball.universerescue.scenes.LoadingScene;
import org.escoladeltreball.universerescue.scenes.MainMenuScene;
import org.escoladeltreball.universerescue.scenes.SplashScene;

import android.view.MotionEvent;

public class SceneManager {
	// Attributes
	private BaseScene splashScene;
	private BaseScene loadingScene;
	private LevelSelector levelScene;
	private GameScene gameScene;
	private static SceneManager obj = null;
	private Engine engine = ResourcesManager.getInstance().engine;
	private SceneType currentSceneType = SceneType.SCENE_SPLASH;
	private BaseScene currentScene;
	private int currentlevel;

	private final float centerX = (GameActivity.getWidth() * 0.5f);
	private final float centerY = (GameActivity.getHeight() * 0.5f);

	/** BaseScene of MainMenu */
	private BaseScene mainMenu;
	public boolean isLayerShown;
	private Scene placeholderModalScene;
	private boolean cameraHadHud;
	public Layer currentLayer;

	// Constructor
	public SceneManager() {
	}

	// Singleton
	public static SceneManager getInstance() {
		if (obj == null) {
			obj = new SceneManager();
		}
		return obj;
	}

	// Methods
	public enum SceneType {
		SCENE_SPLASH, SCENE_MAINMENU, SCENE_LOADING, SCENE_GAME, SCENE_LEVEL;
	}

	/**
	 * Create the Splash Scene
	 * 
	 * @param pOnCreateSceneCallback
	 */
	public void createSplashScene(OnCreateSceneCallback pOnCreateSceneCallback) {
		ResourcesManager.getInstance().loadSplashScreen();
		splashScene = new SplashScene();
		currentScene = splashScene;
		pOnCreateSceneCallback.onCreateSceneFinished(splashScene);
	}

	/**
	 * Dispose and put the splashscene to null
	 */
	private void disposeSplashScene() {
		ResourcesManager.getInstance().unloadSplashScreen();
		splashScene.disposeScene();
		splashScene = null;
	}

	/**
	 * Calls a MainMenuScene and LoadingScene, splashScene goes to dispose and
	 * until MainMenuScene is not full load, loadingScene should be show.
	 */

	public void createMenuScene() {
		ResourcesManager.getInstance().loadMenuGraphics();
		mainMenu = new MainMenuScene();
		setScene(mainMenu);
		disposeSplashScene();
	}

	/**
	 * Create the LevelScene
	 */
	public void createLevelScene() {
		// load the graphics of the levelSelector
		ResourcesManager.getInstance().loadLevelSelectorGraphics();
		this.levelScene = new LevelSelector(100, 100,
				ResourcesManager.getInstance().menuLevelIcon,
				ResourcesManager.getInstance().levelsFont);
		setScene(this.levelScene);
		mainMenu.detachSelf();
		// back sprite
		final ButtonSprite backToMenuButton = new ButtonSprite(
				ResourcesManager.getInstance().backarrow.getWidth() / 2f,
				ResourcesManager.getInstance().backarrow.getHeight() / 2f,
				ResourcesManager.getInstance().backarrow,
				engine.getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if (pSceneTouchEvent.getAction() == MotionEvent.ACTION_UP) {
					backToMenu();
				}
				return true;
			}
		};
		// attach the elements to the scene
		this.levelScene.attachChild(backToMenuButton);
		this.levelScene.registerTouchArea(backToMenuButton);
		backToMenuButton.setScale(1.5f);
		backToMenuButton.setOffsetCenter(0, 0);
	}

	/**
	 * Dispose the levelScene
	 */
	public void unloadLevelScene() {
		levelScene.detachSelf();
		levelScene.dispose();
	}

	/**
	 * Pause the game
	 */
	public void pauseScene() {
		CameraScene pauseScene = new CameraScene(
				ResourcesManager.getInstance().camera);
		Sprite pausedSprite = new Sprite(centerX, centerY,
				ResourcesManager.getInstance().pause,
				ResourcesManager.getInstance().vbom);
		pausedSprite.setScale(2f);
		pauseScene.attachChild(pausedSprite);
		pauseScene.setBackgroundEnabled(false);
		gameScene.setChildScene(pauseScene, false, true, true);
	}

	/**
	 * If the game stay in pause clear the pause
	 */
	public void clearPause() {
		gameScene.clearChildScene();
		gameScene.createControls();
	}

	/**
	 * return to the mainmenu
	 */
	public void backToMenu() {
		mainMenu = new MainMenuScene();
		setScene(mainMenu);
		this.unloadLevelScene();
	}

	/**
	 * return to the levelmenu
	 */
	public void backToLevelMenu() {
		this.createLevelScene();
		gameScene.disposeScene();
	}

	/**
	 * Create the gameScene depend of the level you select
	 * 
	 * @param engine
	 * @param level
	 */
	public void createTempGameScene(final Engine engine, final int level) {
		//load and set the loading scene
		ResourcesManager.getInstance().loadLoadingScreen();
		this.loadingScene = new LoadingScene();
		this.setScene(loadingScene);
		engine.registerUpdateHandler(new TimerHandler(0.1f,
				new ITimerCallback() {
					public void onTimePassed(final TimerHandler pTimerHandler) {
						engine.unregisterUpdateHandler(pTimerHandler);
						loadingScene.disposeScene();
						currentlevel = level;
						ResourcesManager.getInstance().loadGameGraphics();
						// load the level 1
						if (level == 1) {
							ResourcesManager.getInstance().loadLevel1Graphics();
							gameScene = new Level1();
							// load the level 2
						} else if (level == 2) {
							ResourcesManager.getInstance().loadLevel2Graphics();
							gameScene = new Level2();
							// load the level 3
						} else {
							ResourcesManager.getInstance().loadLevel3Graphics();
							gameScene = new Level3();
						}
						//set the gameScene
						setScene(gameScene);
					}
				}));
	}
	/**
	 * Get the current level you stay
	 * @return currentlevel
	 */
	public int getCurrentlevel() {
		return currentlevel;
	}

	/**
	 * Dispose the GameScene
	 */
	public void unloadGameScene() {
		gameScene.disposeScene();
	}
	
	/**
	 * Set the scene in the param
	 * @param scene
	 */
	public void setScene(BaseScene scene) {
		engine.setScene(scene);
		this.currentScene = scene;
		this.currentSceneType = scene.getSceneType();
	}

	/**
	 * set the scene depends of the type you put in the param
	 * @param sceneType
	 */
	public void setScene(SceneType sceneType) {
		switch (sceneType) {
		case SCENE_SPLASH:
			setScene(splashScene);
			break;
		case SCENE_MAINMENU:
			setScene(mainMenu);
			break;
		case SCENE_LOADING:
			setScene(loadingScene);
			break;
		default:
			break;
		}
	}
	/**
	 * Get the current scenetype
	 * @return currentSceneType
	 */
	public SceneType getCurrentSceneType() {
		return currentSceneType;
	}
	/**
	 * Get the current Scene
	 * @return currentScene
	 */
	public BaseScene getCurrentScene() {
		return currentScene;
	}
	
	/**
	 * Show the layer of the options
	 * @param pSuspendCurrentSceneUpdates
	 */
	public void showOptionsLayer(final boolean pSuspendCurrentSceneUpdates) {
		showLayer(OptionsLayer.getInstance(), false,
				pSuspendCurrentSceneUpdates, true);
	}
	/**
	 * Show the layer of the Controls
	 * @param pSuspendCurrentSceneUpdates
	 */
	public void showControlsLayer(final boolean pSuspendCurrentSceneUpdates) {
		showLayer(ControlsLayer.getInstance(), false,
				pSuspendCurrentSceneUpdates, true);
	}
	/**
	 * Show the layer of the finalBoss
	 * @param pSuspendCurrentSceneUpdates
	 */
	public void showIntroFinalBLayer(final boolean pSuspendCurrentSceneUpdates) {
		showLayer(IntroFB.getInstance(), false, pSuspendCurrentSceneUpdates,
				true);
	}
	/**
	 * Show the layer of the GameOver
	 * @param pSuspendCurrentSceneUpdates
	 */
	public void showLoseLayer(final boolean pSuspendCurrentSceneUpdates) {
		showLayer(GameOverLayer.getInstance(), false,
				pSuspendCurrentSceneUpdates, false);
	}
	/**
	 * Show the layer of the Win Game
	 * @param pSuspendCurrentSceneUpdates
	 */
	public void showWinLayer(final boolean pSuspendCurrentSceneUpdates) {
		showLayer(WinLayer.getInstance(), false, pSuspendCurrentSceneUpdates,
				false);
	}

	/**
	 * Shows a layer by placing it as a child to the Camera's HUD.
	 * 
	 * @param pLayer
	 * @param pSuspendSceneDrawing
	 * @param pSuspendSceneUpdates
	 * @param pSuspendSceneTouchEvents
	 */
	public void showLayer(Layer pLayer, boolean pModalDraw,
			boolean pModalUpdate, boolean pModalTouch) {
		// If the camera has a HUD, use it.
		if (engine.getCamera().hasHUD()) {
			cameraHadHud = true;
		} else {
			// Otherwise, create one to use.
			cameraHadHud = false;
			HUD placeholderHud = new HUD();
			engine.getCamera().setHUD(placeholderHud);
		}
		// If the managed layer needs modal properties, set them.
		if (pModalDraw || pModalUpdate || pModalTouch) {
			// Apply the layer directly to the Camera's HUD
			engine.getCamera()
					.getHUD()
					.setChildScene(pLayer, pModalDraw, pModalUpdate,
							pModalTouch);
		} else {
			// If the managed layer does not need to be modal, simply set it to
			// the HUD.
			engine.getCamera().getHUD().setChildScene(pLayer);
		}
		// Set the camera for the layer.
		pLayer.setCamera(engine.getCamera());
		// Let the layer know that it is being shown.
		pLayer.onShowManagedLayer();
		pLayer.removeNext();
		// Reflect that a layer is shown.
		isLayerShown = true;
		// Set the current layer to pLayer.
		currentLayer = pLayer;
	}
	/**
	 * Hide the layer
	 */
	public void hideLayer() {
		if (isLayerShown) {
			// Clear the HUD.
			engine.getCamera().getHUD().clearChildScene();
			// If we had to use a place-holder scene, clear it.
			if (currentScene.hasChildScene()) {
				if (currentScene.getChildScene() == placeholderModalScene) {
					currentScene.clearChildScene();
				}
			}
			// If the camera did not have a HUD before we showed the layer,
			// remove the place-holder HUD.
			if (!cameraHadHud) {
				engine.getCamera().setHUD(null);
			}
			isLayerShown = false;
			currentLayer = null;
		}
	}
}
