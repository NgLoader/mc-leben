package de.ngloader.leben.core.synced.network;

import de.ngloader.leben.network.packet.PacketRegistry;
import de.ngloader.leben.network.packet.auth.AuthClientPacketHandler;
import de.ngloader.leben.network.packet.auth.client.CPacketAuthFinish;
import de.ngloader.leben.network.packet.auth.client.CPacketAuthHandshake;
import de.ngloader.leben.network.packet.auth.server.SPacketAuthClientHello;
import de.ngloader.leben.network.packet.auth.server.SPacketAuthHandshake;

public class AuthPacketHandlerClient extends StandartPacketHandlerClient implements AuthClientPacketHandler {

	@Override
	public void onConnected() {
		this.networkManager.sendPacket(new SPacketAuthClientHello());
	}

	@Override
	public void handleFinish(CPacketAuthFinish packet) {
		this.networkManager.setPacketRegistry(PacketRegistry.BUNGEE);
//		this.networkManager.setPacketHandler(new BungeePacketHandlerClient());
	}

	@Override
	public void handleHandshake(CPacketAuthHandshake packet) {
		byte[] data = packet.getData();

		//TODO encrypt data

		this.networkManager.sendPacket(new SPacketAuthHandshake(data));
	}
}