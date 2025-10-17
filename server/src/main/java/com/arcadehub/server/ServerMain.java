package com.arcadehub.server;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.arcadehub.server.lobby.LobbyManager;
import com.arcadehub.server.game.GameLoopManager;
import com.arcadehub.server.leaderboard.LeaderboardManager;
import com.arcadehub.server.game.AntiCheatValidator;

import javax.sql.DataSource;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ServerMain {
    private static final Logger logger = LoggerFactory.getLogger(ServerMain.class);

    // Config from environment
    private static final String DB_URL = System.getenv().getOrDefault("DB_URL", "jdbc:postgresql://localhost:5432/arcadehub");
    private static final String DB_USER = System.getenv().getOrDefault("DB_USER", "user");
    private static final String DB_PASS = System.getenv().getOrDefault("DB_PASS", "pass");
    private static final int GAME_PORT = Integer.parseInt(System.getenv().getOrDefault("GAME_PORT", "5050"));
    private static final int CHAT_PORT = Integer.parseInt(System.getenv().getOrDefault("CHAT_PORT", "5051"));
    private static final int ADMIN_PORT = Integer.parseInt(System.getenv().getOrDefault("ADMIN_PORT", "8080"));
    private static final String ADMIN_TOKEN = System.getenv().getOrDefault("ADMIN_TOKEN", "default_token");

    public static void main(String[] args) throws Exception {
        // Bootstrap DB pool
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(DB_URL);
        config.setUsername(DB_USER);
        config.setPassword(DB_PASS);
        config.setMaximumPoolSize(10);
        DataSource dataSource = new HikariDataSource(config);
        logger.info("DB pool initialized");

        // Scheduled tasks executor
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
        scheduler.scheduleAtFixedRate(() -> logger.info("Scheduled task: cleanup inactive lobbies"), 0, 5, TimeUnit.MINUTES);

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            LobbyManager lobbyManager = new LobbyManager();
            LeaderboardManager leaderboardManager = new LeaderboardManager();
            AntiCheatValidator antiCheatValidator = new AntiCheatValidator();
            GameLoopManager gameLoopManager = new GameLoopManager(leaderboardManager);
            NettyServer nettyServer = new NettyServer(bossGroup, workerGroup, lobbyManager, gameLoopManager, leaderboardManager, antiCheatValidator);
            nettyServer.start(GAME_PORT, CHAT_PORT);

            // Admin API
            AdminApi adminApi = new AdminApi(dataSource, ADMIN_TOKEN);
            adminApi.start(ADMIN_PORT);

            // Wait for shutdown
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                logger.info("Shutting down...");
                scheduler.shutdown();
                nettyServer.stop();
                adminApi.stop();
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            }));

            // Keep running
            Thread.currentThread().join();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
