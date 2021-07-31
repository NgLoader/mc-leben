package de.ngloader.leben.network.packet.auth.server;

import de.ngloader.leben.network.packet.Packet;
import de.ngloader.leben.network.packet.auth.AuthServerPacketHandler;
import de.ngloader.leben.network.util.ByteBufUtil;
import io.netty.buffer.ByteBuf;

public class SPacketAuthHandshake implements Packet<AuthServerPacketHandler> {

	private byte[] data;

	public SPacketAuthHandshake() { }

	public SPacketAuthHandshake(byte[] data) {
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
	public void handle(AuthServerPacketHandler handler) {
		handler.handleHandshake(this);
	}

	public byte[] getData() {
		return this.data;
	}
}