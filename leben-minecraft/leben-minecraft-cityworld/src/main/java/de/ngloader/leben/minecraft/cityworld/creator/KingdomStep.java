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

public class KingdomStep extends Step implements Listener {

	private static final Location SPAWN_LOCATION = new Location(Bukkit.getWorld("creating"), 94.5, 209.2, 145.5, 90, 0);
	private static final Location RIMA_LOCATION = new Location(Bukkit.getWorld("creating"), 78.8, 209, 157, -140, 0);
	private static final Location KARENA_LOCATION = new Location(Bukkit.getWorld("creating"), 78.5, 209, 134, -74, 0);

	private static final NPCPlayer NPC_RIMA = new NPCPlayer(CharacterCreator.NPC_REGISTRY, "Rima", RIMA_LOCATION).create();
	private static final NPCPlayer NPC_KARENA = new NPCPlayer(CharacterCreator.NPC_REGISTRY, "Karena", KARENA_LOCATION).create();

	static {
		NPC_RIMA.addToRunner(NPCRunnerType.DISTANCE_CHECK, NPCRunnerType.LOOK);
		NPC_RIMA.getProperties().set(NPCPropertyValue.NPC_LOOK_GLOBAL.getKey(), false);
		NPC_RIMA.getProperties().set(NPCPropertyValue.NPC_LOOK_RANGE.getKey(), 2);
		NPC_RIMA.setTextures(
				"ewogICJ0aW1lc3RhbXAiIDogMTYyNTIyODY4NzcwNywKICAicHJvZmlsZUlkIiA6ICJjYTRiNmEyNGIyODI0YTdhYjYxYjY5YmYzOGYxZGIxNCIsCiAgInByb2ZpbGVOYW1lIiA6ICJOaWxzc01pbmVyOTkiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTA3OTdkYjIzMzZiZDE4NTUxZDUzOTQ1NWIyYjY5OTVhMTRlYzM1MDg1NDI3NGJlNTdlYjZjZjFmN2NiMGFlMyIKICAgIH0KICB9Cn0=",
				"WZP56U/llW1cbLinwarlvx2aDErk+sDDh/+4swSAouEKFgCNswO1BcVIuJC+4fv8K1KxP16h5yI+09NIz4nCdb6u8YBdxFLxsodI0OXkmeG+cVuycO/S/OJiC70td85j1PK3yLukTZ80CAnXT/eCo9k5jVgABSeZlGcZo1cu9RTdWwqLxRtyiJ1Gv/v22wToLSSwePIxX+T2J9Em8wht8CHtWTyOVyHWAsXBK0nYIRe+voVWEo+oYe/UpCTHZjJk7IvqMiE1AXstKqdhDCw42IRHQr3fNRPSdVjbhkKeTVk75QrIqxRlaqyLFyGNCXhDdr8U5NQq9liDf+G+pP3Lwc7PEtu7Tf+Oa4MxqxfViZg52KzZa+CB4S3V868mD3bHD0wVwd2fbjSO/SeIUKEfQc/8YRjyLZeyXoqO+qXujSq8jNZMCkx3pzk7vKAQ97pomIBWU9By4G/bebYwim+mqFCmDCiAF+MHYJrSPwbSgiu5o69QKv3YjIplYSsCSTbEAzCdXPdwl9YbqUi67EmNdZFWqTMwPl5NlEUZdJfSCgdOYe8wql1Z6U92lgDcd5pd8mDBkvbLNNfXm2k0sg0PLKJ+Dv3HoSkR9f1X9LtXg2saCZCUr1Jw+am2qzIWIvkja2nAOyouAU9KQV2Lg1fZHbSvmIR0+OhkBePRfpj1SEA=");

		NPC_KARENA.addToRunner(NPCRunnerType.DISTANCE_CHECK, NPCRunnerType.LOOK);
		NPC_KARENA.getProperties().set(NPCPropertyValue.NPC_LOOK_GLOBAL.getKey(), false);
		NPC_KARENA.getProperties().set(NPCPropertyValue.NPC_LOOK_RANGE.getKey(), 2);
		NPC_KARENA.setTextures(
				"ewogICJ0aW1lc3RhbXAiIDogMTYyNTIyODY4NzcwNywKICAicHJvZmlsZUlkIiA6ICJjYTRiNmEyNGIyODI0YTdhYjYxYjY5YmYzOGYxZGIxNCIsCiAgInByb2ZpbGVOYW1lIiA6ICJOaWxzc01pbmVyOTkiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTA3OTdkYjIzMzZiZDE4NTUxZDUzOTQ1NWIyYjY5OTVhMTRlYzM1MDg1NDI3NGJlNTdlYjZjZjFmN2NiMGFlMyIKICAgIH0KICB9Cn0=",
				"WZP56U/llW1cbLinwarlvx2aDErk+sDDh/+4swSAouEKFgCNswO1BcVIuJC+4fv8K1KxP16h5yI+09NIz4nCdb6u8YBdxFLxsodI0OXkmeG+cVuycO/S/OJiC70td85j1PK3yLukTZ80CAnXT/eCo9k5jVgABSeZlGcZo1cu9RTdWwqLxRtyiJ1Gv/v22wToLSSwePIxX+T2J9Em8wht8CHtWTyOVyHWAsXBK0nYIRe+voVWEo+oYe/UpCTHZjJk7IvqMiE1AXstKqdhDCw42IRHQr3fNRPSdVjbhkKeTVk75QrIqxRlaqyLFyGNCXhDdr8U5NQq9liDf+G+pP3Lwc7PEtu7Tf+Oa4MxqxfViZg52KzZa+CB4S3V868mD3bHD0wVwd2fbjSO/SeIUKEfQc/8YRjyLZeyXoqO+qXujSq8jNZMCkx3pzk7vKAQ97pomIBWU9By4G/bebYwim+mqFCmDCiAF+MHYJrSPwbSgiu5o69QKv3YjIplYSsCSTbEAzCdXPdwl9YbqUi67EmNdZFWqTMwPl5NlEUZdJfSCgdOYe8wql1Z6U92lgDcd5pd8mDBkvbLNNfXm2k0sg0PLKJ+Dv3HoSkR9f1X9LtXg2saCZCUr1Jw+am2qzIWIvkja2nAOyouAU9KQV2Lg1fZHbSvmIR0+OhkBePRfpj1SEA=");
	}

	private boolean found = false;

	public KingdomStep(CharacterCreator creator) {
		super(creator, SPAWN_LOCATION);

		LebenPlayer player = this.creator.player;
		NPC_RIMA.show(player);
		NPC_KARENA.show(player);
	}

	@EventHandler
	public void onNpcHit(NPCInteractEvent event) {
		if (found || !event.getPlayer().getUniqueId().equals(this.creator.player.getUniqueId())) {
			return;
		}

		NPC npc = event.getNPC();
		if (npc.equals(NPC_RIMA)) {
			//this.character set rima
			this.found = true;
			this.creator.player.sendMessage("DEBUG: rima!");
			this.creator.nextStep();
		} else if (npc.equals(NPC_KARENA)) {
			//this.character set karena
			this.found = true;
			this.creator.player.sendMessage("DEBUG: karena!");
			this.creator.nextStep();
		} else {
			//Handle error
		}
	}

	@Override
	void destroy() {
		super.destroy();
		LebenPlayer player = this.creator.player;
		NPC_RIMA.hide(player);
		NPC_KARENA.hide(player);
	}
}