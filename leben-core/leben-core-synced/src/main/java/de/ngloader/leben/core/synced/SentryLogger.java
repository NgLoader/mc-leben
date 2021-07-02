package de.ngloader.leben.core.synced;

import io.sentry.Sentry;
import io.sentry.SentryLevel;

public class SentryLogger {

	public SentryLogger(String serverType, String serverName) {
		Sentry.init(options -> {
			options.setDsn("https://41968b250dce406197d24b411afa6f03@sentry.zockercraft.net/2");
			options.setShutdownTimeout(5000);
			options.setDiagnosticLevel(SentryLevel.ERROR);
			options.setTag("ServerType", serverType);
			options.setTag("ServerName", serverName);
		});
	}
}