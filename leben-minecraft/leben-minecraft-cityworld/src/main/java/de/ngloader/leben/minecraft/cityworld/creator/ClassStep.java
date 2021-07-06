package de.ngloader.leben.minecraft.cityworld.creator;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import de.ngloader.npcsystem.NPC;
import de.ngloader.npcsystem.event.NPCInteractEvent;
import de.ngloader.npcsystem.npc.entity.NPCPlayer;
import de.ngloader.npcsystem.property.NPCProperties;
import de.ngloader.npcsystem.property.NPCPropertyValue;
import de.ngloader.npcsystem.runner.NPCRunnerType;

public class ClassStep extends Step implements Listener {

	private static final Location SPAWN_LOCATION = new Location(Bukkit.getWorld("creating"), -28.5, 123.2, 217.5, 0, 0);

	private static final Set<NPCPlayer> NPCS = new HashSet<>();

	static {
		for (CharacterClass characterClass : CharacterClass.values()) {
			NPCPlayer npc = new NPCPlayer(CharacterCreator.NPC_REGISTRY, characterClass.name(), characterClass.location);
			npc.addToRunner(NPCRunnerType.DISTANCE_CHECK, NPCRunnerType.LOOK);
			npc.getProperties().set(NPCPropertyValue.NPC_LOOK_GLOBAL.getKey(), false);
			npc.getProperties().set(NPCPropertyValue.NPC_LOOK_RANGE.getKey(), 2);
			npc.getProperties().set("characterClass", characterClass);
			npc.setTextures(
					"ewogICJ0aW1lc3RhbXAiIDogMTYyNTIyODY4NzcwNywKICAicHJvZmlsZUlkIiA6ICJjYTRiNmEyNGIyODI0YTdhYjYxYjY5YmYzOGYxZGIxNCIsCiAgInByb2ZpbGVOYW1lIiA6ICJOaWxzc01pbmVyOTkiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTA3OTdkYjIzMzZiZDE4NTUxZDUzOTQ1NWIyYjY5OTVhMTRlYzM1MDg1NDI3NGJlNTdlYjZjZjFmN2NiMGFlMyIKICAgIH0KICB9Cn0=",
					"WZP56U/llW1cbLinwarlvx2aDErk+sDDh/+4swSAouEKFgCNswO1BcVIuJC+4fv8K1KxP16h5yI+09NIz4nCdb6u8YBdxFLxsodI0OXkmeG+cVuycO/S/OJiC70td85j1PK3yLukTZ80CAnXT/eCo9k5jVgABSeZlGcZo1cu9RTdWwqLxRtyiJ1Gv/v22wToLSSwePIxX+T2J9Em8wht8CHtWTyOVyHWAsXBK0nYIRe+voVWEo+oYe/UpCTHZjJk7IvqMiE1AXstKqdhDCw42IRHQr3fNRPSdVjbhkKeTVk75QrIqxRlaqyLFyGNCXhDdr8U5NQq9liDf+G+pP3Lwc7PEtu7Tf+Oa4MxqxfViZg52KzZa+CB4S3V868mD3bHD0wVwd2fbjSO/SeIUKEfQc/8YRjyLZeyXoqO+qXujSq8jNZMCkx3pzk7vKAQ97pomIBWU9By4G/bebYwim+mqFCmDCiAF+MHYJrSPwbSgiu5o69QKv3YjIplYSsCSTbEAzCdXPdwl9YbqUi67EmNdZFWqTMwPl5NlEUZdJfSCgdOYe8wql1Z6U92lgDcd5pd8mDBkvbLNNfXm2k0sg0PLKJ+Dv3HoSkR9f1X9LtXg2saCZCUr1Jw+am2qzIWIvkja2nAOyouAU9KQV2Lg1fZHbSvmIR0+OhkBePRfpj1SEA=");
			npc.create();

			NPCS.add(npc);
		}
	}

	private boolean found = false;

	public ClassStep(CharacterCreator creator) {
		super(creator, SPAWN_LOCATION);

		NPCS.forEach(npc -> npc.show(this.creator.player));
	}

	@EventHandler
	public void onNpcHit(NPCInteractEvent event) {
		if (found || !event.getPlayer().getUniqueId().equals(this.creator.player.getUniqueId())) {
			return;
		}

		NPC npc = event.getNPC();
		NPCProperties properties = npc.getProperties();

		if (properties.has("characterClass")) {
			this.found = true;

			CharacterClass characterClass = properties.get("characterClass", CharacterClass.HEALER, CharacterClass.class);
			if (characterClass != null) {
				this.creator.player.sendMessage("DEBUG: " + characterClass.name());
				this.creator.nextStep();
			}
		}
	}

	@Override
	void destroy() {
		super.destroy();
		NPCS.forEach(npc -> npc.hide(this.creator.player));
	}

	private enum CharacterClass {
		SMITE(-29, 122, 241),
		WARRIOR(-48, 122, 236),
		THIEF(-57, 122, 217),
		HEALER(-48, 122, 198),
		NINJA(-29, 122, 187),
		WITCHER(-9, 122, 197),
		SHAMAN(-1, 122, 217),
		HUNTER(-9, 122, 237);

		private final Location location;

		private CharacterClass(double x, double y, double z) {
			this.location = new Location(Bukkit.getWorld("creating"), x, y, z, 0, 0);
		}
	}
}