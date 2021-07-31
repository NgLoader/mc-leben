package de.ngloader.leben.network.packet.auth.server;

import de.ngloader.leben.network.packet.Packet;
import de.ngloader.leben.network.packet.auth.AuthServerPacketHandler;
import io.netty.buffer.ByteBuf;

public class SPacketAuthClientHello implements Packet<AuthServerPacketHandler> {

	public SPacketAuthClientHello() { }

	@Override
	public void read(ByteBuf buffer) { }

	@Override
	public void write(ByteBuf buffer) { }

	@Override
	public void handle(AuthServerPacketHandler handler) {
		handler.handleClientHello(this);
	}
}