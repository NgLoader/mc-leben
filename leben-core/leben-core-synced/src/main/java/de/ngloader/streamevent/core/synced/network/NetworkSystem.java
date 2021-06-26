package de.ngloader.streamevent.core.synced.network;

import java.io.File;
import java.net.InetSocketAddress;

import javax.net.ssl.SSLException;

import de.ngloader.streamevent.core.synced.LebenCoreConfig;
import de.ngloader.streamevent.network.NetworkHandler;
import de.ngloader.streamevent.network.NetworkManager;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;

public class NetworkSystem extends NetworkHandler {

	private InetSocketAddress address;
	private SslContext sslContext;

	public NetworkSystem(LebenCoreConfig config) {
		LebenCoreConfig.MasterConfig masterConfig = config.master;
		this.address = new InetSocketAddress(masterConfig.address, masterConfig.port);

		try {
			this.sslContext = SslContextBuilder.forClient().trustManager(new File(masterConfig.sslPath)).build();
		} catch (SSLException e) {
			e.printStackTrace();
		}

		this.connect();
	}

	public void connect() {
		this.startClient(this.address, this.sslContext, (pipeline) -> {
			NetworkManager networkManager = new NetworkManager();
			networkManager.setPacketHandler(new AuthPacketHandlerClient());

			NetworkSystem.this.networkManagers.add(networkManager);
			pipeline.addLast("packet_handler", networkManager);
		});
	}
}