package com.arcadehub.client.network;

import com.arcadehub.shared.Packet;
import com.arcadehub.shared.NetworkPacket;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class ClientNetworkManager {
    private static final Logger logger = LoggerFactory.getLogger(ClientNetworkManager.class);
    private static final String HOST = "localhost"; // TODO: Make configurable
    private static final int PORT = 5050; // Game port

    private EventLoopGroup group;
    private Channel channel;

    public void connect() throws Exception {
        group = new NioEventLoopGroup();

        // TLS context for client - trust self-signed for dev
        SslContext sslContext = SslContextBuilder.forClient()
            .trustManager(InsecureTrustManagerFactory.INSTANCE)
            .build();

        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
             .channel(NioSocketChannel.class)
             .handler(new ChannelInitializer<SocketChannel>() {
                 @Override
                 public void initChannel(SocketChannel ch) throws Exception {
                     ch.pipeline().addLast(
                             sslContext.newHandler(ch.alloc(), HOST, PORT),
                             new IdleStateHandler(0, 5, 0, TimeUnit.SECONDS), // Write idle for heartbeat
                             new ObjectEncoder(),
                             new ObjectDecoder(ClassResolvers.weakCachingConcurrentResolver(getClass().getClassLoader())),
                             new ClientHandler()
                     );
                 }
             });

            ChannelFuture f = b.connect(HOST, PORT).sync();
            channel = f.channel();
            logger.info("Connected to server at {}:{}", HOST, PORT);
            channel.closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

    public void sendPacket(Packet packet) {
        if (channel != null && channel.isActive()) {
            NetworkPacket netPacket = new NetworkPacket(packet.getType(), packet.getVersion(), null, packet.getPayload());
            channel.writeAndFlush(netPacket);
        } else {
            logger.warn("Attempted to send packet while not connected or channel inactive.");
        }
    }

    public void disconnect() {
        if (channel != null) {
            channel.close();
        }
        if (group != null) {
            group.shutdownGracefully();
        }
        logger.info("Disconnected from server.");
    }

    public boolean isConnected() {
        return channel != null && channel.isActive();
    }
}
