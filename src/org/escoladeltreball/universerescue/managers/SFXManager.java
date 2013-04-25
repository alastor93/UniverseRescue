package org.escoladeltreball.universerescue.managers;

import org.andengine.audio.music.Music;

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

	
	

}
