package de.ngloader.leben.network.packet;

import java.io.IOException;

import de.ngloader.leben.network.util.ByteBufUtil;
import io.netty.buffer.ByteBuf;

public class PacketCodecAbstract {

	private final EnumPacketDirection encodeDirection;
	private final EnumPacketDirection decodeDirection;

	public PacketCodecAbstract(EnumPacketDirection encodeDirection, EnumPacketDirection decodeDirection) {
		this.encodeDirection = encodeDirection;
		this.decodeDirection = decodeDirection;
	}

	public void encode(PacketRegistry registry, Packet<? extends PacketHandler> packet, ByteBuf out) throws Exception {
		int id = registry.getId(this.encodeDirection, packet);

		if (id == -1) {
			throw new IOException("Unregistered packet id");
		}

		ByteBufUtil.writeVarInt(out, id);
		packet.write(out);
	}

	public Packet<? extends PacketHandler> decode(PacketRegistry registry, ByteBuf in) throws Exception {
		int id = ByteBufUtil.readVarInt(in);

		Packet<? extends PacketHandler> packet = registry.getPacket(this.decodeDirection, id);
		if (packet == null) {
			throw new IOException("Unable to locate packet");
		}

		packet.read(in);
		if (in.readableBytes() > 0) {
			System.out.println(in.readableBytes());
			System.out.println(io.netty.buffer.ByteBufUtil.prettyHexDump(in.readBytes(in.readableBytes())));
			throw new IOException(String.format("Packet \"%s\" has unreaded bytes", packet.getClass().getSimpleName()));
		}

		return packet;
	}
}