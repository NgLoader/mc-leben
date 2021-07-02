package de.ngloader.leben.network.packet;

import java.io.IOException;
import java.util.List;

import de.ngloader.leben.network.NetworkManager;
import de.ngloader.leben.network.util.ByteBufUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

public class PacketCodec extends ByteToMessageCodec<Packet<?>> {

	private final EnumPacketDirection encodeDirection;
	private final EnumPacketDirection decodeDirection;

	public PacketCodec(EnumPacketDirection encodeDirection, EnumPacketDirection decodeDirection) {
		this.encodeDirection = encodeDirection;
		this.decodeDirection = decodeDirection;
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, Packet<?> packet, ByteBuf out) throws Exception {
		PacketRegistry registry = ctx.channel().attr(NetworkManager.PACKET_REGISTRY).get();
		int id = registry.getId(this.encodeDirection, packet);

		if (id == -1) {
			throw new IOException("Unregistered packet id");
		}

		ByteBufUtil.writeVarInt(out, id);
		packet.write(out);
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		PacketRegistry registry = ctx.channel().attr(NetworkManager.PACKET_REGISTRY).get();
		int id = ByteBufUtil.readVarInt(in);

		Packet<?> packet = registry.getPacket(this.decodeDirection, id);
		if (packet == null) {
			throw new IOException("Unable to locate packet");
		}

		packet.read(in);
		if (in.readableBytes() > 0) {
			throw new IOException(String.format("Packet \"%s\" has unreaded bytes", packet.getClass().getSimpleName()));
		}

		out.add(packet);
	}
}