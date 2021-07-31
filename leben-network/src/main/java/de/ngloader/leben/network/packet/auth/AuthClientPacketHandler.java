package de.ngloader.leben.network.packet.auth;

import de.ngloader.leben.network.packet.PacketHandler;
import de.ngloader.leben.network.packet.auth.client.CPacketAuthFinish;
import de.ngloader.leben.network.packet.auth.client.CPacketAuthHandshake;

public interface AuthClientPacketHandler extends PacketHandler {

	public void handleFinish(CPacketAuthFinish packet);

	public void handleHandshake(CPacketAuthHandshake packet);
}