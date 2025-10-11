package com.arcadehub.server.game;

import com.arcadehub.shared.GameType;
import com.arcadehub.shared.Lobby;
import com.arcadehub.shared.StateUpdatePacket;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameLoopManager {
    private static final Logger logger = LoggerFactory.getLogger(GameLoopManager.class);
    private final Map<UUID, GameLoop> activeGameLoops;
    private final ScheduledExecutorService scheduler;

    public GameLoopManager() {
        this.activeGameLoops = new ConcurrentHashMap<>();
        this.scheduler = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
    }

    public void startGame(Lobby lobby, Map<String, ChannelHandlerContext> playerChannels) {
        if (activeGameLoops.containsKey(lobby.getId())) {
            logger.warn("Game for lobby {} already started.", lobby.getId());
            return;
        }

        GameLoop gameLoop;
        switch (lobby.getGameType()) {
            case SNAKE:
                gameLoop = new SnakeGameLoop(lobby, playerChannels);
                break;
            case PONG:
                gameLoop = new PongGameLoop(lobby, playerChannels);
                break;
            default:
                logger.error("Unsupported game type: {}", lobby.getGameType());
                return;
        }

        activeGameLoops.put(lobby.getId(), gameLoop);
        scheduler.scheduleAtFixedRate(gameLoop, 0, 1000 / gameLoop.getTickRateHz(), TimeUnit.MILLISECONDS);
        logger.info("Started {} game for lobby {}", lobby.getGameType(), lobby.getId());
    }

    public void stopGame(UUID lobbyId) {
        GameLoop gameLoop = activeGameLoops.remove(lobbyId);
        if (gameLoop != null) {
            gameLoop.stop();
            logger.info("Stopped game for lobby {}", lobbyId);
        }
    }

    public void shutdown() {
        scheduler.shutdown();
        activeGameLoops.values().forEach(GameLoop::stop);
        logger.info("GameLoopManager shut down.");
    }

    // Interface for game-specific loops
    private interface GameLoop extends Runnable {
        int getTickRateHz();
        void stop();
    }

    // Placeholder for Snake Game Loop
    private static class SnakeGameLoop implements GameLoop {
        private final Lobby lobby;
        private final Map<String, ChannelHandlerContext> playerChannels;
        private volatile boolean running = true;

        public SnakeGameLoop(Lobby lobby, Map<String, ChannelHandlerContext> playerChannels) {
            this.lobby = lobby;
            this.playerChannels = playerChannels;
        }

        @Override
        public int getTickRateHz() {
            return 20; // From game_specs.snake.tick_rate_hz
        }

        @Override
        public void run() {
            if (!running) return;
            // TODO: Implement Snake game logic
            logger.debug("Snake game loop for lobby {}", lobby.getId());
            // Example: Send a state update packet
            StateUpdatePacket stateUpdate = new StateUpdatePacket(System.currentTimeMillis());
            playerChannels.values().forEach(ctx -> ctx.writeAndFlush(stateUpdate));
        }

        @Override
        public void stop() {
            running = false;
        }
    }

    // Placeholder for Pong Game Loop
    private static class PongGameLoop implements GameLoop {
        private final Lobby lobby;
        private final Map<String, ChannelHandlerContext> playerChannels;
        private volatile boolean running = true;

        public PongGameLoop(Lobby lobby, Map<String, ChannelHandlerContext> playerChannels) {
            this.lobby = lobby;
            this.playerChannels = playerChannels;
        }

        @Override
        public int getTickRateHz() {
            return 60; // From game_specs.pong.tick_rate_hz
        }

        @Override
        public void run() {
            if (!running) return;
            // TODO: Implement Pong game logic
            logger.debug("Pong game loop for lobby {}", lobby.getId());
            // Example: Send a state update packet
            StateUpdatePacket stateUpdate = new StateUpdatePacket(System.currentTimeMillis());
            playerChannels.values().forEach(ctx -> ctx.writeAndFlush(stateUpdate));
        }

        @Override
        public void stop() {
            running = false;
        }
    }
}