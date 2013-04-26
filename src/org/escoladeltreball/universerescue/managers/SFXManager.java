package org.escoladeltreball.universerescue.managers;

import java.io.IOException;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.util.debug.Debug;
import org.escoladeltreball.universerescue.GameActivity;

public class SFXManager {
	
	private static SFXManager obj = null;
	private static Music music;
	public boolean soundsMuted;
	public boolean musicMuted;
	
	public static SFXManager getInstance() {
		if (obj == null) {
			obj = new SFXManager();
		}
		return obj;
	}

	public SFXManager() {
		MusicFactory.setAssetBasePath("sounds/");
		try {
			music  = MusicFactory.createMusicFromAsset(ResourcesManager.getInstance().activity.getMusicManager() ,ResourcesManager.getInstance().activity, "Metroid_To_Brinstar_OC_ReMix.ogg");
			music.setLooping(true);
		} catch (IOException e) {
			Debug.e(e);
		}
	}	
	
	public boolean isSoundMuted() {
		return soundsMuted;
	}
	
	public static boolean isMusicMuted() {
		return getInstance().musicMuted;
	}
	
	public static void setMusicMuted(final boolean pMuted) {
		getInstance().musicMuted = pMuted;
		if(getInstance().musicMuted) music.pause(); else music.play();
		GameActivity.writeIntToSharedPreferences(GameActivity.SHARED_PREFS_MUSIC_MUTED, (getInstance().musicMuted? 1:0));
	}
	
	public static boolean toggleMusicMuted() {
		getInstance().musicMuted = !getInstance().musicMuted;
		if(getInstance().musicMuted) music.pause(); else music.play();
		GameActivity.writeIntToSharedPreferences(GameActivity.SHARED_PREFS_MUSIC_MUTED, (getInstance().musicMuted? 1:0));
		return getInstance().musicMuted;
	}
	
	public static void playMusic() {
		if(!isMusicMuted())
			music.play();
	}
	
	public static void pauseMusic() {
		music.pause();
	}
	
	public static void resumeMusic() {
		if(!isMusicMuted())
			music.resume();
	}
	
	public static float getMusicVolume() {
		return music.getVolume();
	}
	
	public static void setMusicVolume(final float pVolume) {
		music.setVolume(pVolume);
	
	}

}
