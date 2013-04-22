package org.escoladeltreball.universerescue;

import org.andengine.engine.Engine;
import org.andengine.ui.IGameInterface.OnCreateSceneCallback;



public class SceneManager {
	//Attributes
	private BaseScene splashScene;
	private static SceneManager obj = null;
	private Engine engine = ResourcesManager.getInstance().engine;
	private SceneType currentSceneType = SceneType.SCENE_SPLASH;
	private BaseScene currentScene;
	
	//Constructor
	public SceneManager() {
	}
	
	//Singleton
	public static SceneManager getInstance(){
		if(obj == null){
			obj = new SceneManager();
		}
		return obj;
	}
	
	//Methods
	public enum SceneType{
		SCENE_SPLASH;
	}
	
	public void createMenuScene(){
		
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
	
	public void setScene(BaseScene scene) {
		engine.setScene(scene);
		this.currentScene = scene;
		this.currentSceneType = scene.getSceneType();
	}
	
	public  void setScene(SceneType sceneType){
		switch(sceneType){
		case SCENE_SPLASH:
			setScene(splashScene);
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
