package org.escoladeltreball.universerescue.scenes;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.util.GLState;
import org.andengine.util.adt.color.Color;
import org.escoladeltreball.universerescue.managers.SceneManager.SceneType;

public class LoadingScene extends BaseScene {

	private Sprite loading;
	@Override
	public void createScene() {
		setBackground(new Background(Color.BLACK));
		loading = new Sprite(0, 0, manager.loading_region, vbom){
    		@Override
            protected void preDraw(GLState pGLState, Camera pCamera) 
    		{
                super.preDraw(pGLState, pCamera);
                pGLState.enableDither();
            }
    	};
    	loading.setScale(1.5f);
    	loading.setPosition(activity.getWidth()/2, activity.getHeight()/2);
    	attachChild(loading);
		
	}

	@Override
	public void onBackKeyPressed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_LOADING;
	}

	@Override
	public void disposeScene() {
		// TODO Auto-generated method stub
		
	}

}