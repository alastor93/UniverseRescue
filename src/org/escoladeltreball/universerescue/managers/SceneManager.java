package org.escoladeltreball.universerescue.managers;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.ui.IGameInterface.OnCreateSceneCallback;
import org.escoladeltreball.universerescue.layers.GameOverLayer;
import org.escoladeltreball.universerescue.layers.Layer;
import org.escoladeltreball.universerescue.layers.OptionsLayer;
import org.escoladeltreball.universerescue.levels.LevelSelector;
import org.escoladeltreball.universerescue.levels.level1;
import org.escoladeltreball.universerescue.levels.level2;
import org.escoladeltreball.universerescue.levels.level3;
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
		SCENE_SPLASH, SCENE_MAINMENU, SCENE_LOADING, SCENE_GAME,SCENE_LEVEL;
	}

	public void createSplashScene(OnCreateSceneCallback pOnCreateSceneCallback) {
		ResourcesManager.getInstance().loadSplashScreen();
		splashScene = new SplashScene();
		currentScene = splashScene;
		pOnCreateSceneCallback.onCreateSceneFinished(splashScene);
	}

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

	public void createLevelScene() {
		ResourcesManager.getInstance().loadLevelSelectorGraphics();
		this.levelScene = new LevelSelector(100, 100,
				ResourcesManager.getInstance().menuLevelIcon,
				ResourcesManager.getInstance().levelsFont);
		setScene(this.levelScene);
		mainMenu.detachSelf();
		// mainMenu.dispose();
		// Build a back button
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
		this.levelScene.attachChild(backToMenuButton);
		this.levelScene.registerTouchArea(backToMenuButton);
		backToMenuButton.setScale(1.5f);
		backToMenuButton.setOffsetCenter(0, 0);
	}
	
	public void unloadLevelScene() {
		levelScene.detachSelf();
		levelScene.dispose();
	}
	
	public void backToMenu() {
		mainMenu = new MainMenuScene();
		setScene(mainMenu);
		this.unloadLevelScene();
	}
	
	public void backToLevelMenu() {
		this.createLevelScene();
		gameScene.disposeScene();
	}

	public void createTempGameScene(final Engine engine,final int level) {
		ResourcesManager.getInstance().loadLoadingScreen();
		this.loadingScene = new LoadingScene();
		this.setScene(loadingScene);
		engine.registerUpdateHandler(new TimerHandler(0.1f,
				new ITimerCallback() {
					public void onTimePassed(final TimerHandler pTimerHandler) {
						engine.unregisterUpdateHandler(pTimerHandler);
						loadingScene.disposeScene();
						ResourcesManager.getInstance().loadGameGraphics();
						if (level == 1) {
							ResourcesManager.getInstance().loadLevel1Graphics();
							gameScene = new level1();
						}else if (level == 2) {
							ResourcesManager.getInstance().loadLevel2Graphics();
							gameScene = new level2();
						} else {
							gameScene = new level3();
						}

						setScene(gameScene);
					}
				}));
	}
	
	public void unloadGameScene() {
		gameScene.detachSelf();
		gameScene.disposeScene();
	}

	public void setScene(BaseScene scene) {
		engine.setScene(scene);
		this.currentScene = scene;
		this.currentSceneType = scene.getSceneType();
	}

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

	public SceneType getCurrentSceneType() {
		return currentSceneType;
	}

	public BaseScene getCurrentScene() {
		return currentScene;
	}

	public void showOptionsLayer(final boolean pSuspendCurrentSceneUpdates) {
		showLayer(OptionsLayer.getInstance(), false,
				pSuspendCurrentSceneUpdates, true);
	}
	
	public void showLoseLayer(final boolean pSuspendCurrentSceneUpdates) {
		showLayer(GameOverLayer.getInstance(), false,
				pSuspendCurrentSceneUpdates, true);
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
		// Reflect that a layer is shown.
		isLayerShown = true;
		// Set the current layer to pLayer.
		currentLayer = pLayer;
	}

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
