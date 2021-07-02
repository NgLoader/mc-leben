package de.ngloader.leben.network.packet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.ngloader.leben.network.packet.auth.AuthRegistry;
import de.ngloader.leben.network.packet.bungee.BungeeRegistry;

public class PacketRegistry {

	public static final AuthRegistry AUTH = new AuthRegistry();
	public static final BungeeRegistry BUNGEE = new BungeeRegistry();

	private final Map<Class<? extends Packet<? extends PacketHandler>>, Integer> serverPacketByClass = new HashMap<>();
	private final Map<Integer, Class<? extends Packet<? extends PacketHandler>>> serverPacketById = new HashMap<>();

	private final Map<Class<? extends Packet<? extends PacketHandler>>, Integer> clientPacketByClass = new HashMap<>();
	private final Map<Integer, Class<? extends Packet<? extends PacketHandler>>> clientPacketById = new HashMap<>();

	private Map<Class<? extends Packet<? extends PacketHandler>>, Integer> getPacketByClass(EnumPacketDirection packetDirection) {
		return packetDirection == EnumPacketDirection.SERVER ? this.serverPacketByClass : this.clientPacketByClass;
	}

	private Map<Integer, Class<? extends Packet<? extends PacketHandler>>> getPacketById(EnumPacketDirection packetDirection) {
		return packetDirection == EnumPacketDirection.SERVER ? this.serverPacketById : this.clientPacketById;
	}

	protected void registerPacket(EnumPacketDirection packetDirection, Class<? extends Packet<? extends PacketHandler>> packet) {
		Map<Class<? extends Packet<? extends PacketHandler>>, Integer> byClass = this.getPacketByClass(packetDirection);
		Map<Integer, Class<? extends Packet<? extends PacketHandler>>> byId = this.getPacketById(packetDirection);

		int id = byClass.size();
		while (byClass.containsValue(id) || byId.containsKey(id)) {
			id++;
		}

		byClass.put(packet, id);
		byId.put(id, packet);
	}

	public int getId(EnumPacketDirection packetDirection, Packet<?> packet) {
		return this.getPacketByClass(packetDirection).getOrDefault(packet.getClass(), -1);
	}

	public Packet<?> getPacket(EnumPacketDirection packetDirection, int id) throws IOException, ReflectiveOperationException {
		Class<? extends Packet<? extends PacketHandler>> clazz = this.getPacketById(packetDirection).get(id);
		if (clazz == null) {
			throw new IOException("Packet id not found!");
		}
		return clazz.getConstructor().newInstance();
	}
}