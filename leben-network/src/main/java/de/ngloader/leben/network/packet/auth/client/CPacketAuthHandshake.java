package de.ngloader.leben.network.packet.auth.client;

import de.ngloader.leben.network.packet.Packet;
import de.ngloader.leben.network.packet.auth.AuthClientPacketHandler;
import de.ngloader.leben.network.util.ByteBufUtil;
import io.netty.buffer.ByteBuf;

public class CPacketAuthHandshake implements Packet<AuthClientPacketHandler> {

	private byte[] data;

	public CPacketAuthHandshake() { }

	public CPacketAuthHandshake(byte[] data) {
		this.data = data;
	}

	@Override
	public void read(ByteBuf buffer) {
		this.data = ByteBufUtil.readByteArray(buffer);
	}

	@Override
	public void write(ByteBuf buffer) {
		this.data = ByteBufUtil.readByteArray(buffer);
	}

	@Override
	public void handle(AuthClientPacketHandler handler) {
		handler.handleHandshake(this);
	}

	public byte[] getData() {
		return this.data;
	}
}