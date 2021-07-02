package de.ngloader.leben.core.bungee;

import net.md_5.bungee.api.plugin.Listener;

public class ProxyModule {

	protected ProxyCore plugin;

	public ProxyModule(ProxyCore plugin) {
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

	public ProxyCore getPlugin() {
		return this.plugin;
	}
}