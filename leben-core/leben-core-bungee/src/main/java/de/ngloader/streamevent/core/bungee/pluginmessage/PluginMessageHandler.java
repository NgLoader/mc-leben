package de.ngloader.streamevent.core.bungee.pluginmessage;

import java.util.logging.Level;
import java.util.logging.Logger;

import de.ngloader.streamevent.core.bungee.ProxyCore;
import de.ngloader.streamevent.core.bungee.ProxyModule;
import de.ngloader.streamevent.network.packet.EnumPacketDirection;
import de.ngloader.streamevent.network.packet.Packet;
import de.ngloader.streamevent.network.packet.PacketCodecAbstract;
import de.ngloader.streamevent.network.packet.PacketHandler;
import de.ngloader.streamevent.network.packet.PacketRegistry;
import de.ngloader.streamevent.network.packet.bungee.BungeeRegistry;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PluginMessageHandler extends ProxyModule implements Listener {

	private static final Logger LOGGER = Logger.getLogger(PluginMessageHandler.class.getSimpleName());

	private static final String MESSAGE_CHANNEL = "Leben";

	private final PacketRegistry packetRegistry;
	private final PacketCodecAbstract packetCodec;

	private final PacketHandler packetHandler;

	public PluginMessageHandler(ProxyCore plugin) {
		super(plugin);

		this.packetCodec = new PacketCodecAbstract(EnumPacketDirection.CLIENT, EnumPacketDirection.SERVER);
		this.packetRegistry = new BungeeRegistry();

		this.packetHandler = new StandartPacketHandler(this);

		ProxyServer.getInstance().registerChannel(MESSAGE_CHANNEL);
	}

	@SuppressWarnings("unchecked")
	@EventHandler
	public void onPluginMessage(PluginMessageEvent event) {
		if (!event.getTag().equals(MESSAGE_CHANNEL)) {
			return;
		}

		try {
			Packet<? extends PacketHandler> packet = this.packetCodec.decode(this.packetRegistry, Unpooled.wrappedBuffer(event.getData()));
			((Packet<PacketHandler>) packet).handle(this.packetHandler);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error by handling packet", e);
		}
	}

	public void sendPacket(ProxiedPlayer player, Packet<?> packet) {
		try {
			if (!player.isConnected()) {
				return;
			}

			ByteBuf buffer = Unpooled.buffer();
			this.packetCodec.encode(this.packetRegistry, packet, buffer);
			player.sendData(MESSAGE_CHANNEL, buffer.array());
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error by sending packet", e);
		}
	}
}