package de.ngloader.leben.network.packet.standart;

import de.ngloader.leben.network.packet.PacketHandler;
import de.ngloader.leben.network.packet.standart.client.CPacketStandartDisconnect;
import de.ngloader.leben.network.packet.standart.client.CPacketStandartKeepAlive;

public interface StandartClientPacketHandler extends PacketHandler {

	public void handleKeepAlive(CPacketStandartKeepAlive packet);

	public void handleDisconnect(CPacketStandartDisconnect packet);
}