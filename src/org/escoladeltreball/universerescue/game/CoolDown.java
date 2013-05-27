package org.escoladeltreball.universerescue.game;

import java.util.Timer;
import java.util.TimerTask;

public class CoolDown {
	//Attributes
	private boolean valid;
	private Timer timer;

	//Constructor
	public CoolDown() {
		timer = new Timer();
		valid = true;
	}

	/**
	 * See if the time in the param has passed
	 * @param delay
	 * @return true if is passed false otherwhise
	 */
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
