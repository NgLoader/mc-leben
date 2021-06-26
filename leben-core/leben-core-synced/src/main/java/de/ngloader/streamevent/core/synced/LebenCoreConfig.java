package de.ngloader.streamevent.core.synced;

import de.ngloader.streamevent.core.synced.config.Config;

@Config(path = "leben", name = "core")
public class LebenCoreConfig {

	public String sentryUrl;

	public String backendApiPath;

	public String redisUri;

	public MasterConfig master;

	public class MasterConfig {

		public String address;
		public int port;

		public String sslPath;
	}
}