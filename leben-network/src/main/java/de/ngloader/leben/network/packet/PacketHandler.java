package de.ngloader.leben.network.packet;

import de.ngloader.leben.network.NetworkManager;

public interface PacketHandler {

	public void setNetworkManager(NetworkManager manager);

	public default void onConnected() { };
	public default void onDisconnect(String reason) { };
}