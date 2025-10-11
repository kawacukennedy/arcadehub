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
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class GameLoopManager {
    private static final Logger logger = LoggerFactory.getLogger(GameLoopManager.class);
    private final Map<UUID, LobbyGameLoop> activeGameLoops;

    public GameLoopManager() {
        this.activeGameLoops = new ConcurrentHashMap<>();
    }

    public void startLoop(UUID lobbyId) {
        if (activeGameLoops.containsKey(lobbyId)) {
            logger.warn("Game loop for lobby {} already started.", lobbyId);
            return;
        }

        LobbyGameLoop gameLoop = new LobbyGameLoop(lobbyId);
        activeGameLoops.put(lobbyId, gameLoop);
        gameLoop.start();
        logger.info("Started game loop for lobby {}", lobbyId);
    }

    public void stopLoop(UUID lobbyId) {
        LobbyGameLoop gameLoop = activeGameLoops.remove(lobbyId);
        if (gameLoop != null) {
            gameLoop.stop();
            logger.info("Stopped game loop for lobby {}", lobbyId);
        }
    }

    public void shutdown() {
        activeGameLoops.values().forEach(LobbyGameLoop::stop);
        logger.info("GameLoopManager shut down.");
    }

    // Per-lobby game loop
    private static class LobbyGameLoop {
        private final UUID lobbyId;
        private final ScheduledThreadPoolExecutor executor;
        private volatile boolean running = true;

        public LobbyGameLoop(UUID lobbyId) {
            this.lobbyId = lobbyId;
            this.executor = new ScheduledThreadPoolExecutor(1);
        }

        public void start() {
            executor.scheduleAtFixedRate(this::tick, 0, 50, TimeUnit.MILLISECONDS); // 20 Hz = 50ms
        }

        private void tick() {
            if (!running) return;
            // TODO: Implement tick logic as per spec
            // (1) read inputs queued up to tick N
            // (2) apply deterministic update
            // (3) resolve collisions
            // (4) update ELO on match end
            // (5) append immutable snapshot to replay stream
            // (6) broadcast STATE_UPDATE(N)
            logger.debug("Tick for lobby {}", lobbyId);
        }

        public void stop() {
            running = false;
            executor.shutdown();
        }
    }
}