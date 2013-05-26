package org.escoladeltreball.universerescue.scenes;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.util.GLState;
import org.escoladeltreball.universerescue.GameActivity;
import org.escoladeltreball.universerescue.managers.SceneManager.SceneType;

public class SplashScene extends BaseScene {
	//Sprite with the img of the splash menu
	private Sprite splash;

	@Override
	public void createScene() {
		//Create the Splash sprite, set scale and the position
		splash = new Sprite(0, 0, manager.splash_region, vbom) {
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) {
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}
		};
		splash.setScale(1.5f);
		splash.setPosition(GameActivity.getWidth()/2, GameActivity.getHeight()/2);
		attachChild(splash);
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_SPLASH;

	}

	@Override
	public void disposeScene() {
		splash.detachSelf();
		splash.dispose();
		this.detachSelf();
		this.dispose();

	}

}
