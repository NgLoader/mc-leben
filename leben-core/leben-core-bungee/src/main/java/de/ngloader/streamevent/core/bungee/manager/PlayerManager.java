package de.ngloader.streamevent.core.bungee.manager;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import de.ngloader.streamevent.core.bungee.ProxyCore;
import de.ngloader.streamevent.core.bungee.pluginmessage.PluginMessageHandler;
import de.ngloader.streamevent.network.packet.bungee.client.CPacketBungeePlayerDisconnect;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerConnectEvent.Reason;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PlayerManager implements Listener {

	private final Map<UUID, LebenPlayer> lebenPlayer = new ConcurrentHashMap<>();
	private final Map<UUID, Consumer<UUID>> serverJoin = new ConcurrentHashMap<>();
	private final Map<UUID, ServerInfo> serverChange = new ConcurrentHashMap<>();

	private final PluginMessageHandler pluginMessageHandler;

	public PlayerManager(ProxyCore plugin) {
		this.pluginMessageHandler = plugin.getPluginMessageHandler();
	}

	@EventHandler(priority = 5)
	public void onClientConnect(PreLoginEvent event) {
		UUID uuid = event.getConnection().getUniqueId();
	}

	@EventHandler
	public void onPlayerDisconnect(PlayerDisconnectEvent event) {
		
	}

	@EventHandler(priority = 5)
	public void onServerConnect(ServerConnectEvent event) {
		UUID uuid = event.getPlayer().getUniqueId();

		if (serverJoin.containsKey(uuid)) {
			
		}

		ServerInfo serverInfo = this.serverChange.get(uuid);
		if (serverInfo == null || serverInfo.equals(event.getTarget())) {
			event.setCancelled(true);
		}
	}

	public boolean switchServer(ProxiedPlayer player, ServerInfo target) {
		if (!target.canAccess(player) || player.getServer().getInfo().equals(target)) {
			return false;
		}

		this.serverChange.put(player.getUniqueId(), target);
		this.pluginMessageHandler.sendPacket(player, new CPacketBungeePlayerDisconnect(player.getUniqueId()));
		return true;
	}

	public void reciveData(UUID uuid, byte[] data) {
		LebenPlayer player = this.lebenPlayer.get(uuid);
		if (player == null) {
			return;
		}

		ServerInfo serverInfo = this.serverChange.remove(uuid);
		if (serverInfo != null) {
			player.getPlayer().connect(serverInfo, Reason.PLUGIN);
		}
	}
}
