package de.ngloader.leben.core.minecraft.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class EventUtil implements Listener {

	private static final Set<Class<? extends Event>> EVENT_CANCELLED = Collections.newSetFromMap(new ConcurrentHashMap<>());
	private static final Map<Class<? extends Event>, Function<Event, Entity>> EVENT_GET_ENTITY = new ConcurrentHashMap<>();
	private static final Map<Class<? extends Event>, Set<UUID>> DISABLED_EVENT = new ConcurrentHashMap<>();

	public static void addCancelledEvent(Plugin plugin, Listener listener, Class<? extends Event> eventClass, String getEntityMethod, Entity player) {
		addCancelledEvent(plugin, listener, eventClass, eventClass, getEntityMethod, player.getUniqueId());
	}

	public static void addCancelledEvent(Plugin plugin, Listener listener, Class<? extends Event> eventClass, String getEntityMethod, UUID uuid) {
		addCancelledEvent(plugin, listener, eventClass, eventClass, getEntityMethod, uuid);
	}

	public static void addCancelledEvent(Plugin plugin, Listener listener, Class<? extends Event> eventClass, Class<? extends Event> getEntityClass, String getEntityMethod, Entity player) {
		addCancelledEvent(plugin, listener, eventClass, eventClass, getEntityMethod, player.getUniqueId());
	}

	public static void addCancelledEvent(Plugin plugin, Listener listener, Class<? extends Event> eventClass, Class<? extends Event> getEntityClass, String getEntityMethod, UUID uuid) {
		if (!Cancellable.class.isAssignableFrom(eventClass)) {
			return;
		}

		try {
			Method method = getEntityClass.getDeclaredMethod(getEntityMethod);

			if (EVENT_CANCELLED.add(eventClass)) {
				Bukkit.getPluginManager().registerEvent(eventClass, listener, EventPriority.NORMAL, EventUtil::onDisableEvent, plugin, true);
			}

			if (!EVENT_GET_ENTITY.containsKey(eventClass)) {
				EVENT_GET_ENTITY.put(eventClass, (event) -> {
					try {
						return (Entity) method.invoke(event);
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						e.printStackTrace();
					}
					return null;
				});
			}
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}

		Set<UUID> disabledPlayer = DISABLED_EVENT.get(eventClass);
		if (disabledPlayer == null) {
			disabledPlayer = Collections.newSetFromMap(new ConcurrentHashMap<>());
			DISABLED_EVENT.put(eventClass, disabledPlayer);
		}
		disabledPlayer.add(uuid);
	}

	public static void removeCancelledEvent(Class<? extends Event> eventClass, Entity entity) {
		removeCancelledEvent(eventClass, entity.getUniqueId());
	}

	public static void removeCancelledEvent(Class<? extends Event> eventClass, UUID uuid) {
		Set<UUID> disabledPlayer = DISABLED_EVENT.get(eventClass);
		if (disabledPlayer != null) {
			disabledPlayer.remove(uuid);

			if (disabledPlayer.isEmpty()) {
				DISABLED_EVENT.remove(eventClass);
			}
		}
	}

	private static void onDisableEvent(Listener listener, Event event) {
		Function<Event, Entity> getEntity = EVENT_GET_ENTITY.get(event.getClass());
		if (getEntity != null) {
			Entity entity = getEntity.apply(event);
			Set<UUID> disabledPlayer = DISABLED_EVENT.get(event.getClass());
			if (entity != null && disabledPlayer != null && disabledPlayer.contains(entity.getUniqueId())) {
				((Cancellable) event).setCancelled(true);
			}
		}
	}

	private Plugin plugin;

	private final Set<CancelledEvent> cancelledEvents = new HashSet<>();
	private final Set<UUID> cancelledEntitys = new HashSet<>();

	private final Set<Listener> listeners = new HashSet<>();

	public EventUtil() { }

	public EventUtil(Plugin plugin) {
		this.plugin = plugin;
	}

	public void register(Listener listener) {
		if (this.listeners.add(listener)) {
			Bukkit.getPluginManager().registerEvents(listener, this.plugin);
		}
	}

	public void unregister(Listener listener) {
		if (this.listeners.remove(listener)) {
			HandlerList.unregisterAll(listener);
		}
	}

	public void addCancelledEvent(Class<? extends Event> eventClass, String getPlayer) {
		this.addCancelledEvent(eventClass, eventClass, getPlayer);
	}

	public void addCancelledEvent(Class<? extends Event> eventClass, Class<? extends Event> getEntityClass, String getPlayer) {
		for (CancelledEvent cancelled : this.cancelledEvents) {
			if (cancelled.eventClass.equals(eventClass)) {
				return;
			}
		}

		this.cancelledEvents.add(new CancelledEvent(eventClass, getEntityClass, getPlayer));
	}

	public void removeCancelledEvent(Class<? extends Event> eventClass) {
		for (CancelledEvent cancelled : this.cancelledEvents) {
			if (cancelled.eventClass.equals(eventClass)) {
				this.cancelledEvents.remove(cancelled);
				break;
			}
		}
	}

	public void addEntity(Entity entity) {
		this.addUUID(entity.getUniqueId());
	}

	public void addUUID(UUID uuid) {
		if (this.cancelledEntitys.add(uuid)) {
			for (CancelledEvent cancelled : this.cancelledEvents) {
				EventUtil.addCancelledEvent(this.plugin, this, cancelled.eventClass, cancelled.getEntityClass, cancelled.getPlayer, uuid);
			}
		}
	}

	public void removeEntity(Entity entity) {
		this.removeUUID(entity.getUniqueId());
	}

	public void removeUUID(UUID uuid) {
		if (this.cancelledEntitys.remove(uuid)) {
			for (CancelledEvent cancelled : this.cancelledEvents) {
				EventUtil.removeCancelledEvent(cancelled.eventClass, uuid);
			}
		}
	}

	public void destroy() {
		for (Iterator<Listener> iterator = this.listeners.iterator(); iterator.hasNext();) {
			Listener listener = iterator.next();
			this.unregister(listener);
		}

		for (Iterator<UUID> iterator = this.cancelledEntitys.iterator(); iterator.hasNext();) {
			UUID uuid = iterator.next();
			this.removeUUID(uuid);
			
		}
	}

	public Plugin getPlugin() {
		return this.plugin;
	}

	public void setPlugin(Plugin plugin) {
		this.plugin = plugin;
	}

	private class CancelledEvent {

		Class<? extends Event> eventClass;
		Class<? extends Event> getEntityClass;
		String getPlayer;

		public CancelledEvent(Class<? extends Event> eventClass, Class<? extends Event> getEntityClass,
				String getPlayer) {
			this.eventClass = eventClass;
			this.getEntityClass = getEntityClass;
			this.getPlayer = getPlayer;
		}
	}
}
