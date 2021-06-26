package de.ngloader.streamevent.core.bungee.pluginmessage;

import de.ngloader.streamevent.core.bungee.manager.PlayerManager;
import de.ngloader.streamevent.network.NetworkManager;
import de.ngloader.streamevent.network.packet.bungee.BungeeServerPacketHandler;
import de.ngloader.streamevent.network.packet.bungee.server.SPacketBungeeInventoryLoad;

public class StandartPacketHandler implements BungeeServerPacketHandler {

	private final PlayerManager playerManager;

	public StandartPacketHandler(PluginMessageHandler messageHandler) {
		this.playerManager = messageHandler.getPlugin().getPlayerManager();
	}

	@Override
	public void handleInventoryLoad(SPacketBungeeInventoryLoad packet) {
		this.playerManager.reciveData(packet.getUUID(), packet.getData());
	}

	@Override
	public void setNetworkManager(NetworkManager manager) {
		throw new UnsupportedOperationException("setNetworkManager is not supported");
	}
}