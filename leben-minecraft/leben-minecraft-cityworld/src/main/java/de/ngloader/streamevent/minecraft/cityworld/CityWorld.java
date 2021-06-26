package de.ngloader.streamevent.minecraft.cityworld;

import org.bukkit.Bukkit;

import de.ngloader.streamevent.core.minecraft.MCCore;
import de.ngloader.streamevent.core.synced.LebenCoreConfig;
import de.ngloader.streamevent.core.synced.config.ConfigService;

public class CityWorld extends MCCore {

	@Override
	public void onLoad() {
		Bukkit.setWhitelist(true);

		this.preInitialize("https://036486f6a61e4bfe913821da47fa9f0e@sentry.es-intern.de/6", "CityWorld");
	}

	@Override
	public void onEnable() {
		try {
			LebenCoreConfig config = ConfigService.getConfig(LebenCoreConfig.class);
			this.initialize(config);
		} catch (Exception e) {
			this.onFailed(e);
		}
	}

	@Override
	public void onDisable() {
		try {
			Bukkit.setWhitelist(true);
			Bukkit.getOnlinePlayers().forEach(player -> player.kickPlayer("§cRestarting..."));

			this.destroy();

			Bukkit.getServer().getScheduler().cancelTasks(this);
		} catch (Exception e) {
			this.onFailed(e);
		}
	}
}