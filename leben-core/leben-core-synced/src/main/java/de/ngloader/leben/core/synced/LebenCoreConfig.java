package de.ngloader.leben.core.synced;

import de.ngloader.leben.core.synced.config.Config;

@Config(path = "leben", name = "core")
public class LebenCoreConfig {

	public String sentryUrl = "SENTRY URL";

	public RedisConfig redis = new RedisConfig();

	public MasterConfig master = new MasterConfig();

	public class RedisConfig {
		public String host = "0.0.0.0";
		public int port = 6379;

		public String password = "password";

		public String privateKey = "privateKey";
		public String publicCrt = "publicKey";
		public String pem = "pem";
	}

	public class MasterConfig {

		public String address = "0.0.0.0";
		public int port = 25000;

		public String sslPath = "ssl";
	}
}