package de.ngloader.streamevent.network.packet.bungee.server;

import java.util.UUID;

import de.ngloader.streamevent.network.packet.Packet;
import de.ngloader.streamevent.network.packet.bungee.BungeeServerPacketHandler;
import de.ngloader.streamevent.network.util.ByteBufUtil;
import io.netty.buffer.ByteBuf;

public class SPacketBungeeInventoryLoad implements Packet<BungeeServerPacketHandler> {

	private UUID uuid;
	private byte[] data;

	public SPacketBungeeInventoryLoad() { }

	public SPacketBungeeInventoryLoad(UUID uuid, byte[] data) {
		this.uuid = uuid;
		this.data = data;
	}

	@Override
	public void read(ByteBuf buffer) {
		this.uuid = ByteBufUtil.readUUID(buffer);
		this.data = ByteBufUtil.readByteArray(buffer);
	}

	@Override
	public void write(ByteBuf buffer) {
		ByteBufUtil.writeUUID(buffer, this.uuid);
		ByteBufUtil.writeByteArray(buffer, this.data);
	}

	@Override
	public void handle(BungeeServerPacketHandler handler) {
		handler.handleInventoryLoad(this);
	}

	public UUID getUUID() {
		return this.uuid;
	}

	public byte[] getData() {
		return this.data;
	}
}