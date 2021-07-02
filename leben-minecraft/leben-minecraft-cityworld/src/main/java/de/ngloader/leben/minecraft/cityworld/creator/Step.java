package de.ngloader.leben.minecraft.cityworld.creator;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

class Step {

	private Location teleportTo;

	CharacterCreator creator;

	public Step(CharacterCreator creator, Location teleportTo) {
		this.creator = creator;
		this.teleportTo = teleportTo;

		if (this instanceof Listener) {
			Bukkit.getPluginManager().registerEvents((Listener) this, this.creator.plugin);
		}

		this.teleportToStartPoint();
	}

	void teleportToStartPoint() {
		this.creator.player.teleport(this.teleportTo);
	}

	void destroy() {
		if (this instanceof Listener) {
			HandlerList.unregisterAll((Listener) this);
		}
	}
}