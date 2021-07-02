package de.ngloader.leben.core.minecraft.player;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_17_R1.CraftServer;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import de.ngloader.leben.core.minecraft.util.NBTUtil;

public class LebenPlayer extends CraftPlayer {

	private final long connectionTime = System.currentTimeMillis();

	public LebenPlayer(Player player) {
		super((CraftServer) Bukkit.getServer(), ((CraftPlayer) player).getHandle());
	}

	public void loadPlayerData(byte[] data) {
		this.getHandle().load(NBTUtil.binaryToNBT(data));
	}

	public byte[] savePlayerData() {
		return NBTUtil.nbtToBinary(this.save());
	}

	public long getConnectionTime() {
		return this.connectionTime;
	}
}