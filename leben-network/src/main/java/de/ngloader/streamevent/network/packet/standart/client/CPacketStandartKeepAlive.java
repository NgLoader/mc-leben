package de.ngloader.streamevent.network.packet.standart.client;

import de.ngloader.streamevent.network.packet.Packet;
import de.ngloader.streamevent.network.packet.standart.StandartClientPacketHandler;
import io.netty.buffer.ByteBuf;

public class CPacketStandartKeepAlive implements Packet<StandartClientPacketHandler> {

	private long time;

	public CPacketStandartKeepAlive() { }

	public CPacketStandartKeepAlive(long time) {
		this.time = time;
	}

	@Override
	public void read(ByteBuf buffer) {
		this.time = buffer.readLong();
	}

	@Override
	public void write(ByteBuf buffer) {
		buffer.writeLong(this.time);
	}

	@Override
	public void handle(StandartClientPacketHandler handler) {
		handler.handleKeepAlive(this);
	}

	public long getTime() {
		return this.time;
	}
}