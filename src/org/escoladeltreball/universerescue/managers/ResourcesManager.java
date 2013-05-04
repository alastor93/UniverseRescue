package org.escoladeltreball.universerescue.managers;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.AssetBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;
import org.andengine.util.debug.Debug;
import org.escoladeltreball.universerescue.GameActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.text.Html;

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
	
	public void loadFonts(){
		if (defaultFont == null) {
			defaultFont = FontFactory.create(engine.getFontManager(), engine.getTextureManager()
					, 256, 256, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 50f,
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
			this.gameFont = FontFactory.createFromAsset(engine.getFontManager(),
					engine.getTextureManager(),254, 254, getActivity().getAssets(), "fonts/ecliptic.ttf", 48f, true, Color.WHITE_ARGB_PACKED_INT);
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
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/mainMenu/");
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
			music_region = getLimitableTTR("tiledsoun.png",2,1,TextureOptions.BILINEAR);
					
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
	
	public void unloadMenuTextures() {
		this.menuTextureAtlas.unload();
	}
	
	public void loadLevelSelectorGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/levelSelector/");
		if (menuLevelIcon == null) {
			BitmapTextureAtlas levelIcon = new BitmapTextureAtlas(
					this.engine.getTextureManager(), 150, 150, BILINEAR);
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
		if (this.menuLevelStar == null) {
			BitmapTextureAtlas levelStars = new BitmapTextureAtlas(
					this.engine.getTextureManager(), 64, 64, BILINEAR);
			menuLevelStar = BitmapTextureAtlasTextureRegionFactory
					.createTiledFromAsset(levelStars, activity,
							"Stars.png", 0, 0, 4, 1);
			levelStars.load();
		}
		if (this.backarrow == null) {
			BitmapTextureAtlas arrow = new BitmapTextureAtlas(
					this.engine.getTextureManager(), 50, 50, BILINEAR);
			this.backarrow = BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(arrow, activity, "arrowback.png", 0, 0);
			arrow.load();
		}
	}
	
	/**
	 * Load Game's graphics
	 * 
	 */
	public void loadGameGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/game/");
		if (this.game_background == null) {
			BitmapTextureAtlas background = new BitmapTextureAtlas(
					this.engine.getTextureManager(), 1000, 500, BILINEAR);
			this.game_background =  BitmapTextureAtlasTextureRegionFactory
					.createFromAsset(background, activity, "gameBackground.png",
							0, 0);
			background.load();
		}
		if (this.playerSprite == null) {
			BuildableBitmapTextureAtlas player = new BuildableBitmapTextureAtlas(
					engine.getTextureManager(), 292, 40,
					TextureOptions.BILINEAR);
			this.playerSprite = BitmapTextureAtlasTextureRegionFactory
					.createTiledFromAsset(player, activity, "player.png", 9, 1);
			try {
				player
						.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
								0, 0, 0));
				player.load();
			} catch (final TextureAtlasBuilderException e) {
				Debug.e(e);
			}
		}
	}
	
	private TiledTextureRegion getLimitableTTR(String pTiledTextureRegionPath, int pColumns, int pRows, TextureOptions pTextureOptions) {
		final IBitmapTextureAtlasSource bitmapTextureAtlasSource = AssetBitmapTextureAtlasSource.create(activity.getAssets(), BitmapTextureAtlasTextureRegionFactory.getAssetBasePath() + pTiledTextureRegionPath);
		final BitmapTextureAtlas bitmapTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), bitmapTextureAtlasSource.getTextureWidth(), bitmapTextureAtlasSource.getTextureHeight(), pTextureOptions);
		final ITextureRegion[] textureRegions = new ITextureRegion[pColumns * pRows];

		final int tileWidth = bitmapTextureAtlas.getWidth() / pColumns;
		final int tileHeight = bitmapTextureAtlas.getHeight() / pRows;

		for(int tileColumn = 0; tileColumn < pColumns; tileColumn++) {
			for(int tileRow = 0; tileRow < pRows; tileRow++) {
				final int tileIndex = tileRow * pColumns + tileColumn;

				final int x = tileColumn * tileWidth;
				final int y = tileRow * tileHeight;
				textureRegions[tileIndex] = new TextureRegion(bitmapTextureAtlas, x, y, tileWidth, tileHeight, false) {
					@Override
					public void updateUV() {
							this.mU = this.getTextureX() / bitmapTextureAtlas.getWidth();
							this.mU2 = (this.getTextureX() + tileWidth) / bitmapTextureAtlas.getWidth();

							this.mV = this.getTextureY() / bitmapTextureAtlas.getHeight();
							this.mV2 = (this.getTextureY() + tileHeight) / bitmapTextureAtlas.getHeight();
						
					}
				};
					textureRegions[tileIndex].setTextureSize(textureRegions[tileIndex].getWidth()*2f, textureRegions[tileIndex].getHeight()*2f);
			}
		}

		final TiledTextureRegion tiledTextureRegion = new TiledTextureRegion(bitmapTextureAtlas, false, textureRegions);
		bitmapTextureAtlas.addTextureAtlasSource(bitmapTextureAtlasSource, 0, 0);
		bitmapTextureAtlas.load();
		return tiledTextureRegion;
	}
	
	public void showMessageExit(){
		this.getActivity().runOnUiThread(new Runnable() {
			
			
			@Override
			public void run() {
				final AlertDialog.Builder builder = new AlertDialog.Builder(ResourcesManager.getActivity()).setTitle("Universe Rescue").setMessage(Html.fromHtml("Estas seguro que desea salir?")).setPositiveButton("Si", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(final DialogInterface dialog, final int id) {
									System.exit(0);

								}
							}).setNegativeButton("No", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(final DialogInterface dialog, final int id) {
								}
							});
				
				final AlertDialog alert = builder.create();
				alert.show();
			}
		});
	}
	public static GameActivity getActivity() {
		return getInstance().activity;
	}
}
