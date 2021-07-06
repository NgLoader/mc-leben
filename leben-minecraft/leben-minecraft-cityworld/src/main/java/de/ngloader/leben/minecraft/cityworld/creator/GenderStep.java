package de.ngloader.leben.minecraft.cityworld.creator;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import de.ngloader.leben.core.minecraft.player.LebenPlayer;
import de.ngloader.npcsystem.NPC;
import de.ngloader.npcsystem.event.NPCInteractEvent;
import de.ngloader.npcsystem.npc.entity.NPCPlayer;
import de.ngloader.npcsystem.property.NPCPropertyValue;
import de.ngloader.npcsystem.runner.NPCRunnerType;

public class GenderStep extends Step implements Listener {

	private static final Location SPAWN_LOCATION = new Location(Bukkit.getWorld("creating"), 191.5, 154.2, 67.5, 90, 0);
	private static final Location MALE_LOCATION = new Location(Bukkit.getWorld("creating"), 185.5, 154, 53.5, 0, 0);
	private static final Location FEMALE_LOCATION = new Location(Bukkit.getWorld("creating"), 185.5, 154, 81.5, 180, 0);

	private static final NPCPlayer NPC_MALE = new NPCPlayer(CharacterCreator.NPC_REGISTRY, "Male", MALE_LOCATION).create();
	private static final NPCPlayer NPC_FEMALE = new NPCPlayer(CharacterCreator.NPC_REGISTRY, "Female", FEMALE_LOCATION).create();

	static {
		NPC_MALE.addToRunner(NPCRunnerType.DISTANCE_CHECK, NPCRunnerType.LOOK);
		NPC_MALE.getProperties().set(NPCPropertyValue.NPC_LOOK_GLOBAL.getKey(), false);
		NPC_MALE.getProperties().set(NPCPropertyValue.NPC_LOOK_RANGE.getKey(), 2);
		NPC_MALE.setTextures(
				"ewogICJ0aW1lc3RhbXAiIDogMTYyNTIyODY4NzcwNywKICAicHJvZmlsZUlkIiA6ICJjYTRiNmEyNGIyODI0YTdhYjYxYjY5YmYzOGYxZGIxNCIsCiAgInByb2ZpbGVOYW1lIiA6ICJOaWxzc01pbmVyOTkiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTA3OTdkYjIzMzZiZDE4NTUxZDUzOTQ1NWIyYjY5OTVhMTRlYzM1MDg1NDI3NGJlNTdlYjZjZjFmN2NiMGFlMyIKICAgIH0KICB9Cn0=",
				"WZP56U/llW1cbLinwarlvx2aDErk+sDDh/+4swSAouEKFgCNswO1BcVIuJC+4fv8K1KxP16h5yI+09NIz4nCdb6u8YBdxFLxsodI0OXkmeG+cVuycO/S/OJiC70td85j1PK3yLukTZ80CAnXT/eCo9k5jVgABSeZlGcZo1cu9RTdWwqLxRtyiJ1Gv/v22wToLSSwePIxX+T2J9Em8wht8CHtWTyOVyHWAsXBK0nYIRe+voVWEo+oYe/UpCTHZjJk7IvqMiE1AXstKqdhDCw42IRHQr3fNRPSdVjbhkKeTVk75QrIqxRlaqyLFyGNCXhDdr8U5NQq9liDf+G+pP3Lwc7PEtu7Tf+Oa4MxqxfViZg52KzZa+CB4S3V868mD3bHD0wVwd2fbjSO/SeIUKEfQc/8YRjyLZeyXoqO+qXujSq8jNZMCkx3pzk7vKAQ97pomIBWU9By4G/bebYwim+mqFCmDCiAF+MHYJrSPwbSgiu5o69QKv3YjIplYSsCSTbEAzCdXPdwl9YbqUi67EmNdZFWqTMwPl5NlEUZdJfSCgdOYe8wql1Z6U92lgDcd5pd8mDBkvbLNNfXm2k0sg0PLKJ+Dv3HoSkR9f1X9LtXg2saCZCUr1Jw+am2qzIWIvkja2nAOyouAU9KQV2Lg1fZHbSvmIR0+OhkBePRfpj1SEA=");

		NPC_FEMALE.addToRunner(NPCRunnerType.DISTANCE_CHECK, NPCRunnerType.LOOK);
		NPC_FEMALE.getProperties().set(NPCPropertyValue.NPC_LOOK_GLOBAL.getKey(), false);
		NPC_FEMALE.getProperties().set(NPCPropertyValue.NPC_LOOK_RANGE.getKey(), 2);
		NPC_FEMALE.setTextures(
				"ewogICJ0aW1lc3RhbXAiIDogMTYyNTIyODY4NzcwNywKICAicHJvZmlsZUlkIiA6ICJjYTRiNmEyNGIyODI0YTdhYjYxYjY5YmYzOGYxZGIxNCIsCiAgInByb2ZpbGVOYW1lIiA6ICJOaWxzc01pbmVyOTkiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTA3OTdkYjIzMzZiZDE4NTUxZDUzOTQ1NWIyYjY5OTVhMTRlYzM1MDg1NDI3NGJlNTdlYjZjZjFmN2NiMGFlMyIKICAgIH0KICB9Cn0=",
				"WZP56U/llW1cbLinwarlvx2aDErk+sDDh/+4swSAouEKFgCNswO1BcVIuJC+4fv8K1KxP16h5yI+09NIz4nCdb6u8YBdxFLxsodI0OXkmeG+cVuycO/S/OJiC70td85j1PK3yLukTZ80CAnXT/eCo9k5jVgABSeZlGcZo1cu9RTdWwqLxRtyiJ1Gv/v22wToLSSwePIxX+T2J9Em8wht8CHtWTyOVyHWAsXBK0nYIRe+voVWEo+oYe/UpCTHZjJk7IvqMiE1AXstKqdhDCw42IRHQr3fNRPSdVjbhkKeTVk75QrIqxRlaqyLFyGNCXhDdr8U5NQq9liDf+G+pP3Lwc7PEtu7Tf+Oa4MxqxfViZg52KzZa+CB4S3V868mD3bHD0wVwd2fbjSO/SeIUKEfQc/8YRjyLZeyXoqO+qXujSq8jNZMCkx3pzk7vKAQ97pomIBWU9By4G/bebYwim+mqFCmDCiAF+MHYJrSPwbSgiu5o69QKv3YjIplYSsCSTbEAzCdXPdwl9YbqUi67EmNdZFWqTMwPl5NlEUZdJfSCgdOYe8wql1Z6U92lgDcd5pd8mDBkvbLNNfXm2k0sg0PLKJ+Dv3HoSkR9f1X9LtXg2saCZCUr1Jw+am2qzIWIvkja2nAOyouAU9KQV2Lg1fZHbSvmIR0+OhkBePRfpj1SEA=");
	}

	private boolean found = false;

	public GenderStep(CharacterCreator creator) {
		super(creator, SPAWN_LOCATION);

		LebenPlayer player = this.creator.player;
		NPC_MALE.show(player);
		NPC_FEMALE.show(player);
	}

	@EventHandler
	public void onNpcHit(NPCInteractEvent event) {
		if (found || !event.getPlayer().getUniqueId().equals(this.creator.player.getUniqueId())) {
			return;
		}

		NPC npc = event.getNPC();
		if (npc.equals(NPC_MALE)) {
			//this.character set male
			this.found = true;
			this.creator.player.sendMessage("DEBUG: male!");
			this.creator.nextStep();
		} else if (npc.equals(NPC_FEMALE)) {
			//this.character set female
			this.found = true;
			this.creator.player.sendMessage("DEBUG: female!");
			this.creator.nextStep();
		} else {
			//Handle error
		}
	}

	@Override
	void destroy() {
		super.destroy();
		LebenPlayer player = this.creator.player;
		NPC_MALE.hide(player);
		NPC_FEMALE.hide(player);
	}
}