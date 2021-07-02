package de.ngloader.leben.core.minecraft.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.ngloader.leben.core.minecraft.player.LebenPlayer;

public class LebenPlayerJoinEvent extends Event {

	private static final HandlerList HANDLERS = new HandlerList();
	
	private LebenPlayer player;
	
	public LebenPlayerJoinEvent(LebenPlayer player) {
		this.player = player;
	}

	public LebenPlayer getPlayer() {
		return this.player;
	}

	public HandlerList getHandlers() {
		return HANDLERS;
	}

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}
}