package org.escoladeltreball.universerescue;

import java.io.IOException;

import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;
import org.escoladeltreball.universerescue.managers.ResourcesManager;
import org.escoladeltreball.universerescue.managers.SFXManager;
import org.escoladeltreball.universerescue.managers.SceneManager;

import android.app.AlertDialog;
import android.content.DialogInterface;

public class GameActivity extends BaseGameActivity {

	private Camera camera;
	private ResourcesManager manager;
	private static final int WIDTH = 800;
	private static final int HEIGHT = 480;
	public static final String SHARED_PREFS_MUSIC_MUTED = "mute.music";
	public static final String SHARED_PREFS_MAIN = "UniverseRunnerSettings";
	public static final String SHARED_PREFS_LEVEL_MAX_REACHED = "levels.reached.";
	public static final String SHARED_PREFS_LEVEL_STARS = "level.stars.";

	@Override
	public Engine onCreateEngine(EngineOptions pEngineOptions) {
		return new LimitedFPSEngine(pEngineOptions, 60);
	}

	@Override
	public EngineOptions onCreateEngineOptions() {
		this.camera = new Camera(0, 0, WIDTH, HEIGHT);
		EngineOptions engineOptions = new EngineOptions(true,
				ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(),
				camera);
		engineOptions.getAudioOptions().setNeedsMusic(true).setNeedsSound(true);
		engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
		return engineOptions;
	}

	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws IOException {
		ResourcesManager.setup(mEngine, camera, this,
				getVertexBufferObjectManager());
		manager = ResourcesManager.getInstance();
		pOnCreateResourcesCallback.onCreateResourcesFinished();

	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
			throws IOException {
		SceneManager.getInstance().createSplashScene(pOnCreateSceneCallback);
		// Start the background music.
		SFXManager.playMusic();
		// If the music is muted in the settings, mute it in the game.
		if (getIntFromSharedPreferences(SHARED_PREFS_MUSIC_MUTED) > 0)
			SFXManager.setMusicMuted(true);
	}

	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback)
			throws IOException {
		mEngine.registerUpdateHandler(new TimerHandler(2f,
				new ITimerCallback() {

					@Override
					public void onTimePassed(TimerHandler pTimerHandler) {
						mEngine.unregisterUpdateHandler(pTimerHandler);
						SceneManager.getInstance().createMenuScene();
					}
				}));
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}

	public static int writeIntToSharedPreferences(final String pStr,
			final int pValue) {
		ResourcesManager.getInstance().activity
				.getSharedPreferences(SHARED_PREFS_MAIN, 0).edit()
				.putInt(pStr, pValue).apply();
		return pValue;
	}

	public static int getIntFromSharedPreferences(final String pStr) {
		return ResourcesManager.getInstance().activity.getSharedPreferences(
				SHARED_PREFS_MAIN, 0).getInt(pStr, 0);
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (this.isGameLoaded()) {
			SFXManager.pauseMusic();
		}
	}

	 @Override
	 protected void onDestroy() {
	 super.onDestroy();
	 }

	@Override
	protected synchronized void onResume() {
		super.onResume();
		System.gc();
		if (this.isGameLoaded()) {
			SFXManager.resumeMusic();
		}
	}

	@Override
	public void onBackPressed() {
		if (ResourcesManager.getInstance().engine != null) {
			if (SceneManager.getInstance().isLayerShown) {
				SceneManager.getInstance().currentLayer.onHideLayer();
			} else if (SceneManager.getInstance().getCurrentScene()
					.equals("GameLevel")) {
			} else {
				ResourcesManager.getInstance().showMessageExit();
			}
		}
	}

	public static int getWidth() {
		return WIDTH;
	}

	public static int getHeight() {
		return HEIGHT;
	}

	public static int getLevelStars(int maxLevelNumber) {
		return getIntFromSharedPreferences(SHARED_PREFS_LEVEL_STARS
				+ String.valueOf(maxLevelNumber));
	}
}
