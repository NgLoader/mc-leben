package de.ngloader.leben.network.packet.auth.client;

import de.ngloader.leben.network.packet.Packet;
import de.ngloader.leben.network.packet.auth.AuthClientPacketHandler;
import io.netty.buffer.ByteBuf;

public class CPacketAuthFinish implements Packet<AuthClientPacketHandler> {

	public CPacketAuthFinish() { }

	@Override
	public void read(ByteBuf buffer) { }

	@Override
	public void write(ByteBuf buffer) { }

	@Override
	public void handle(AuthClientPacketHandler handler) {
		handler.handleFinish(this);
	}
}