package de.ngloader.leben.core.synced.scheduler;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import de.ngloader.leben.core.synced.util.LebenThread;
import io.sentry.Sentry;

public class LebenScheduler implements Runnable {

	private final AtomicInteger taskIds = new AtomicInteger();
	private final List<LebenTask> tasks = new LinkedList<LebenTask>();

	private AtomicBoolean running = new AtomicBoolean(true);

	public LebenScheduler() {
		LebenThread.run(this::run);
	}

	public void run() {
		try {
			long lastExecute;
			while (this.running.get()) {
				lastExecute = System.currentTimeMillis();

				for (Iterator<LebenTask> iterator = tasks.iterator(); iterator.hasNext(); ) {
					LebenTask task = iterator.next();

					try {
						if (task.tick()) {
							iterator.remove();
						}
					} catch(Exception e) {
						e.printStackTrace();
						Sentry.captureException(e);

						System.out.println("Error by running task. Removing task from scheduler");
						iterator.remove();
					}
				}

				long executeTime = System.currentTimeMillis() - lastExecute;
				if (executeTime > 200) {
					Sentry.captureMessage("LebenScheduler is running " + ((int) (executeTime / 200)) + " ticks behind.");
					Thread.sleep(200);
				} else {
					Thread.sleep(200 - executeTime);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			Sentry.captureException(e);

			this.running.set(false);
		}
	}

	public void stop() {
		this.running.set(false);
		this.cancelAllTasks();
	}

	/**
	 * 
	 * @param shard
	 * @param task
	 * @param delay in ticks (5 ticks -> 1 second)
	 * @return WuffyInfoTask
	 */
	public int runTask(Runnable task, int delay) {
		LebenTask taskInfo = new LebenTask(task, this.taskIds.getAndIncrement(), delay);
		this.tasks.add(taskInfo);
		return taskInfo.getTaskId();
	}

	/**
	 * 
	 * @param shard
	 * @param task
	 * @param delay in ticks (5 ticks -> 1 second)
	 * @param tick in ticks (5 ticks -> 1 second)
	 * @return WuffyInfoTask
	 */
	public int runTaskLoop(Runnable task, int delay, int tick) {
		LebenTask taskInfo = new LebenTask(task, this.taskIds.getAndIncrement(), delay, tick, true);
		this.tasks.add(taskInfo);
		return taskInfo.getTaskId();
	}

	/**
	 * 
	 * @param task
	 * @return true when the task was stopped or false when the task can not be stopped (not exist)
	 */
	public boolean cancelTask(Runnable task) {
		return this.tasks.removeAll(this.tasks.stream().filter(taskInfo -> taskInfo.getTask().equals(task)).collect(Collectors.toList()));
	}

	/**
	 * 
	 * @param taskId
	 * @return true when the task was stopped or false when the task can not be stopped (not exist)
	 */
	public boolean cancelTask(int taskId) {
		return this.tasks.removeAll(this.tasks.stream().filter(taskInfo -> taskInfo.getTaskId() == taskId).collect(Collectors.toList()));
	}

	/**
	 * 
	 * @return true when any task was stopped or false when no task exist to stop
	 */
	public boolean cancelAllTasks() {
		if(this.tasks.isEmpty())
			return false;

		this.tasks.clear();
		return true;
	}
}