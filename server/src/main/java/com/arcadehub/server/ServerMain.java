package com.arcadehub.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.codec.http.HttpServerCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.arcadehub.server.lobby.LobbyManager;
import com.arcadehub.server.game.GameLoopManager;
import com.arcadehub.server.leaderboard.LeaderboardManager;
import com.arcadehub.server.lobby.LobbyManager;
import com.arcadehub.server.game.GameLoopManager;
import com.arcadehub.server.leaderboard.LeaderboardManager;

public class ServerMain {
    private static final Logger logger = LoggerFactory.getLogger(ServerMain.class);
    private static final int GAME_PORT = 5050;
    private static final int CHAT_PORT = 5051;

    public static void main(String[] args) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        // TLS context - for dev, self-signed
        SslContext sslContext = SslContextBuilder.forServer(
            getClass().getResourceAsStream("/certs/server.crt"),
            getClass().getResourceAsStream("/certs/server.key")
        ).build();

        try {
            LobbyManager lobbyManager = new LobbyManager();
            GameLoopManager gameLoopManager = new GameLoopManager();
            LeaderboardManager leaderboardManager = new LeaderboardManager();

            ServerBootstrap gameBootstrap = new ServerBootstrap();
            gameBootstrap.group(bossGroup, workerGroup)
                         .channel(NioServerSocketChannel.class)
                         .handler(new LoggingHandler(LogLevel.INFO))
                         .childHandler(new ServerInitializer(sslContext, lobbyManager, gameLoopManager, leaderboardManager)); // Pass managers to initializer

            gameLoopManager.setLeaderboardManager(leaderboardManager);

            // Bind and start to accept incoming connections.
            ChannelFuture gameFuture = gameBootstrap.bind(GAME_PORT).sync();
            logger.info("ArcadeHub Game Server started on port {}", GAME_PORT);

            // Admin HTTP server
            ServerBootstrap httpBootstrap = new ServerBootstrap();
            httpBootstrap.group(bossGroup, workerGroup)
                         .channel(NioServerSocketChannel.class)
                         .handler(new LoggingHandler(LogLevel.INFO))
                         .childHandler(new HttpServerInitializer());
            ChannelFuture httpFuture = httpBootstrap.bind(8081).sync();
            logger.info("ArcadeHub Admin HTTP Server started on port 8081");

            // For now, we'll use the same initializer for chat, but it can be separated later if needed.
            // ChannelFuture chatFuture = gameBootstrap.bind(CHAT_PORT).sync();
            // logger.info("ArcadeHub Chat Server started on port {}", CHAT_PORT);

            // Wait until the server socket is closed.
            gameFuture.channel().closeFuture().sync();
            httpFuture.channel().closeFuture().sync();
            // chatFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
