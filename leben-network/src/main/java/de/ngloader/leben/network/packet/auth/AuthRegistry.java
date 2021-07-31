package de.ngloader.leben.network.packet.auth;

import de.ngloader.leben.network.packet.EnumPacketDirection;
import de.ngloader.leben.network.packet.auth.client.CPacketAuthFinish;
import de.ngloader.leben.network.packet.auth.client.CPacketAuthHandshake;
import de.ngloader.leben.network.packet.auth.server.SPacketAuthClientHello;
import de.ngloader.leben.network.packet.auth.server.SPacketAuthHandshake;
import de.ngloader.leben.network.packet.standart.StandartRegistry;

public class AuthRegistry extends StandartRegistry {

	public AuthRegistry() {
		this.registerPacket(EnumPacketDirection.CLIENT, CPacketAuthHandshake.class);
		this.registerPacket(EnumPacketDirection.CLIENT, CPacketAuthFinish.class);

		this.registerPacket(EnumPacketDirection.SERVER, SPacketAuthClientHello.class);
		this.registerPacket(EnumPacketDirection.SERVER, SPacketAuthHandshake.class);
	}
}