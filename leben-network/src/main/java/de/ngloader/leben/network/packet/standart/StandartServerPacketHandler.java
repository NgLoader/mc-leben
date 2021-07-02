package de.ngloader.leben.network.packet.standart;

import de.ngloader.leben.network.packet.PacketHandler;
import de.ngloader.leben.network.packet.standart.server.SPacketStandartKeepAlive;

public interface StandartServerPacketHandler extends PacketHandler {

	public void handleKeepAlive(SPacketStandartKeepAlive packet);
}