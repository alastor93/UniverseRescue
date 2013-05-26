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
	public BuildableBitmapTextureAtlas level1Atlas, level2Atlas, level3Atlas;
	public ITextureRegion splash_region;
	public ITextureRegion loading_region;
	public Font defaultFont;

	/** ITextureRegion for load background on MainMenuScene */
	public ITextureRegion menu_background_region;
	/** ITextureRegion for load play option on MainMenuScene */
	public ITextureRegion play_region;
	/** ITextureRegion for load options option on MainMenuScene */
	public ITextureRegion options_region;
	/** ITextureRegion for load exit option on MainMenuScene */
	public ITextureRegion exit_region;
	/** ITextureRegion for load controls option on MainMenuScene */
	public ITextureRegion controls_region;
	/** ITextureRegion for load music option on MainMenuScene */
	public static TiledTextureRegion music_region;
	/** ITextureRegion for load controls on ControlLayer */
	public ITextureRegion controlinfo;

	/** TextureOption BILINEAR */
	private final TextureOptions BILINEAR = TextureOptions.BILINEAR;

	/** LevelButton's Font */
	public Font levelsFont;

	/** GameScene count enemies Font */
	public Font gameFont;
	/** Font for final Boss */
	public Font bossFont;

	/** TextureRegion for load image for level icon */
	public ITextureRegion menuLevelIcon;
	/** TextureRegion for load image for level locked */
	public ITextureRegion menuLevelLocked;
	/** TiledTextureRegion for load level's star */
	public ITiledTextureRegion menuLevelStar;
	/** TextureRegion for load backArrow image */
	public ITextureRegion backarrow;

	/** ITextureRegion for load game background */
	public ITextureRegion game_background;
	public ITextureRegion game_background2;

	// ITextureRegions for load layer game over
	public ITextureRegion gameOver;
	public ITextureRegion continueGame;
	public ITextureRegion exitGame;
	public ITextureRegion arrow;
	public ITextureRegion pause;
	public ITextureRegion introFinalBoss;
	// ITextureRegion for load the character sprite
	public TiledTextureRegion playerSprite;
	public TiledTextureRegion enemySprite;
	public TiledTextureRegion enemySprite2;
	public TiledTextureRegion flyEnemySprite;
	public TiledTextureRegion finalBoss;

	// Texture for load the platform sprite
	public ITextureRegion platformSprite;

	// Itexture for load the life of the player
	public ITextureRegion life;

	// Itexture for load the item
	public ITextureRegion item;
	public ITextureRegion itemArmour;

	// Itexture for load the stalactite
	public ITextureRegion stalactite;

	// ITextureRegion for load game controls
	public ITextureRegion controlBaseRegion;
	public ITextureRegion controlKnobRegion;
	public ITextureRegion buttonA;
	public ITextureRegion bulletSprite;
	public ITextureRegion flyEnemyBullet;
	public ITextureRegion win;

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
					TextureOptions.BILINEAR,
					Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 50f,
					true, Color.WHITE_ARGB_PACKED_INT);
			defaultFont.load();
		}
		if (levelsFont == null) {
			this.levelsFont = FontFactory.create(this.engine.getFontManager(),
					this.engine.getTextureManager(), 256, 256,
					TextureOptions.BILINEAR,
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
		if (bossFont == null) {
			this.bossFont = FontFactory.createFromAsset(
					engine.getFontManager(), engine.getTextureManager(), 254,
					254, getActivity().getAssets(), "fonts/ecliptic.ttf", 35f,
					true, Color.RED_ARGB_PACKED_INT);
			this.bossFont.load();
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
					activity.getTextureManager(), 1700, 1200,
					TextureOptions.BILINEAR);
		}
		if (menu_background_region == null) {
			menu_background_region = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(menuTextureAtlas, activity,
							"fondomenuPrin.jpg");
		}
		if (play_region == null) {
			play_region = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(menuTextureAtlas, activity, "play.png");
		}
		if (options_region == null) {
			options_region = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(menuTextureAtlas, activity, "options.png");
		}
		if (controls_region == null) {
			controls_region = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(menuTextureAtlas, activity, "controls.png");
		}
		if (exit_region == null) {
			exit_region = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(menuTextureAtlas, activity, "exit.png");
		}
		if (music_region == null) {
			music_region = BitmapTextureAtlasTextureRegionFactory
					.createTiledFromAsset(menuTextureAtlas, activity,
							"tiledsound.png", 2, 1);
		}
		if (controlinfo == null) {
			controlinfo = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(menuTextureAtlas, activity,
							"controlsinfo.png");
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

	/**
	 * Load Game's graphics
	 * 
	 */
	public void loadGameGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
		gameAtlas = new BuildableBitmapTextureAtlas(
				this.engine.getTextureManager(), 1317, 737, BILINEAR);
		if (this.playerSprite == null) {
			this.playerSprite = BitmapTextureAtlasTextureRegionFactory
					.createTiledFromAsset(gameAtlas, activity, "player.png", 6,
							3);
		}
		if (this.bulletSprite == null) {
			this.bulletSprite = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(gameAtlas, activity, "bullet.png");
		}
		if (this.life == null) {
			this.life = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
					gameAtlas, activity, "vida.png");
		}
		if (this.item == null) {
			this.item = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
					gameAtlas, activity, "item.png");
		}
		if (this.itemArmour == null) {
			this.itemArmour = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(gameAtlas, activity, "armour.png");
		}
		if (this.gameOver == null) {
			this.gameOver = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(gameAtlas, activity, "gameOver.png");
		}
		if (this.continueGame == null) {
			this.continueGame = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(gameAtlas, activity, "continue.png");
		}
		if (this.exitGame == null) {
			this.exitGame = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(gameAtlas, activity, "exit.png");
		}
		if (this.arrow == null) {
			this.arrow = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(gameAtlas, activity, "arrow.png");
		}
		if (this.pause == null) {
			this.pause = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(gameAtlas, activity, "pause.png");
		}
		if (this.win == null) {
			this.win = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(gameAtlas, activity, "win.png");
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

	public void loadLevel1Graphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
		level1Atlas = new BuildableBitmapTextureAtlas(
				this.engine.getTextureManager(), 1810, 510, BILINEAR);
		if (this.game_background == null) {
			this.game_background = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(level1Atlas, activity,
							"gameBackground.png");
		}
		if (this.enemySprite == null) {
			this.enemySprite = BitmapTextureAtlasTextureRegionFactory
					.createTiledFromAsset(level1Atlas, activity, "enemy.png",
							4, 3);
		}
		if (this.flyEnemySprite == null) {
			this.flyEnemySprite = BitmapTextureAtlasTextureRegionFactory
					.createTiledFromAsset(level1Atlas, activity,
							"flyEnemy.png", 5, 2);
		}
		if (this.flyEnemyBullet == null) {
			this.flyEnemyBullet = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(level1Atlas, activity,
							"flyEnemyAttack.png");
		}
		if (this.platformSprite == null) {
			this.platformSprite = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(level1Atlas, activity, "platform.png");
		}
		try {
			this.level1Atlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							1, 1, 1));
			this.level1Atlas.load();
		} catch (final TextureAtlasBuilderException e) {
			Debug.e(e);
		}
	}

	public void loadLevelSelectorGraphics() {
		BitmapTextureAtlasTextureRegionFactory
				.setAssetBasePath("gfx/levelSelector/");
		BuildableBitmapTextureAtlas levelAtlas = new BuildableBitmapTextureAtlas(
				this.engine.getTextureManager(), 300, 160, BILINEAR);
		if (menuLevelIcon == null) {
			menuLevelIcon = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(levelAtlas, activity, "LevelIcon.png");
		}
		if (this.menuLevelLocked == null) {
			this.menuLevelLocked = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(levelAtlas, activity, "Lock.png");
		}
		if (this.menuLevelStar == null) {
			menuLevelStar = BitmapTextureAtlasTextureRegionFactory
					.createTiledFromAsset(levelAtlas, activity, "Stars.png", 4,
							1);
		}
		if (this.backarrow == null) {
			this.backarrow = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(levelAtlas, activity, "arrowback.png");
		}
		try {
			levelAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							1, 1, 1));
			levelAtlas.load();
		} catch (final TextureAtlasBuilderException e) {
			Debug.e(e);
		}
	}

	public void loadLevel2Graphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
		level2Atlas = new BuildableBitmapTextureAtlas(
				this.engine.getTextureManager(), 1238, 611, BILINEAR);
		if (this.stalactite == null) {
			this.stalactite = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(level2Atlas, activity, "stalactite.png");
		}
		if (this.game_background2 == null) {
			this.game_background2 = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(level2Atlas, activity,
							"gameBackground2.png");
		}
		if (this.enemySprite2 == null) {
			this.enemySprite2 = BitmapTextureAtlasTextureRegionFactory
					.createTiledFromAsset(level2Atlas, activity, "enemy2.png",
							4, 2);
		}
		try {
			this.level2Atlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							1, 1, 1));
			this.level2Atlas.load();
		} catch (final TextureAtlasBuilderException e) {
			Debug.e(e);
		}
	}

	public void loadLevel3Graphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
		level3Atlas = new BuildableBitmapTextureAtlas(
				this.engine.getTextureManager(), 1600, 1000, BILINEAR);
		if (this.game_background2 == null) {
			this.game_background2 = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(level3Atlas, activity,
							"gameBackground2.png");
		}
		if (this.finalBoss == null) {
			this.finalBoss = BitmapTextureAtlasTextureRegionFactory
					.createTiledFromAsset(level3Atlas, activity,
							"finalboss.png", 4, 3);
		}
		if (this.platformSprite == null) {
			this.platformSprite = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(level3Atlas, activity, "platform.png");
		}
		if (introFinalBoss == null) {
			this.introFinalBoss = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(level3Atlas, activity,
							"finalbossopening.png");
		}

		try {
			this.level3Atlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							1, 1, 1));
			this.level3Atlas.load();
		} catch (final TextureAtlasBuilderException e) {
			Debug.e(e);
		}

	}

	public void loadControls() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
		BuildableBitmapTextureAtlas controlAtlas = new BuildableBitmapTextureAtlas(
				this.engine.getTextureManager(), 270, 210, BILINEAR);
		if (this.controlBaseRegion == null) {
			this.controlBaseRegion = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(controlAtlas, activity,
							"onscreen_control_base.png");
		}
		if (this.controlKnobRegion == null) {
			this.controlKnobRegion = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(controlAtlas, activity,
							"onscreen_control_knob.png");
		}
		if (this.buttonA == null) {
			this.buttonA = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(controlAtlas, activity, "Button_A.png");
		}
		try {
			controlAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
							1, 1, 1));
			controlAtlas.load();
		} catch (final TextureAtlasBuilderException e) {
			Debug.e(e);
		}
	}

	public static GameActivity getActivity() {
		return getInstance().activity;
	}
}
