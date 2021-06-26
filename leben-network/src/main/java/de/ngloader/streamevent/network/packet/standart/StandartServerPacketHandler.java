package de.ngloader.streamevent.network.packet.standart;

import de.ngloader.streamevent.network.packet.PacketHandler;
import de.ngloader.streamevent.network.packet.standart.server.SPacketStandartKeepAlive;

public interface StandartServerPacketHandler extends PacketHandler {

	public void handleKeepAlive(SPacketStandartKeepAlive packet);
}