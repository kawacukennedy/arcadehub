package com.arcadehub.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import com.arcadehub.server.network.JsonDecoder;
import com.arcadehub.server.network.JsonEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.handler.ssl.SslContext;
import com.arcadehub.server.lobby.LobbyManager;
import com.arcadehub.server.game.GameLoopManager;
import com.arcadehub.server.leaderboard.LeaderboardManager;
import com.arcadehub.server.game.AntiCheatValidator;
import com.arcadehub.server.network.ServerHandler;

import java.util.concurrent.TimeUnit;

public class ServerInitializer extends ChannelInitializer<SocketChannel> {

    private static final int HEARTBEAT_INTERVAL_MS = 5000; // From project spec
    private static final int HEARTBEAT_TIMEOUT_MS = 20000; // From project spec

    private final SslContext sslContext;
    private final LobbyManager lobbyManager;
    private final GameLoopManager gameLoopManager;
    private final LeaderboardManager leaderboardManager;
    private final AntiCheatValidator antiCheatValidator;

    public ServerInitializer(SslContext sslContext, LobbyManager lobbyManager, GameLoopManager gameLoopManager, LeaderboardManager leaderboardManager, AntiCheatValidator antiCheatValidator) {
        this.sslContext = sslContext;
        this.lobbyManager = lobbyManager;
        this.gameLoopManager = gameLoopManager;
        this.leaderboardManager = leaderboardManager;
        this.antiCheatValidator = antiCheatValidator;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        // TLS
        pipeline.addLast(sslContext.newHandler(ch.alloc()));

        // Add idle state handler for heartbeat mechanism
        pipeline.addLast(new IdleStateHandler(HEARTBEAT_TIMEOUT_MS, HEARTBEAT_INTERVAL_MS, 0, TimeUnit.MILLISECONDS));

        // Decoders
        pipeline.addLast(new JsonDecoder());

        // Encoders
        pipeline.addLast(new JsonEncoder());

        // Our custom server handler
        pipeline.addLast(new ServerHandler(lobbyManager, gameLoopManager, leaderboardManager, antiCheatValidator));
    }
}
