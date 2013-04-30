package org.escoladeltreball.universerescue.managers;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;
import org.andengine.util.debug.Debug;
import org.escoladeltreball.universerescue.GameActivity;

import android.graphics.Typeface;

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
	public Font defaultFont;

	/** ITextureRegion for load background on MainMenuScene */
	public ITextureRegion menu_background_region = null;
	/** ITextureRegion for load play option on MainMenuScene */
	public ITextureRegion play_region = null;
	/** ITextureRegion for load options option on MainMenuScene */
	public ITextureRegion options_region = null;
	/** ITextureRegion for load exit option on MainMenuScene */
	public ITextureRegion exit_region = null;
	
	/** TextureOption BILINEAR */
	private final TextureOptions BILINEAR = TextureOptions.BILINEAR;

	/** LevelButton's Font */
	public Font levelsFont = null;

	/** TextureRegion for load image for level icon */
	public ITextureRegion menuLevelIcon = null;
	/** TextureRegion for load image for level locked */
	public ITextureRegion menuLevelLocked = null;
	/** TiledTextureRegion for load level's star */
	public ITiledTextureRegion menuLevelStar = null;
	/** */
	public BitmapTextureAtlas levelSelectorAtlas = null;
	
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
	
	public void loadFonts(){
		if(defaultFont==null) {
			defaultFont = FontFactory.create(engine.getFontManager(), engine.getTextureManager(), 256, 256, Typeface.create(Typeface.DEFAULT, Typeface.BOLD),  50f, true, Color.WHITE_ARGB_PACKED_INT);
			defaultFont.load();
		}
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
	
	public void loadLevelSelectorGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
//		if (levelSelectorAtlas == null) {
//			this.levelSelectorAtlas = new BitmapTextureAtlas(
//					activity.getTextureManager(), 64, 64, BILINEAR);
//		}
		if (levelsFont == null) {
			this.levelsFont = FontFactory.create(this.engine.getFontManager(),
					this.engine.getTextureManager(), 256, 256,
					Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 32f,
					true, Color.WHITE_ARGB_PACKED_INT);
			levelsFont.load();
		}
		if (menuLevelIcon == null) {
			BitmapTextureAtlas levelIcon = new BitmapTextureAtlas(
					this.engine.getTextureManager(), 64, 64, BILINEAR);
			menuLevelIcon = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(levelIcon, activity, "LevelIcon.png",
							0, 0);
			levelIcon.load();
		}
		if (this.menuLevelLocked == null) {
			BitmapTextureAtlas levelLockedT = new BitmapTextureAtlas(
					this.engine.getTextureManager(), 64, 64, BILINEAR);
			this.menuLevelLocked = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(levelLockedT, activity, "Lock.png",
							0, 0);
			levelLockedT.load();
		}
//		if (this.menuLevelStar == null) {
////			BitmapTextureAtlas levelStars = new BitmapTextureAtlas(
////					this.engine.getTextureManager(), 64, 64, BILINEAR);
//			menuLevelStar = BitmapTextureAtlasTextureRegionFactory
//					.createTiledFromAsset(levelSelectorAtlas, getActivity(),
//							"Stars.png", 0, 0, 4, 1);
////			levelStars.load();
//		}
//		
	}

}
