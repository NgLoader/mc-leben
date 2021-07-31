package de.ngloader.leben.network.packet;

import io.netty.buffer.ByteBuf;

public interface Packet<T extends PacketHandler> {

	public void read(ByteBuf buffer);
	public void write(ByteBuf buffer);

	public default void handle(T handler) {
		throw new IllegalArgumentException("Method handle(T) is not supported");
	}
}