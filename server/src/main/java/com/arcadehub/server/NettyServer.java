package com.arcadehub.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.arcadehub.server.lobby.LobbyManager;
import com.arcadehub.server.game.GameLoopManager;
import com.arcadehub.server.leaderboard.LeaderboardManager;

public class NettyServer {
    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);
    private final EventLoopGroup bossGroup;
    private final EventLoopGroup workerGroup;
    private ChannelFuture gameChannel;
    private ChannelFuture chatChannel;
    private final LobbyManager lobbyManager;
    private final GameLoopManager gameLoopManager;
    private final LeaderboardManager leaderboardManager;

    public NettyServer(EventLoopGroup bossGroup, EventLoopGroup workerGroup, LobbyManager lobbyManager, GameLoopManager gameLoopManager, LeaderboardManager leaderboardManager) {
        this.bossGroup = bossGroup;
        this.workerGroup = workerGroup;
        this.lobbyManager = lobbyManager;
        this.gameLoopManager = gameLoopManager;
        this.leaderboardManager = leaderboardManager;
    }

    public void start(int gamePort, int chatPort) throws Exception {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                 .channel(NioServerSocketChannel.class)
                 .handler(new LoggingHandler(LogLevel.INFO))
                 .childHandler(new ServerInitializer(lobbyManager, gameLoopManager, leaderboardManager));

        gameChannel = bootstrap.bind(gamePort).sync();
        logger.info("Game server started on port {}", gamePort);

        chatChannel = bootstrap.bind(chatPort).sync();
        logger.info("Chat server started on port {}", chatPort);
    }

    public void stop() {
        if (gameChannel != null) gameChannel.channel().close();
        if (chatChannel != null) chatChannel.channel().close();
    }
}