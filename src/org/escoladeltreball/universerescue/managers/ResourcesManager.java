package org.escoladeltreball.universerescue.managers;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.AssetBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;
import org.escoladeltreball.universerescue.GameActivity;

public class ResourcesManager {

	// Attributes
	private static ResourcesManager obj = null;
	public GameActivity activity;
	public Camera camera;
	public Engine engine;
	public VertexBufferObjectManager vbom;
	public BitmapTextureAtlas splashTextureAtlas;
	public BitmapTextureAtlas loadingTextureAtlas;
	public BitmapTextureAtlas optionsTextureAtlas;
	public BuildableBitmapTextureAtlas menuTextureAtlas;
	public ITextureRegion splash_region;
	public ITextureRegion loading_region;

	/** ITextureRegion for load background on MainMenuScene */
	public ITextureRegion menu_background_region = null;
	/** ITextureRegion for load play option on MainMenuScene */
	public ITextureRegion play_region = null;
	/** ITextureRegion for load options option on MainMenuScene */
	public ITextureRegion options_region = null;
	/** ITextureRegion for load exit option on MainMenuScene */
	public ITextureRegion exit_region = null;

	// Singleton

	public static ResourcesManager getInstance() {
		if (obj == null) {
			obj = new ResourcesManager();
		}
		return obj;
	}

	public static GameActivity getActivity() {
		return getInstance().activity;
	}

	// Methods

	/**
	 * 
	 * @param engine
	 * @param camera
	 * @param activity
	 * @param vbom
	 * 
	 */
	public static void setup(Engine engine, Camera camera,
			GameActivity activity, VertexBufferObjectManager vbom) {
		getInstance().engine = engine;
		getInstance().camera = camera;
		getInstance().activity = activity;
		getInstance().vbom = vbom;
	}

	public void loadSplashScreen() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		splashTextureAtlas = new BitmapTextureAtlas(
				activity.getTextureManager(), 200, 200, TextureOptions.BILINEAR);
		splash_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				splashTextureAtlas, activity, "logoUR.png", 0, 0);
		splashTextureAtlas.load();
	}

	public void loadLoadingScreen() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		loadingTextureAtlas = new BitmapTextureAtlas(
				activity.getTextureManager(), 200, 100, TextureOptions.BILINEAR);
		loading_region = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(loadingTextureAtlas, activity, "loading.png",
						0, 0);
		loadingTextureAtlas.load();
	}

	public void unloadLoadingScreen() {
		loadingTextureAtlas.unload();
		loading_region = null;
	}

	public void unloadSplashScreen() {
		splashTextureAtlas.unload();
		splash_region = null;
	}

	public void loadOptionsLayer(){
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		optionsTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(),512 , 256, TextureOptions.BILINEAR);
		options_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(optionsTextureAtlas, activity, "LevelLayerBG.png",0,0);
		optionsTextureAtlas.load();
	}

	/**
	 * Load the Menu's graphics
	 */

	public void loadMenuGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		if (menuTextureAtlas == null) {
			menuTextureAtlas = new BuildableBitmapTextureAtlas(
					activity.getTextureManager(), 1200, 1200,
					TextureOptions.BILINEAR);
		}
		if (menu_background_region == null) {
			menu_background_region = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(menuTextureAtlas, activity,
							"fondomenuPrin.jpg");
		}
		if (play_region == null) {
			play_region = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(menuTextureAtlas, activity, "newgame.png");
		}
		if (options_region == null) {
			options_region = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(menuTextureAtlas, activity, "opciones.png");
		}
		if (exit_region == null) {
			exit_region = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(menuTextureAtlas, activity, "salir.png");
		}
		try {
			this.menuTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							0, 0, 0));
			this.menuTextureAtlas.load();
		} catch (final TextureAtlasBuilderException e) {
			Debug.e(e);
		}
	}

}
