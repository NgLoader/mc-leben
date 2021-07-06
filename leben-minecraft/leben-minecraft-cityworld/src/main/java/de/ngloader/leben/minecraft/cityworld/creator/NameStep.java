package de.ngloader.leben.minecraft.cityworld.creator;

import java.util.concurrent.atomic.AtomicBoolean;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class NameStep extends Step implements Listener {

	private static final Location SPAWN_LOCATION = new Location(Bukkit.getWorld("creating"), 122.5, 136.2, 83.5, 0, 0);

	private AtomicBoolean finish = new AtomicBoolean();

	public NameStep(CharacterCreator creator) {
		super(creator, SPAWN_LOCATION);

		this.creator.player.sendMessage("§4§lBitte schreiben deinen gewünschten namen in chat.");
	}

	@EventHandler
	public void onChatEvent(AsyncPlayerChatEvent event) {
		if (this.finish.get() || !event.getPlayer().getUniqueId().equals(this.creator.player.getUniqueId())) {
			return;
		}

		String message = event.getMessage().trim();
		if (message.length() > 4 && message.length() < 16) {
			this.finish.set(true);

			this.creator.player.sendMessage("DEBUG: " + message);
			Bukkit.getScheduler().runTask(this.creator.plugin,  this.creator::nextStep);
			return;
		}

		this.creator.player.sendMessage("Bitte wähle einen namen zwischen 4-16 zeichen aus!");
	}
}