package de.ngloader.leben.core.synced;

import io.sentry.Sentry;
import io.sentry.SentryLevel;

public class SentryLogger {

	public SentryLogger(LebenCoreConfig config, String serverType, String serverName) {
		Sentry.init(options -> {
			options.setDsn(config.sentryUrl);
			options.setShutdownTimeout(5000);
			options.setDiagnosticLevel(SentryLevel.ERROR);
			options.setTag("ServerType", serverType);
			options.setTag("ServerName", serverName);
		});
	}
}