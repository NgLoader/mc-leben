package de.ngloader.streamevent.core.minecraft;

import org.bukkit.event.Listener;

public class MCModule {

	protected MCCore plugin;

	public MCModule(MCCore plugin) {
		this.plugin = plugin;
	}

	public void registerListeners(Listener... listeners) {
		this.plugin.registerEvents(this, listeners);
	}

	public void unregisterListeners(Listener... listeners) {
		this.plugin.unregisterEvents(this, listeners);
	}

	public void destroy() {
		this.plugin.unregisterEvents(this);
	}

	public MCCore getPlugin() {
		return this.plugin;
	}
}