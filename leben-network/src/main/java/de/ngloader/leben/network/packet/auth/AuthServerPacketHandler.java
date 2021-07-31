package de.ngloader.leben.network.packet.auth;

import de.ngloader.leben.network.packet.PacketHandler;
import de.ngloader.leben.network.packet.auth.server.SPacketAuthClientHello;
import de.ngloader.leben.network.packet.auth.server.SPacketAuthHandshake;

public interface AuthServerPacketHandler extends PacketHandler {

	public void handleClientHello(SPacketAuthClientHello packet);

	public void handleHandshake(SPacketAuthHandshake packet);
}