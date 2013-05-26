package org.escoladeltreball.universerescue.managers;

import java.io.IOException;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.util.debug.Debug;
import org.escoladeltreball.universerescue.GameActivity;

public class SFXManager {
	//Attributes
	private static SFXManager obj = null;
	private static Music music;
	public boolean musicMuted;
	private static Sound shoot;

	//Singleton
	public static SFXManager getInstance() {
		if (obj == null) {
			obj = new SFXManager();
		}
		return obj;
	}

	//Constructor
	public SFXManager() {
		MusicFactory.setAssetBasePath("sounds/");
		try {
			music = MusicFactory.createMusicFromAsset(
					ResourcesManager.getInstance().activity.getMusicManager(),
					ResourcesManager.getInstance().activity,
					"Metroid_To_Brinstar_OC_ReMix.ogg");
			music.setLooping(true);
		} catch (IOException e) {
			Debug.e(e);
		}
		SoundFactory.setAssetBasePath("sounds/");
		try {
			shoot = SoundFactory.createSoundFromAsset(ResourcesManager
					.getActivity().getSoundManager(), ResourcesManager
					.getActivity(), "shoot.ogg");
		} catch (final IOException e) {
			Debug.e(e);
		}
	}
	/**
	 * Check if the music is muted
	 * @return true if is muted false otherwhise
	 */
	public static boolean isMusicMuted() {
		return getInstance().musicMuted;
	}
	
	/**
	 * pause or play the music depends of the param
	 * @param pMuted
	 */
	public static void setMusicMuted(final boolean pMuted) {
		getInstance().musicMuted = pMuted;
		if (getInstance().musicMuted)
			music.pause();
		else
			music.play();
		GameActivity.writeIntToSharedPreferences(
				GameActivity.SHARED_PREFS_MUSIC_MUTED,
				(getInstance().musicMuted ? 1 : 0));
	}

	/**
	 * Check if the music is muted
	 * @return the vakue of the var musicMuted
	 */
	public static boolean toggleMusicMuted() {
		getInstance().musicMuted = !getInstance().musicMuted;
		if (getInstance().musicMuted)
			music.pause();
		else
			music.play();
		GameActivity.writeIntToSharedPreferences(
				GameActivity.SHARED_PREFS_MUSIC_MUTED,
				(getInstance().musicMuted ? 1 : 0));
		return getInstance().musicMuted;
	}

	/**
	 * Play the music
	 */
	public static void playMusic() {
		if (!isMusicMuted()) {
			music.play();
		}
	}
	/**
	 * Pause the music
	 */
	public static void pauseMusic() {
		music.pause();
	}
	/**
	 * Resume the music
	 */
	public static void resumeMusic() {
		if (!isMusicMuted()) {
			music.resume();
		}
	}

	/**
	 * Get the volume of the music
	 * @return volume of music
	 */
	public static float getMusicVolume() {
		return music.getVolume();
	}

	/**
	 * Set the volume of the music
	 * @param pVolume
	 */
	public static void setMusicVolume(final float pVolume) {
		music.setVolume(pVolume);

	}
	/**
	 * Play the sound of the weapon
	 * @param pRate
	 * @param pVolume
	 */
	public static void playShoot(final float pRate, final float pVolume) {
		playSound(shoot, pRate, pVolume);
	}
	
	/**
	 * Play the sound of the parameter
	 * @param pSound
	 * @param pRate
	 * @param pVolume
	 */
	private static void playSound(final Sound pSound, final float pRate,
			final float pVolume) {
		if (!isMusicMuted()) {
			pSound.setRate(pRate);
			pSound.setVolume(pVolume);
			pSound.play();
		}
	}
}
