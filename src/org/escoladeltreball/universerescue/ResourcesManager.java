package org.escoladeltreball.universerescue;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class ResourcesManager {
	
	
	//Attributes
	private static  ResourcesManager obj = null;
	private GameActivity activity;
	private Camera camera;
	private Engine engine;
	private VertexBufferObjectManager vbom;
	private BitmapTextureAtlas splashTextureAtlas;
	private BuildableBitmapTextureAtlas menuTextureAtlas;
	private ITextureRegion splash_region;
	private ITextureRegion menu_background_region;
	
	
	//Singleton
	
	public ResourcesManager getInstance(){
		if(obj == null){
			obj = new ResourcesManager();
		}
		return obj;
	}
	
	//Methods
	
	/**
	 * 
	 * @param engine
	 * @param camera
	 * @param activity
	 * @param vbom
	 * 
	 */
	public void prepareManager(Engine engine, Camera camera, GameActivity activity, VertexBufferObjectManager vbom){
    	getInstance().engine = engine;
    	getInstance().camera = camera;
    	getInstance().activity = activity;
    	getInstance().vbom = vbom;
	}
	
	
	
}
