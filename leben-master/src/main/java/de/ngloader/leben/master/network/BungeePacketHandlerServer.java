package de.ngloader.leben.master.network;

import de.ngloader.leben.network.NetworkManager;
import de.ngloader.leben.network.packet.bungee.BungeeServerPacketHandler;
import de.ngloader.leben.network.packet.bungee.server.SPacketBungeeInventoryLoad;
import de.ngloader.leben.network.packet.standart.StandartServerPacketHandler;
import de.ngloader.leben.network.packet.standart.server.SPacketStandartKeepAlive;

public class BungeePacketHandlerServer implements StandartServerPacketHandler, BungeeServerPacketHandler {

	@Override
	public void setNetworkManager(NetworkManager manager) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleInventoryLoad(SPacketBungeeInventoryLoad packet) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleKeepAlive(SPacketStandartKeepAlive packet) {
		// TODO Auto-generated method stub
		
	}

}