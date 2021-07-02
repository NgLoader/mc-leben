package de.ngloader.leben.minecraft.cityworld.creator;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import de.ngloader.leben.core.minecraft.player.LebenPlayer;
import de.ngloader.npcsystem.NPC;
import de.ngloader.npcsystem.event.NPCInteractEvent;
import de.ngloader.npcsystem.npc.entity.NPCSheep;
import de.ngloader.npcsystem.property.NPCPropertyValue;

public class TypeStep extends Step implements Listener {

	private static final Location SPAWN_LOCATION = new Location(Bukkit.getWorld("creating"), 191.5, 154.2, 67.5, 90, 0);
	private static final Location MALE_LOCATION = new Location(Bukkit.getWorld("creating"), 185.5, 155, 53.5, 0, 0);
	private static final Location FEMALE_LOCATION = new Location(Bukkit.getWorld("creating"), 185.5, 154, 81.5, 180, 0);

	private static final NPCSheep NPC_MALE = new NPCSheep(CharacterCreator.NPC_REGISTRY, MALE_LOCATION);
	private static final NPCSheep NPC_FEMALE = new NPCSheep(CharacterCreator.NPC_REGISTRY, FEMALE_LOCATION);

	static {
		NPC_MALE.getProperties().set(NPCPropertyValue.NPC_LOOK_GLOBAL.getKey(), false);
		NPC_FEMALE.getProperties().set(NPCPropertyValue.NPC_LOOK_GLOBAL.getKey(), false);
	}

	public TypeStep(CharacterCreator creator) {
		super(creator, SPAWN_LOCATION);

		LebenPlayer player = this.creator.player;
		NPC_MALE.show(player);
		NPC_FEMALE.show(player);
	}

	@EventHandler
	public void onNpcHit(NPCInteractEvent event) {
		if (event.getPlayer().getUniqueId().equals(this.creator.player.getUniqueId())) {
			NPC npc = event.getNPC();
			if (npc.equals(NPC_MALE)) {
				//this.character set male
				this.creator.nextStep();
			} else if (npc.equals(NPC_FEMALE)) {
				//this.character set female
				this.creator.nextStep();
			} else {
				//Handle error
			}
		}
	}

	@Override
	void destroy() {
		LebenPlayer player = this.creator.player;
		NPC_MALE.hide(player);
		NPC_FEMALE.hide(player);
	}
}