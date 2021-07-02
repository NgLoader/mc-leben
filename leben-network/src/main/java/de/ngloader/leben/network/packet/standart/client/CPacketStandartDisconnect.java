package de.ngloader.leben.network.packet.standart.client;

import de.ngloader.leben.network.packet.Packet;
import de.ngloader.leben.network.packet.standart.StandartClientPacketHandler;
import de.ngloader.leben.network.util.ByteBufUtil;
import io.netty.buffer.ByteBuf;

public class CPacketStandartDisconnect implements Packet<StandartClientPacketHandler> {

	private String reason;

	public CPacketStandartDisconnect() { }

	public CPacketStandartDisconnect(String reason) {
		this.reason = reason;
	}

	@Override
	public void read(ByteBuf buffer) {
		this.reason = ByteBufUtil.readString(buffer);
	}

	@Override
	public void write(ByteBuf buffer) {
		ByteBufUtil.writeString(buffer, this.reason);
	}

	@Override
	public void handle(StandartClientPacketHandler handler) {
		handler.handleDisconnect(this);
	}

	public String getReason() {
		return this.reason;
	}
}