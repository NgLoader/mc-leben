package de.ngloader.leben.network.packet.bungee;

import de.ngloader.leben.network.packet.PacketHandler;
import de.ngloader.leben.network.packet.bungee.server.SPacketBungeeInventoryLoad;

public interface BungeeServerPacketHandler extends PacketHandler {

	public void handleInventoryLoad(SPacketBungeeInventoryLoad packet);
}