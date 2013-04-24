package org.escoladeltreball.universerescue.managers;

import org.andengine.engine.Engine;
import org.andengine.ui.IGameInterface.OnCreateSceneCallback;
import org.escoladeltreball.universerescue.scenes.BaseScene;
import org.escoladeltreball.universerescue.scenes.LoadingScene;
import org.escoladeltreball.universerescue.scenes.MainMenuScene;
import org.escoladeltreball.universerescue.scenes.SplashScene;

public class SceneManager {
	// Attributes
	private BaseScene splashScene;
	private BaseScene loadingScene;
	private static SceneManager obj = null;
	private Engine engine = ResourcesManager.getInstance().engine;
	private SceneType currentSceneType = SceneType.SCENE_SPLASH;
	private BaseScene currentScene;

	/** BaseScene of MainMenu */
	private BaseScene mainMenu;

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
		SCENE_SPLASH, SCENE_MAINMENU, SCENE_LOADING;
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
		//loadingScene = new LoadingScene();
		setScene(mainMenu);
		disposeSplashScene();
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

}
