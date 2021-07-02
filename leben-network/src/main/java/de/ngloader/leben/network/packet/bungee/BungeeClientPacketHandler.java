package de.ngloader.leben.network.packet.bungee;

import de.ngloader.leben.network.packet.PacketHandler;
import de.ngloader.leben.network.packet.bungee.client.CPacketBungeeInventoryLoad;
import de.ngloader.leben.network.packet.bungee.client.CPacketBungeePlayerDisconnect;

public interface BungeeClientPacketHandler extends PacketHandler {

	public void handleInventoryLoad(CPacketBungeeInventoryLoad packet);

	public void handlePlayerDisconnect(CPacketBungeePlayerDisconnect packet);
}