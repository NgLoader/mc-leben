package de.ngloader.streamevent.network.packet.bungee;

import de.ngloader.streamevent.network.packet.EnumPacketDirection;
import de.ngloader.streamevent.network.packet.PacketRegistry;
import de.ngloader.streamevent.network.packet.bungee.client.CPacketBungeePlayerDisconnect;
import de.ngloader.streamevent.network.packet.bungee.client.CPacketBungeeInventoryLoad;
import de.ngloader.streamevent.network.packet.bungee.server.SPacketBungeeInventoryLoad;

public class BungeeRegistry extends PacketRegistry {

	public BungeeRegistry() {
		this.registerPacket(EnumPacketDirection.CLIENT, CPacketBungeeInventoryLoad.class);
		this.registerPacket(EnumPacketDirection.CLIENT, CPacketBungeePlayerDisconnect.class);

		this.registerPacket(EnumPacketDirection.SERVER, SPacketBungeeInventoryLoad.class);
	}
}