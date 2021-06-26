package de.ngloader.streamevent.core.minecraft;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import de.ngloader.streamevent.core.minecraft.player.PlayerManager;
import de.ngloader.streamevent.core.minecraft.pluginmessage.PluginMessageHandler;
import de.ngloader.streamevent.core.minecraft.typeadapter.TypeAdapterBlockFace;
import de.ngloader.streamevent.core.minecraft.typeadapter.TypeAdapterDamageCause;
import de.ngloader.streamevent.core.minecraft.typeadapter.TypeAdapterMaterial;
import de.ngloader.streamevent.core.synced.LebenCoreConfig;
import de.ngloader.streamevent.core.synced.SentryLogger;
import de.ngloader.streamevent.core.synced.config.ConfigService;
import de.ngloader.streamevent.core.synced.redis.RedisManager;
import io.sentry.Sentry;

public class MCCore extends JavaPlugin {

	private static String SERVER_NAME;

	public static String getServerName() {
		return SERVER_NAME;
	}

	public static void setServerName(String serverName) {
		SERVER_NAME = serverName;
	}

	static {
		ConfigService.addTypeAdapter(new TypeAdapterBlockFace());
		ConfigService.addTypeAdapter(new TypeAdapterMaterial());
		ConfigService.addTypeAdapter(new TypeAdapterDamageCause());
	}

	private final Map<MCModule, Set<Listener>> moduleListeners = new HashMap<>();
	private final Map<Class<? extends MCModule>, MCModule> modules = new HashMap<>();

	private SentryLogger sentryLogger;

	private RedisManager redisManager;
	private PlayerManager playerManager;

	private PluginMessageHandler pluginMessageHandler;

	protected void preInitialize(String serverType, String serverName) {
		setServerName(serverName);

		this.sentryLogger = new SentryLogger(serverType, serverName);
	}

	protected void initialize(LebenCoreConfig config) {
		this.redisManager = new RedisManager(config);
		this.playerManager = new PlayerManager(this);

		this.pluginMessageHandler = new PluginMessageHandler(this);
	}

	protected void destroy() {
		for (MCModule module : this.modules.values()) {
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
			if (this.sentryLogger != null) {
				Sentry.captureException(e);
				Sentry.close();
			}
		} catch (Exception e2) {
			e2.printStackTrace();
		} finally {
			Bukkit.shutdown();
		}
	}

	public <T extends MCModule> T getModule(Class<T> moduleClass) {
		MCModule module = this.modules.get(moduleClass);
		if (module == null) {
			try {
				T instance = moduleClass.getConstructor(MCModule.class).newInstance(this);
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

	public void registerEvents(MCModule module, Listener... listeners) {
		Set<Listener> moduleListeners = this.moduleListeners.get(module);
		if (moduleListeners == null) {
			moduleListeners = new HashSet<>();
			this.moduleListeners.put(module, moduleListeners);
		}

		for (Listener listener : listeners) {
			Bukkit.getPluginManager().registerEvents(listener, this);
			moduleListeners.add(listener);
		}
	}

	public void unregisterEvents(MCModule module) {
		Set<Listener> listeners = this.moduleListeners.remove(module);
		if (listeners != null) {
			listeners.forEach(HandlerList::unregisterAll);
		}
	}

	public void unregisterEvents(MCModule module, Listener... listeners) {
		Set<Listener> moduleListeners = this.moduleListeners.get(module);
		if (moduleListeners == null) {
			return;
		}

		for (Listener listener : listeners) {
			HandlerList.unregisterAll(listener);
			moduleListeners.remove(listener);
		}
	}

	public SentryLogger getLoggerSystem() {
		return this.sentryLogger;
	}

	public RedisManager getRedisManager() {
		return this.redisManager;
	}

	public PlayerManager getPlayerManager() {
		return this.playerManager;
	}

	public PluginMessageHandler getPluginMessageHandler() {
		return this.pluginMessageHandler;
	}
}