package de.ngloader.streamevent.network.packet;

import de.ngloader.streamevent.network.NetworkManager;

public interface PacketHandler {

	public void setNetworkManager(NetworkManager manager);

	public default void onConnected() { };
	public default void onDisconnect(String reason) { };
}