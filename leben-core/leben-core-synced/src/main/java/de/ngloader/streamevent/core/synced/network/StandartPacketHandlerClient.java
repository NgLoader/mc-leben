package de.ngloader.streamevent.core.synced.network;

import de.ngloader.streamevent.network.NetworkManager;
import de.ngloader.streamevent.network.packet.standart.StandartClientPacketHandler;
import de.ngloader.streamevent.network.packet.standart.client.CPacketStandartDisconnect;
import de.ngloader.streamevent.network.packet.standart.client.CPacketStandartKeepAlive;
import de.ngloader.streamevent.network.packet.standart.server.SPacketStandartKeepAlive;

public class StandartPacketHandlerClient implements StandartClientPacketHandler {

	protected NetworkManager networkManager;

	@Override
	public void setNetworkManager(NetworkManager manager) {
		this.networkManager = manager;
	}

	@Override
	public void handleKeepAlive(CPacketStandartKeepAlive packet) {
		this.networkManager.sendPacket(new SPacketStandartKeepAlive(packet.getTime()));
	}

	@Override
	public void handleDisconnect(CPacketStandartDisconnect packet) {
		this.networkManager.close(packet.getReason());
	}

	@Override
	public void onDisconnect(String reason) {
		System.out.println(String.format("Disconnected. Reason: %s", reason));
	}
}