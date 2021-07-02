package de.ngloader.leben.network.packet.bungee;

import de.ngloader.leben.network.packet.EnumPacketDirection;
import de.ngloader.leben.network.packet.PacketRegistry;
import de.ngloader.leben.network.packet.bungee.client.CPacketBungeeInventoryLoad;
import de.ngloader.leben.network.packet.bungee.client.CPacketBungeePlayerDisconnect;
import de.ngloader.leben.network.packet.bungee.server.SPacketBungeeInventoryLoad;

public class BungeeRegistry extends PacketRegistry {

	public BungeeRegistry() {
		this.registerPacket(EnumPacketDirection.CLIENT, CPacketBungeeInventoryLoad.class);
		this.registerPacket(EnumPacketDirection.CLIENT, CPacketBungeePlayerDisconnect.class);

		this.registerPacket(EnumPacketDirection.SERVER, SPacketBungeeInventoryLoad.class);
	}
}