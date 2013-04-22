package org.escoladeltreball.universerescue.managers;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.escoladeltreball.universerescue.GameActivity;

public class ResourcesManager {
	
	
	//Attributes
	private static  ResourcesManager obj = null;
	public GameActivity activity;
	public Camera camera;
	public Engine engine;
	public VertexBufferObjectManager vbom;
	public BitmapTextureAtlas splashTextureAtlas;
	public BuildableBitmapTextureAtlas menuTextureAtlas;
	public ITextureRegion splash_region;
	public ITextureRegion loading_region;
	public ITextureRegion menu_background_region;
	
	
	//Singleton
	
	public static ResourcesManager getInstance(){
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
	public static void setup(Engine engine, Camera camera, GameActivity activity, VertexBufferObjectManager vbom){
    	getInstance().engine = engine;
    	getInstance().camera = camera;
    	getInstance().activity = activity;
    	getInstance().vbom = vbom;
	}
	
	public void loadSplashScreen(){
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		splashTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 200, 200,TextureOptions.BILINEAR);
		splash_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTextureAtlas, activity, "logoUR.png",0,0);
		splashTextureAtlas.load();
	}
	
	
	public void unloadSplashScreen(){
		splashTextureAtlas.unload();
		splash_region = null;
	}
	
	
	
}
