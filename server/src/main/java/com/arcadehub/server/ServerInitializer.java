package com.arcadehub.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.handler.ssl.SslContext;
import com.arcadehub.server.lobby.LobbyManager;
import com.arcadehub.server.game.GameLoopManager;
import com.arcadehub.server.leaderboard.LeaderboardManager;

import java.util.concurrent.TimeUnit;

public class ServerInitializer extends ChannelInitializer<SocketChannel> {

    private static final int HEARTBEAT_INTERVAL_MS = 5000; // From project spec
    private static final int HEARTBEAT_TIMEOUT_MS = 20000; // From project spec

    private final SslContext sslContext;
    private final LobbyManager lobbyManager;
    private final GameLoopManager gameLoopManager;
    private final LeaderboardManager leaderboardManager;

    public ServerInitializer(SslContext sslContext, LobbyManager lobbyManager, GameLoopManager gameLoopManager, LeaderboardManager leaderboardManager) {
        this.sslContext = sslContext;
        this.lobbyManager = lobbyManager;
        this.gameLoopManager = gameLoopManager;
        this.leaderboardManager = leaderboardManager;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        // TLS
        pipeline.addLast(sslContext.newHandler(ch.alloc()));

        // Add idle state handler for heartbeat mechanism
        pipeline.addLast(new IdleStateHandler(HEARTBEAT_TIMEOUT_MS, HEARTBEAT_INTERVAL_MS, 0, TimeUnit.MILLISECONDS));

        // Decoders
        pipeline.addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(getClass().getClassLoader())));

        // Encoders
        pipeline.addLast(new ObjectEncoder());

        // Our custom server handler
        pipeline.addLast(new ServerHandler(lobbyManager, gameLoopManager, leaderboardManager));
    }
}
