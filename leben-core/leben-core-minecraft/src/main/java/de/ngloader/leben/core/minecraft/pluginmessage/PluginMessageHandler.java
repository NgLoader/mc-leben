package de.ngloader.leben.core.minecraft.pluginmessage;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import de.ngloader.leben.core.minecraft.MCCore;
import de.ngloader.leben.core.minecraft.MCModule;
import de.ngloader.leben.network.packet.EnumPacketDirection;
import de.ngloader.leben.network.packet.Packet;
import de.ngloader.leben.network.packet.PacketCodecAbstract;
import de.ngloader.leben.network.packet.PacketHandler;
import de.ngloader.leben.network.packet.PacketRegistry;
import de.ngloader.leben.network.packet.bungee.BungeeRegistry;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class PluginMessageHandler extends MCModule implements PluginMessageListener {

	private static final Logger LOGGER = Logger.getLogger(PluginMessageHandler.class.getSimpleName());

	private static final String MESSAGE_CHANNEL = "leben:default";

	private final PacketRegistry packetRegistry;
	private final PacketCodecAbstract packetCodec;

	private final PacketHandler packetHandler;

	public PluginMessageHandler(MCCore plugin) {
		super(plugin);

		this.packetCodec = new PacketCodecAbstract(EnumPacketDirection.CLIENT, EnumPacketDirection.SERVER);
		this.packetRegistry = new BungeeRegistry();

		this.packetHandler = new StandartPacketHandler(this);

		Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(this.plugin, MESSAGE_CHANNEL);
		Bukkit.getServer().getMessenger().registerIncomingPluginChannel(this.plugin, MESSAGE_CHANNEL, this);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] data) {
		if (!channel.equals(MESSAGE_CHANNEL)) {
			return;
		}

		try {
			Packet<? extends PacketHandler> packet = this.packetCodec.decode(this.packetRegistry, Unpooled.wrappedBuffer(data));
			((Packet<PacketHandler>) packet).handle(this.packetHandler);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error by handling packet", e);
		}
	}

	public void sendPacket(Player player, Packet<?> packet) {
		try {
			if (!player.isOnline()) {
				return;
			}

			ByteBuf buffer = Unpooled.buffer();
			this.packetCodec.encode(this.packetRegistry, packet, buffer);
			player.sendPluginMessage(this.plugin, "Leben", buffer.array());
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error by sending packet", e);
		}
	}
}