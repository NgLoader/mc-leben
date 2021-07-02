package de.ngloader.leben.network.packet.bungee.client;

import java.util.UUID;

import de.ngloader.leben.network.packet.Packet;
import de.ngloader.leben.network.packet.bungee.BungeeClientPacketHandler;
import de.ngloader.leben.network.util.ByteBufUtil;
import io.netty.buffer.ByteBuf;

public class CPacketBungeeInventoryLoad implements Packet<BungeeClientPacketHandler> {

	private UUID uuid;
	private byte[] data;

	public CPacketBungeeInventoryLoad() { }

	public CPacketBungeeInventoryLoad(UUID uuid, byte[] data) {
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
	public void handle(BungeeClientPacketHandler handler) {
		handler.handleInventoryLoad(this);
	}

	public UUID getUUID() {
		return this.uuid;
	}

	public byte[] getData() {
		return this.data;
	}
}
