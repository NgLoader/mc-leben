package de.ngloader.leben.core.synced.util;

import java.util.concurrent.ForkJoinPool;

import io.sentry.Sentry;

public abstract class LebenThread {

	private static final ForkJoinPool POOL = new ForkJoinPool(
			Runtime.getRuntime().availableProcessors(),
			ForkJoinPool.defaultForkJoinWorkerThreadFactory,
			LebenThread::uncaughtException,
			true);

	private static void uncaughtException(Thread thread, Throwable exception) {
		Sentry.captureException(exception);
	}

	public static void run(Runnable runnable) {
		POOL.execute(runnable);
	}
}