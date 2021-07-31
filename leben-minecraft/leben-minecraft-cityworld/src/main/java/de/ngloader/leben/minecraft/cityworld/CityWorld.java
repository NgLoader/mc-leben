package de.ngloader.leben.minecraft.cityworld;

import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.ngloader.leben.core.minecraft.MCCore;
import de.ngloader.leben.core.minecraft.player.LebenPlayer;
import de.ngloader.leben.core.synced.LebenCoreConfig;
import de.ngloader.leben.core.synced.config.ConfigService;
import de.ngloader.leben.minecraft.cityworld.creator.CharacterCreator;

public class CityWorld extends MCCore {

	@Override
	public void onLoad() {

		try {
			LebenCoreConfig config = ConfigService.getConfig(LebenCoreConfig.class);

			Bukkit.setWhitelist(true);

			this.preInitialize(config, "CityWorld", "CityWorld-01");
		} catch (Exception e) {
			this.onFailed(e);
		}
	}

	@Override
	public void onEnable() {
		try {
			LebenCoreConfig config = ConfigService.getConfig(LebenCoreConfig.class);
			this.initialize(config);

			Bukkit.createWorld(new WorldCreator("creating"));

			this.getCommand("create").setExecutor(new CommandExecutor() {
				
				@Override
				public boolean onCommand(CommandSender var1, Command var2, String var3, String[] var4) {
					if (var1 instanceof Player) {
						Player player = (Player) var1;
						player.sendMessage("CREATEING!");
						new CharacterCreator(CityWorld.this, new LebenPlayer(player), result -> {
							Bukkit.broadcastMessage("Finished!");
						});
						return true;
					}
					var1.sendMessage("No player!");
					return true;
				}
			});
		} catch (Exception e) {
			this.onFailed(e);
		}
	}

	@Override
	public void onDisable() {
		try {
			Bukkit.setWhitelist(true);
//			Bukkit.getOnlinePlayers().forEach(player -> player.kickPlayer("§cRestarting..."));

			this.destroy();

			Bukkit.getServer().getScheduler().cancelTasks(this);
		} catch (Exception e) {
			this.onFailed(e);
		}
	}
}