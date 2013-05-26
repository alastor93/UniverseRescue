package org.escoladeltreball.universerescue.game;

import java.util.Timer;
import java.util.TimerTask;

public class CoolDown {
	private boolean valid;
	private Timer timer;

	public CoolDown() {
		timer = new Timer();
		valid = true;
	}

	public boolean timeHasPassed(long delay) {
		if (valid) {
			valid = false;
			timer.schedule(new Task(), delay);
			return true;
		}
		return false;
	}

	class Task extends TimerTask {

		public void run() {
			valid = true;
		}

	}
}
