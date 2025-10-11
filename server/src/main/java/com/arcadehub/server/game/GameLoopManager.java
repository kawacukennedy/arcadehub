package com.arcadehub.server.game;

import com.arcadehub.shared.*;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.io.*;
import java.nio.file.*;
import java.time.Instant;
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
        // Game state
        private List<Snake> snakes = new ArrayList<>();
        private List<Ball> balls = new ArrayList<>();
        private List<Paddle> paddles = new ArrayList<>();
        private Map<String, Integer> scores = new HashMap<>();
        private long seed = 12345;
        private PrintWriter replayWriter;

        public LobbyGameLoop(UUID lobbyId, ConcurrentLinkedQueue<InputEnvelope> inputQueue) {
            this.lobbyId = lobbyId;
            this.executor = new ScheduledThreadPoolExecutor(1);
            this.inputQueue = inputQueue;
            // Init game state, e.g., add snake
            snakes.add(new Snake("alice", List.of(new Position(10, 10)), Direction.RIGHT));
            // Init replay
            try {
                Path replayPath = Paths.get("replays", lobbyId + ".ndjson");
                Files.createDirectories(replayPath.getParent());
                replayWriter = new PrintWriter(Files.newBufferedWriter(replayPath, StandardOpenOption.CREATE, StandardOpenOption.APPEND));
                // Write header
                String header = String.format("{\"version\":1,\"lobbyId\":\"%s\",\"seed\":%d,\"playerList\":[\"alice\"],\"createdAt\":\"%s\"}", lobbyId, seed, Instant.now());
                replayWriter.println(header);
            } catch (IOException e) {
                logger.error("Failed to init replay", e);
            }
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
            // Apply inputs
            for (InputEnvelope env : inputs) {
                String action = env.getAction();
                // For Snake, change direction
                for (Snake snake : snakes) {
                    if (snake.getUsername().equals(env.getUsername())) {
                        switch (action) {
                            case "MOVE_UP": snake.setDirection(Direction.UP); break;
                            case "MOVE_DOWN": snake.setDirection(Direction.DOWN); break;
                            case "MOVE_LEFT": snake.setDirection(Direction.LEFT); break;
                            case "MOVE_RIGHT": snake.setDirection(Direction.RIGHT); break;
                        }
                    }
                }
                // For Pong, move paddles
                for (Paddle paddle : paddles) {
                    if (paddle.getUsername().equals(env.getUsername())) {
                        switch (action) {
                            case "MOVE_UP": paddle.setPosition(new Position(paddle.getPosition().getX(), paddle.getPosition().getY() - 5)); break;
                            case "MOVE_DOWN": paddle.setPosition(new Position(paddle.getPosition().getX(), paddle.getPosition().getY() + 5)); break;
                        }
                    }
                }
            }
            // Update game state
            updateSnakes();
            updatePong();
            // Create state
            GameState state = new GameState(lobbyId.toString(), currentTick, new ArrayList<>(snakes), new ArrayList<>(paddles), new ArrayList<>(balls), new HashMap<>(scores), seed);
            // Append to replay
            if (replayWriter != null) {
                String snapshot = String.format("{\"tag\":\"TICK_SNAPSHOT\",\"tick\":%d,\"state\":%s,\"serverTimestampNs\":%d}", currentTick, state.toString(), System.nanoTime());
                replayWriter.println(snapshot);
                replayWriter.flush();
            }
            // Broadcast
            StateUpdatePayload payload = new StateUpdatePayload(state, System.currentTimeMillis());
            // TODO: Send to clients
            logger.debug("Tick {} for lobby {}, snakes: {}", currentTick, lobbyId, snakes.size());
        }

        private void updateSnakes() {
            for (Snake snake : snakes) {
                // Move head
                Position head = snake.getHeadPosition();
                Position newHead = switch (snake.getDirection()) {
                    case UP -> new Position(head.getX(), head.getY() - 1);
                    case DOWN -> new Position(head.getX(), head.getY() + 1);
                    case LEFT -> new Position(head.getX() - 1, head.getY());
                    case RIGHT -> new Position(head.getX() + 1, head.getY());
                };
                snake.getBody().add(0, newHead);
                // TODO: Remove tail unless ate food
                // TODO: Check collision
            }
        }

        private void updatePong() {
            for (Ball ball : balls) {
                // Move ball
                ball.setPosition(new Position(ball.getPosition().getX() + ball.getVelocityX(),
                                             ball.getPosition().getY() + ball.getVelocityY()));
                // TODO: Collision with walls, paddles
                // Bounce off top/bottom
                if (ball.getPosition().getY() <= 0 || ball.getPosition().getY() >= 600) {
                    ball.setVelocityY(-ball.getVelocityY());
                }
                // TODO: Score if off left/right
            }
        }

        public void stop() {
            running = false;
            executor.shutdown();
            if (replayWriter != null) {
                replayWriter.close();
            }
        }
    }
}