package de.ngloader.streamevent.network;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import de.ngloader.streamevent.network.packet.EnumPacketDirection;
import de.ngloader.streamevent.network.packet.PacketCodec;
import de.ngloader.streamevent.network.util.NettyUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.timeout.ReadTimeoutHandler;

public class NetworkHandler {

	protected final List<NetworkManager> networkManagers = new ArrayList<>();
	private final List<ChannelFuture> channels = new ArrayList<>();

	private final Thread networkRunner = new Thread(this::run, "keincloud-networkserver");
	private AtomicBoolean running = new AtomicBoolean();

	public NetworkHandler() {
		this.networkRunner.setDaemon(true);
	}

	public void run() {
		while (this.running.get()) {
			synchronized (this.networkManagers) {
				for (Iterator<NetworkManager> iterator = this.networkManagers.iterator(); iterator.hasNext();) {
					NetworkManager networkManager = iterator.next();
					if (networkManager.hasChannel()) {
						if (networkManager.isConnected()) {
							try {
								networkManager.run();
							} catch (Exception e) {
								networkManager.close("Internal client error");
								networkManager.stopReading();
							}
						} else {
							iterator.remove();
							networkManager.handleDisconnect();
						}
					}
				}
			}

			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void checkRunning() {
		if (!this.running.get()) {
			this.running.set(true);
			this.networkRunner.start();
		}
	}

	public void startServer(InetSocketAddress address, int threads, SslContext sslContext, Consumer<ChannelPipeline> consumer) {
		this.startServer(address, threads, new ChannelInitializer<Channel>() {

				@Override
				protected void initChannel(Channel channel) throws Exception {
					ChannelPipeline pipeline = channel.pipeline();
					pipeline.addLast("ssl", sslContext.newHandler(channel.alloc()));
					pipeline.addLast("length_prepender", new LengthFieldPrepender(4));
					pipeline.addLast("length_decoder", new LengthFieldBasedFrameDecoder(2097152, 0, 4, 0, 4));
					pipeline.addLast("timeout", new ReadTimeoutHandler(30));
					pipeline.addLast("codec", new PacketCodec(EnumPacketDirection.CLIENT, EnumPacketDirection.SERVER));
					
					consumer.accept(pipeline);
				}
			});
	}

	public void startServer(InetSocketAddress address, int threads, ChannelInitializer<Channel> channelInitializer) {
		this.checkRunning();
		this.channels.add(new ServerBootstrap()
			.group(NettyUtil.newEventLoopGroup(threads), NettyUtil.newEventLoopGroup())
			.channel(NettyUtil.newServerSocketChannelClass())
			.childHandler(channelInitializer)
			.bind(address)
			.syncUninterruptibly());
	}

	public void startClient(InetSocketAddress address, SslContext sslContext, Consumer<ChannelPipeline> consumer) {
		this.startClient(address, new ChannelInitializer<Channel>() {

			@Override
			protected void initChannel(Channel channel) throws Exception {
				ChannelPipeline pipeline = channel.pipeline();
				pipeline.addLast("ssl", sslContext.newHandler(channel.alloc()));
				pipeline.addLast("length_prepender", new LengthFieldPrepender(4));
				pipeline.addLast("length_decoder", new LengthFieldBasedFrameDecoder(2097152, 0, 4, 0, 4));
				pipeline.addLast("timeout", new ReadTimeoutHandler(30));
				pipeline.addLast("codec", new PacketCodec(EnumPacketDirection.SERVER, EnumPacketDirection.CLIENT));
				
				consumer.accept(pipeline);
			}
		});
	}

	public void startClient(InetSocketAddress address, ChannelInitializer<Channel> channelInitializer) {
		this.checkRunning();
		this.channels.add(new Bootstrap().group(NettyUtil.newEventLoopGroup())
			.channel(NettyUtil.newClientSocketChannelClass())
			.handler(channelInitializer)
			.connect(address)
			.syncUninterruptibly());
	}

	public void close() {
		for (ChannelFuture channel : this.channels) {
			try {
				channel.channel().close().sync();
			} catch (InterruptedException e) {
				//TODO log server was unable to stop
				e.printStackTrace();
			}
		}
	}
}