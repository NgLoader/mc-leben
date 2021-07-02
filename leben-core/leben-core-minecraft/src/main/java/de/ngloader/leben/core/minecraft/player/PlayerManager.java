package de.ngloader.leben.core.minecraft.player;

import java.util.HashSet;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.ngloader.leben.core.minecraft.MCCore;
import de.ngloader.leben.core.minecraft.event.LebenPlayerJoinEvent;
import de.ngloader.leben.core.minecraft.pluginmessage.PluginMessageHandler;
import de.ngloader.leben.core.minecraft.util.DoubleConsumer;
import de.ngloader.leben.network.packet.bungee.server.SPacketBungeeInventoryLoad;

public class PlayerManager implements Listener {

	private static final Logger LOGGER = Logger.getLogger(PlayerManager.class.getSimpleName());

	private final HashSet<Player> joining = new HashSet<>();
	private final HashSet<Player> leaving = new HashSet<>();

	private final ReadWriteLock connectionLock = new ReentrantReadWriteLock();
	private final Map<UUID, DoubleConsumer<LebenPlayer, byte[]>> connectionHandle = new WeakHashMap<>();

	private final Map<UUID, LebenPlayer> loadedPlayer = new WeakHashMap<>();

	private final MCCore plugin;
	private final PluginMessageHandler pluginMessageHandler;

	public PlayerManager(MCCore plugin) {
		this.plugin = plugin;
		this.pluginMessageHandler = plugin.getPluginMessageHandler();

		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		this.joining.add(player);

		player.getInventory().clear();

		try {
		} catch (Exception e) {
			this.quitPlayerWhileJoin(player, PlayerDisconnectReason.PLAYER_JOIN_FAILURE, "Error by loading player data!", e);
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		this.joining.remove(player);
		this.leaving.remove(player);
	}

	private void loadBackendPlayer(Player player) {
		LebenPlayer lebenPlayer = new LebenPlayer(player);

		try {
			this.connectionLock.readLock().lock();
			DoubleConsumer<LebenPlayer, byte[]> consumer = this.connectionHandle.get(player.getUniqueId());
	
			if (consumer == null) {
				try {
					this.connectionLock.writeLock().lock();
					consumer = new DoubleConsumer<LebenPlayer, byte[]>(lebenPlayer, null, this::handleConnect);
					this.connectionHandle.put(player.getUniqueId(), consumer);
				} finally {
					this.connectionLock.writeLock().unlock();
				}
				return;
			}

			consumer.acceptFirst(lebenPlayer);
		} finally {
			this.connectionLock.readLock().unlock();
		}
	}

	public void loadBungeePlayer(Player player, byte[] data) {
		try {
			this.connectionLock.readLock().lock();
			DoubleConsumer<LebenPlayer, byte[]> consumer = this.connectionHandle.get(player.getUniqueId());
	
			if (consumer == null) {
				try {
					this.connectionLock.writeLock().lock();
					consumer = new DoubleConsumer<LebenPlayer, byte[]>(null, data, this::handleConnect);
					this.connectionHandle.put(player.getUniqueId(), consumer);
				} finally {
					this.connectionLock.writeLock().unlock();
				}
				return;
			}

			consumer.acceptSecond(data);
		} finally {
			this.connectionLock.readLock().unlock();
		}
	}

	private void handleConnect(LebenPlayer player, byte[] data) {
		try {
			this.connectionLock.writeLock().lock();
			this.connectionHandle.remove(player.getUniqueId());
		} finally {
			this.connectionLock.writeLock().unlock();
		}

		player.loadPlayerData(data);

		Bukkit.getPluginManager().callEvent(new LebenPlayerJoinEvent(player));
	}

	public void quitPlayer(Player player) {
		this.leaving.add(player);
		this.joining.remove(player);

		LebenPlayer lebenPlayer = this.loadedPlayer.remove(player.getUniqueId());
		this.pluginMessageHandler.sendPacket(player, new SPacketBungeeInventoryLoad(player.getUniqueId(), lebenPlayer != null ? lebenPlayer.savePlayerData() : null));
	}

	private void quitPlayerWhileJoin(Player player, PlayerDisconnectReason reason, String message, Exception error) {
		player.kickPlayer("Error! Reason: " + reason.name());
		LOGGER.log(Level.SEVERE, message, error);

		this.leaving.remove(player);
		this.joining.remove(player);

		try {
			this.connectionLock.writeLock().lock();
			this.connectionHandle.remove(player.getUniqueId());
		} finally {
			this.connectionLock.writeLock().unlock();
		}

		this.quitPlayer(player);
	}
}
