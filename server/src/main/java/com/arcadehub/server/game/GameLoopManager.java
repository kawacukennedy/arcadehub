package com.arcadehub.server.game;

import com.arcadehub.shared.*;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ConcurrentLinkedQueue;

public class GameLoopManager {
    private static final Logger logger = LoggerFactory.getLogger(GameLoopManager.class);
    private final Map<UUID, LobbyGameLoop> activeGameLoops;
    private final Map<UUID, ConcurrentLinkedQueue<InputEnvelope>> inputQueues;

    public GameLoopManager() {
        this.activeGameLoops = new ConcurrentHashMap<>();
        this.inputQueues = new ConcurrentHashMap<>();
    }

    public void startLoop(UUID lobbyId) {
        if (activeGameLoops.containsKey(lobbyId)) {
            logger.warn("Game loop for lobby {} already started.", lobbyId);
            return;
        }

        ConcurrentLinkedQueue<InputEnvelope> queue = new ConcurrentLinkedQueue<>();
        inputQueues.put(lobbyId, queue);
        LobbyGameLoop gameLoop = new LobbyGameLoop(lobbyId, queue);
        activeGameLoops.put(lobbyId, gameLoop);
        gameLoop.start();
        logger.info("Started game loop for lobby {}", lobbyId);
    }

    public void queueInput(UUID lobbyId, InputEnvelope input) {
        ConcurrentLinkedQueue<InputEnvelope> queue = inputQueues.get(lobbyId);
        if (queue != null) {
            queue.add(input);
        }
    }

    public void stopLoop(UUID lobbyId) {
        LobbyGameLoop gameLoop = activeGameLoops.remove(lobbyId);
        if (gameLoop != null) {
            gameLoop.stop();
            inputQueues.remove(lobbyId);
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
        private int currentTick = 0;
        private final ConcurrentLinkedQueue<InputEnvelope> inputQueue;

        public LobbyGameLoop(UUID lobbyId, ConcurrentLinkedQueue<InputEnvelope> inputQueue) {
            this.lobbyId = lobbyId;
            this.executor = new ScheduledThreadPoolExecutor(1);
            this.inputQueue = inputQueue;
        }

        public void start() {
            executor.scheduleAtFixedRate(this::tick, 0, 50, TimeUnit.MILLISECONDS); // 20 Hz = 50ms
        }

        private void tick() {
            if (!running) return;
            currentTick++;
            // Read inputs
            List<InputEnvelope> inputs = new ArrayList<>();
            InputEnvelope input;
            while ((input = inputQueue.poll()) != null) {
                inputs.add(input);
            }
            // TODO: Apply inputs to game state
            // For now, dummy state
            List<Snake> snakes = List.of();
            List<Paddle> paddles = List.of();
            List<Ball> balls = List.of();
            Map<String, Integer> scores = Map.of();
            long seed = 12345;
            GameState state = new GameState(lobbyId.toString(), currentTick, snakes, paddles, balls, scores, seed);
            // TODO: Append to replay
            // Broadcast
            StateUpdatePayload payload = new StateUpdatePayload(state, System.currentTimeMillis());
            // TODO: Send to clients
            logger.debug("Tick {} for lobby {}, processed {} inputs", currentTick, lobbyId, inputs.size());
        }

        public void stop() {
            running = false;
            executor.shutdown();
        }
    }
}