package de.ngloader.leben.master;

import de.ngloader.leben.core.synced.scheduler.LebenScheduler;

public class Master {

	public static void main(String[] args) {
		new Master(args);
	}

	private final LebenScheduler scheduler = new LebenScheduler();

	public Master(String[] args) {
	}
}