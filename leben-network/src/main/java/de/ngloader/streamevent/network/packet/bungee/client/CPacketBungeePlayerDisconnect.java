package de.ngloader.streamevent.network.packet.bungee.client;

import java.util.UUID;

import de.ngloader.streamevent.network.packet.Packet;
import de.ngloader.streamevent.network.packet.bungee.BungeeClientPacketHandler;
import de.ngloader.streamevent.network.util.ByteBufUtil;
import io.netty.buffer.ByteBuf;

public class CPacketBungeePlayerDisconnect implements Packet<BungeeClientPacketHandler> {

	private UUID uuid;

	public CPacketBungeePlayerDisconnect() { }

	public CPacketBungeePlayerDisconnect(UUID uuid) {
		this.uuid = uuid;
	}

	@Override
	public void read(ByteBuf buffer) {
		this.uuid = ByteBufUtil.readUUID(buffer);
	}

	@Override
	public void write(ByteBuf buffer) {
		ByteBufUtil.writeUUID(buffer, this.uuid);
	}

	@Override
	public void handle(BungeeClientPacketHandler handler) {
		handler.handlePlayerDisconnect(this);
	}

	public UUID getUUID() {
		return this.uuid;
	}
}