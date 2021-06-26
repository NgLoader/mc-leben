package de.ngloader.leben.plotworld;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.plugin.java.JavaPlugin;

import de.ngloader.streamevent.core.synced.util.ReflectionUtil;
import net.minecraft.server.v1_16_R3.DedicatedServer;
import net.minecraft.server.v1_16_R3.IRegistryCustom.Dimension;
import net.minecraft.server.v1_16_R3.WorldNBTStorage;

public class Leben extends JavaPlugin {

	@Override
	public void onLoad() {
		CraftServer server = (CraftServer) Bukkit.getServer();
		DedicatedServer dedicatedServer = (DedicatedServer) ReflectionUtil.getField(server, "console");
		WorldNBTStorage worldNBTStorage = (WorldNBTStorage) ReflectionUtil.getField(dedicatedServer, "worldNBTStorage");
		Dimension dimension = (Dimension) ReflectionUtil.getField(dedicatedServer, "customRegistry");
		dedicatedServer.a(new LebenPlayerList(dedicatedServer, dimension, worldNBTStorage));
	}

	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {
	}
}