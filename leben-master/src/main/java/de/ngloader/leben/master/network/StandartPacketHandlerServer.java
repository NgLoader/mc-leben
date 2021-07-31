package de.ngloader.leben.master.network;

import de.ngloader.leben.network.NetworkManager;
import de.ngloader.leben.network.packet.standart.StandartServerPacketHandler;
import de.ngloader.leben.network.packet.standart.server.SPacketStandartKeepAlive;

public class StandartPacketHandlerServer implements StandartServerPacketHandler {

	protected NetworkManager networkManager;

	@Override
	public void setNetworkManager(NetworkManager manager) {
		this.networkManager = manager;
	}

	@Override
	public void handleKeepAlive(SPacketStandartKeepAlive packet) {
		//TODO handle keepAlive
	}

	@Override
	public void onDisconnect(String reason) {
		System.out.println(String.format("Disconnected. Reason: %s", reason));
	}
}