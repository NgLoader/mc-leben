package de.ngloader.streamevent.network.packet.bungee;

import de.ngloader.streamevent.network.packet.PacketHandler;
import de.ngloader.streamevent.network.packet.bungee.client.CPacketBungeePlayerDisconnect;
import de.ngloader.streamevent.network.packet.bungee.client.CPacketBungeeInventoryLoad;

public interface BungeeClientPacketHandler extends PacketHandler {

	public void handleInventoryLoad(CPacketBungeeInventoryLoad packet);

	public void handlePlayerDisconnect(CPacketBungeePlayerDisconnect packet);
}