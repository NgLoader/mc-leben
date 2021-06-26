package de.ngloader.streamevent.network.packet.bungee;

import de.ngloader.streamevent.network.packet.PacketHandler;
import de.ngloader.streamevent.network.packet.bungee.server.SPacketBungeeInventoryLoad;

public interface BungeeServerPacketHandler extends PacketHandler {

	public void handleInventoryLoad(SPacketBungeeInventoryLoad packet);
}