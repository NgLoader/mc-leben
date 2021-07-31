package de.ngloader.leben.core.synced.scheduler;

import io.sentry.Sentry;

public class LebenTask {

	private static final int FAIL_TRYS = 5;
	private static final int FAIL_TRYS_TIME = 5 * 5;

	private final int taskId;
	private final Runnable task;

	private int delay = 0;
	private int ticks = 0;

	private int currentlyDelay = 0;
	private int currentlyTick = 0;

	private boolean loop = false;

	private int failCount = 0;

	public LebenTask(Runnable task, int taskId, int delay) {
		this.task = task;
		this.taskId = taskId;
		this.delay = delay;

		this.currentlyDelay = this.delay;
	}

	public LebenTask(Runnable task, int taskId, int delay, int ticks) {
		this(task, taskId, delay);
		this.ticks = ticks;

		this.currentlyTick = this.ticks;
	}

	public LebenTask(Runnable task, int taskId, int delay, int ticks, boolean loop) {
		this(task, taskId, delay, ticks);
		this.loop = loop;
	}

	public boolean tick() {
		if (this.currentlyDelay > 0) {
			this.currentlyDelay--;
		} else if (this.currentlyTick > 0) {
			this.currentlyTick--;
		} else {
			try {
				this.task.run();
				if (!this.loop) {
					return true;
				}

				this.currentlyTick = this.ticks;
				this.failCount = 0;
			} catch (Exception e) {
				Sentry.captureException(e);
				System.out.println(String.format("Error by running task. Try (%d/%d)", this.failCount, FAIL_TRYS));

				this.failCount++;
				if (this.failCount > FAIL_TRYS) {
					throw e;
				}
				this.currentlyTick = FAIL_TRYS_TIME;
			}
		}
		return false;
	}

	public void reset() {
		this.currentlyDelay = this.delay;
		this.currentlyTick = this.ticks;
	}

	public int getDelay() {
		return this.delay;
	}

	public int getTicks() {
		return this.ticks;
	}

	public int getCurrentlyDelay() {
		return this.currentlyDelay;
	}

	public int getCurrentlyTick() {
		return this.currentlyTick;
	}

	public boolean isLoop() {
		return this.loop;
	}

	public Runnable getTask() {
		return this.task;
	}

	public int getTaskId() {
		return this.taskId;
	}
}
