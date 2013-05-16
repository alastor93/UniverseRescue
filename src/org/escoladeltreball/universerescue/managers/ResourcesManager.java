package org.escoladeltreball.universerescue.managers;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
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
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;
import org.andengine.util.debug.Debug;
import org.escoladeltreball.universerescue.GameActivity;

import android.graphics.Typeface;

public class ResourcesManager {

	// Attributes
	private static ResourcesManager obj = null;
	public GameActivity activity;
	public BoundCamera camera;
	public Engine engine;
	public VertexBufferObjectManager vbom;
	public BitmapTextureAtlas splashTextureAtlas;
	public BitmapTextureAtlas loadingTextureAtlas;
	public BitmapTextureAtlas optionsTextureAtlas;
	public BuildableBitmapTextureAtlas menuTextureAtlas;
	public BuildableBitmapTextureAtlas gameAtlas;
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
	/** ITextureRegion for load music option on MainMenuScene */
	public static TiledTextureRegion music_region = null;

	/** TextureOption BILINEAR */
	private final TextureOptions BILINEAR = TextureOptions.BILINEAR;

	/** LevelButton's Font */
	public Font levelsFont = null;

	/** GameScene count enemies Font */
	public Font gameFont = null;

	/** TextureRegion for load image for level icon */
	public ITextureRegion menuLevelIcon = null;
	/** TextureRegion for load image for level locked */
	public ITextureRegion menuLevelLocked = null;
	/** TiledTextureRegion for load level's star */
	public ITiledTextureRegion menuLevelStar = null;
	/** TextureRegion for load backArrow image */
	public ITextureRegion backarrow = null;

	/** ITextureRegion for load game background */
	public ITextureRegion game_background = null;
	// ITextureRegion for load the character sprite
	public TiledTextureRegion playerSprite;
	public TiledTextureRegion enemySprite;
	
	
	// Texture for load the platform sprite
	public TiledTextureRegion platformSprite;
	
	//Itexture for load the life of the player
	public ITextureRegion life = null;
	
	//Itexture for load the item
	public ITextureRegion item = null;


	// ITextureRegion for load game controls
	public ITextureRegion controlBaseRegion;
	public ITextureRegion controlKnobRegion;
	public ITextureRegion buttonA;
	public ITextureRegion bulletSprite;
	public ITextureRegion flyEnemyBullet;

	// Singleton
	public static ResourcesManager getInstance() {
		if (obj == null) {
			obj = new ResourcesManager();
		}
		return obj;
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
	public static void setup(Engine engine, BoundCamera camera,
			GameActivity activity, VertexBufferObjectManager vbom) {
		getInstance().engine = engine;
		getInstance().camera = camera;
		getInstance().activity = activity;
		getInstance().vbom = vbom;
	}

	public void loadFonts() {
		if (defaultFont == null) {
			defaultFont = FontFactory.create(engine.getFontManager(),
					engine.getTextureManager(), 256, 256,
					Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 50f,
					true, Color.WHITE_ARGB_PACKED_INT);
			defaultFont.load();
		}
		if (levelsFont == null) {
			this.levelsFont = FontFactory.create(this.engine.getFontManager(),
					this.engine.getTextureManager(), 256, 256,
					Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 32f,
					true, Color.WHITE_ARGB_PACKED_INT);
			levelsFont.load();
		}
		if (gameFont == null) {
			this.gameFont = FontFactory.createFromAsset(
					engine.getFontManager(), engine.getTextureManager(), 254,
					254, getActivity().getAssets(), "fonts/ecliptic.ttf", 48f,
					true, Color.WHITE_ARGB_PACKED_INT);
			this.gameFont.load();
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
		BitmapTextureAtlasTextureRegionFactory
				.setAssetBasePath("gfx/mainMenu/");
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
		if (music_region == null) {
			music_region = BitmapTextureAtlasTextureRegionFactory
					.createTiledFromAsset(menuTextureAtlas, activity, "tiledsound.png", 2, 1);
		}
		try {
			this.menuTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							1, 1, 1));
			this.menuTextureAtlas.load();
		} catch (final TextureAtlasBuilderException e) {
			Debug.e(e);
		}
	}

	public void unloadMenuTextures() {
		this.menuTextureAtlas.unload();
	}

	public void loadLevelSelectorGraphics() {
		BitmapTextureAtlasTextureRegionFactory
				.setAssetBasePath("gfx/levelSelector/");
		BitmapTextureAtlas levelAtlas = new BitmapTextureAtlas(
				this.engine.getTextureManager(), 300, 160, BILINEAR);
		if (menuLevelIcon == null) {	
			menuLevelIcon = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(levelAtlas, activity, "LevelIcon.png", 10, 10);
		}
		if (this.menuLevelLocked == null) {
			this.menuLevelLocked = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(levelAtlas, activity, "Lock.png", 170, 10);
		}
		if (this.menuLevelStar == null) {
			menuLevelStar = BitmapTextureAtlasTextureRegionFactory
					.createTiledFromAsset(levelAtlas, activity, "Stars.png", 170,70
							, 4, 1);
		}
		if (this.backarrow == null) {
			this.backarrow = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(levelAtlas, activity, "arrowback.png", 240, 10);
		}
		levelAtlas.load();
	}

	/**
	 * Load Game's graphics
	 * 
	 */
	public void loadGameGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
		gameAtlas = new BuildableBitmapTextureAtlas(
				this.engine.getTextureManager(), 2800, 630, BILINEAR);
		if (this.game_background == null) {
			this.game_background = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(gameAtlas, activity,
							"gameBackground.png");
		}
		if (this.playerSprite == null) {
			this.playerSprite = BitmapTextureAtlasTextureRegionFactory
					.createTiledFromAsset(gameAtlas, activity, "player.png", 6, 2);
		}
		if (this.bulletSprite == null) {
			this.bulletSprite = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(gameAtlas, activity, "bullet.png");
		}
		if (this.enemySprite == null){
			this.enemySprite = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameAtlas, activity, "enemy.png", 6, 2);
		}
		if (this.flyEnemyBullet == null) {
			this.flyEnemyBullet = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(gameAtlas, activity, "flyEnemyAttack.png");
		}
		if (this.platformSprite == null){
			this.platformSprite = BitmapTextureAtlasTextureRegionFactory
					.createTiledFromAsset(gameAtlas, activity, "platform.png",4, 1);
		}
		if (this.life == null){
			this.life = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameAtlas, activity, "vida.png", 1, 1);
		}
		if (this.item == null){
			this.item = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameAtlas, activity, "item.png", 1, 1);
		}
		try {
			this.gameAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							1, 1, 1));
			this.gameAtlas.load();
		} catch (final TextureAtlasBuilderException e) {
			Debug.e(e);
		}
	}

	public void loadControls() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
		BitmapTextureAtlas controlAtlas = new BitmapTextureAtlas(
				this.engine.getTextureManager(), 270, 210, BILINEAR);
		if (this.controlBaseRegion == null) {
			this.controlBaseRegion = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(controlAtlas, activity,
							"onscreen_control_base.png", 10, 10);
		}
		if (this.controlKnobRegion == null) {
			this.controlKnobRegion = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(controlAtlas, activity,
							"onscreen_control_knob.png", 140, 10);
		}
		if (this.buttonA == null) {
			this.buttonA = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(controlAtlas, activity,
							"Button_A.png", 140, 80);
		}
		controlAtlas.load();
	}
	
	public static GameActivity getActivity() {
		return getInstance().activity;
	}
}
