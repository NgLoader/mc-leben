package de.ngloader.streamevent.core.bungee;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.ngloader.streamevent.core.bungee.manager.PlayerManager;
import de.ngloader.streamevent.core.bungee.pluginmessage.PluginMessageHandler;
import de.ngloader.streamevent.core.synced.LebenCoreConfig;
import de.ngloader.streamevent.core.synced.SentryLogger;
import de.ngloader.streamevent.core.synced.config.ConfigService;
import de.ngloader.streamevent.core.synced.redis.RedisManager;
import io.sentry.Sentry;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public class ProxyCore extends Plugin {

	private static String SERVER_NAME;

	public static String getServerName() {
		return SERVER_NAME;
	}

	public static void setServerName(String serverName) {
		SERVER_NAME = serverName;
	}

	private final Map<ProxyModule, Set<Listener>> moduleListeners = new HashMap<>();
	private final Map<Class<? extends ProxyModule>, ProxyModule> modules = new HashMap<>();

	private SentryLogger sentryLogger;

	private RedisManager redisManager;
	private PlayerManager playerManager;

	private PluginMessageHandler pluginMessageHandler;

	@Override
	public void onLoad() {
		setServerName("Leben-Proxy");

		this.sentryLogger = new SentryLogger("Proxy", "Proxy-01");
	}

	@Override
	public void onEnable() {
		LebenCoreConfig config = ConfigService.getConfig(LebenCoreConfig.class);
		this.redisManager = new RedisManager(config);

		this.pluginMessageHandler = new PluginMessageHandler(this);

		this.playerManager = new PlayerManager(this);
	}

	protected void destroy() {
		for (ProxyModule module : this.modules.values()) {
			try {
				module.destroy();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	protected void onFailed(Exception e) {
		e.printStackTrace();

		try {
			Sentry.captureException(e);
			Sentry.close();
		} catch (Exception e2) {
			e2.printStackTrace();
		} finally {
			ProxyServer.getInstance().stop();
		}
	}

	public <T extends ProxyModule> T getModule(Class<T> moduleClass) {
		ProxyModule module = this.modules.get(moduleClass);
		if (module == null) {
			try {
				T instance = moduleClass.getConstructor(ProxyModule.class).newInstance(this);
				this.modules.put(moduleClass, instance);
				return instance;
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
			return null;
		}
		return moduleClass.cast(module);
	}

	public void registerEvents(ProxyModule module, Listener... listeners) {
		Set<Listener> moduleListeners = this.moduleListeners.get(module);
		if (moduleListeners == null) {
			moduleListeners = new HashSet<>();
			this.moduleListeners.put(module, moduleListeners);
		}

		for (Listener listener : listeners) {
			ProxyServer.getInstance().getPluginManager().registerListener(this, listener);
			moduleListeners.add(listener);
		}
	}

	public void unregisterEvents(ProxyModule module) {
		Set<Listener> listeners = this.moduleListeners.remove(module);
		if (listeners != null) {
			listeners.forEach(ProxyServer.getInstance().getPluginManager()::unregisterListener);
		}
	}

	public void unregisterEvents(ProxyModule module, Listener... listeners) {
		Set<Listener> moduleListeners = this.moduleListeners.get(module);
		if (moduleListeners == null) {
			return;
		}

		PluginManager pluginManager = ProxyServer.getInstance().getPluginManager();
		for (Listener listener : listeners) {
			pluginManager.unregisterListener(listener);
			moduleListeners.remove(listener);
		}
	}

	public SentryLogger getLoggerSystem() {
		return this.sentryLogger;
	}

	public RedisManager getRedisManager() {
		return this.redisManager;
	}

	public PluginMessageHandler getPluginMessageHandler() {
		return this.pluginMessageHandler;
	}

	public PlayerManager getPlayerManager() {
		return this.playerManager;
	}
}