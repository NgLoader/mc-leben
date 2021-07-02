package de.ngloader.leben.core.minecraft.pluginmessage;

import org.bukkit.Bukkit;

import de.ngloader.leben.core.minecraft.player.PlayerManager;
import de.ngloader.leben.network.NetworkManager;
import de.ngloader.leben.network.packet.bungee.BungeeClientPacketHandler;
import de.ngloader.leben.network.packet.bungee.client.CPacketBungeeInventoryLoad;
import de.ngloader.leben.network.packet.bungee.client.CPacketBungeePlayerDisconnect;

public class StandartPacketHandler implements BungeeClientPacketHandler {

	private final PlayerManager playerManager;

	public StandartPacketHandler(PluginMessageHandler messageHandler) {
		this.playerManager = messageHandler.getPlugin().getPlayerManager();
	}

	@Override
	public void handleInventoryLoad(CPacketBungeeInventoryLoad packet) {
		this.playerManager.loadBungeePlayer(Bukkit.getPlayer(packet.getUUID()), packet.getData());
	}

	@Override
	public void handlePlayerDisconnect(CPacketBungeePlayerDisconnect packet) {
		this.playerManager.quitPlayer(Bukkit.getPlayer(packet.getUUID()));
	}

	@Override
	public void setNetworkManager(NetworkManager manager) {
		throw new UnsupportedOperationException("setNetworkManager is not supported");
	}
}