package de.ngloader.leben.core.minecraft.item;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

import de.ngloader.leben.core.minecraft.MCCore;
import de.ngloader.leben.core.minecraft.MCModule;

public class ItemManager extends MCModule implements Listener {

	private Map<Integer, CustomItem> items = new ConcurrentHashMap<>();

	public ItemManager(MCCore plugin) {
		super(plugin);

		this.registerListeners(this);
	}

	@EventHandler
	public void onEntityPickupItem(EntityPickupItemEvent event) {
		
	}
}
