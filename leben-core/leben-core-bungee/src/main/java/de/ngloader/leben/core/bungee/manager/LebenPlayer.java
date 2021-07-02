package de.ngloader.leben.core.bungee.manager;

import net.md_5.bungee.api.connection.ProxiedPlayer;

public class LebenPlayer {

	private ProxiedPlayer player;

	private byte[] playerData;
	private byte[] customData;

	public LebenPlayer(ProxiedPlayer player) {
		this.player = player;
	}

	public byte[] getPlayerData() {
		return this.playerData;
	}

	public void setPlayerData(byte[] data) {
		this.playerData = data;
	}

	public byte[] getCustomData() {
		return this.customData;
	}

	public void setCustomData(byte[] customData) {
		this.customData = customData;
	}

	public ProxiedPlayer getPlayer() {
		return this.player;
	}
}