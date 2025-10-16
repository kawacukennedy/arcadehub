package com.arcadehub.server.game;

import com.arcadehub.shared.*;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Random;

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
    private final LeaderboardManager leaderboardManager;

    public GameLoopManager(LeaderboardManager leaderboardManager) {
        this.activeGameLoops = new ConcurrentHashMap<>();
        this.inputQueues = new ConcurrentHashMap<>();
        this.leaderboardManager = leaderboardManager;
    }

    public void setLeaderboardManager(LeaderboardManager lm) {
        // Already set in constructor
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
        private List<Food> foods = new ArrayList<>();
        private Map<String, Integer> scores = new HashMap<>();
        private long seed = 12345;
        private boolean matchEnded = false;
        private PrintWriter replayWriter;

        public LobbyGameLoop(UUID lobbyId, ConcurrentLinkedQueue<InputEnvelope> inputQueue) {
            this.lobbyId = lobbyId;
            this.executor = new ScheduledThreadPoolExecutor(1);
            this.inputQueue = inputQueue;
            // Init game state, e.g., add snake
            snakes.add(new Snake("alice", List.of(new Position(10, 10)), Direction.RIGHT));
            spawnFood();
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

            if (matchEnded) {
                // Update ELO
                updateElo();
                // Archive replay
                archiveReplay();
                // Stop loop
                stop();
            }
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
                // Check eat food
                boolean ate = false;
                for (Food food : foods) {
                    if (newHead.getX() == food.getPosition().getX() && newHead.getY() == food.getPosition().getY()) {
                        ate = true;
                        foods.remove(food);
                        spawnFood();
                        break;
                    }
                }
                if (!ate) {
                    snake.getBody().remove(snake.getBody().size() - 1); // Remove tail
                }
                // Check collision
                if (newHead.getX() < 0 || newHead.getX() >= 40 || newHead.getY() < 0 || newHead.getY() >= 30 ||
                    snake.getBody().subList(1, snake.getBody().size()).contains(newHead)) {
                    matchEnded = true;
                }
            }
        }

        private void updatePong() {
            for (Ball ball : balls) {
                // Move ball
                ball.setPosition(new Position(ball.getPosition().getX() + ball.getVelocityX(),
                                             ball.getPosition().getY() + ball.getVelocityY()));
                // Bounce off top/bottom
                if (ball.getPosition().getY() <= 0 || ball.getPosition().getY() >= 600) {
                    ball.setVelocityY(-ball.getVelocityY());
                }
                // Paddle collision
                for (Paddle paddle : paddles) {
                    if (ball.getPosition().getX() < paddle.getPosition().getX() + 10 &&
                        ball.getPosition().getX() + ball.getRadius() > paddle.getPosition().getX() &&
                        ball.getPosition().getY() < paddle.getPosition().getY() + paddle.getHeight() &&
                        ball.getPosition().getY() + ball.getRadius() > paddle.getPosition().getY()) {
                        ball.setVelocityX(-ball.getVelocityX());
                    }
                }
                // Score if off left/right
                if (ball.getPosition().getX() < 0) {
                    scores.put("player2", scores.getOrDefault("player2", 0) + 1);
                    resetBall();
                } else if (ball.getPosition().getX() > 800) {
                    scores.put("player1", scores.getOrDefault("player1", 0) + 1);
                    resetBall();
                }
                // Check win
                if (scores.values().stream().anyMatch(s -> s >= 10)) {
                    matchEnded = true;
                }
            }
        }

        private void resetBall() {
            for (Ball ball : balls) {
                ball.setPosition(new Position(400, 300));
                ball.setVelocityX(-ball.getVelocityX()); // Change direction
            }
        }

        private void spawnFood() {
            Random rand = new Random(seed);
            foods.add(new Food(new Position(rand.nextInt(40), rand.nextInt(30))));
        }

        private void updateElo() {
            // Simple ELO: winner +10, loser -10
            // TODO: Proper ELO formula
            for (Map.Entry<String, Integer> entry : scores.entrySet()) {
                if (entry.getValue() >= 10) {
                    // Winner
                    // leaderboardManager.updateElo(entry.getKey(), 10);
                } else {
                    // Loser
                    // leaderboardManager.updateElo(entry.getKey(), -10);
                }
            }
        }

        private void archiveReplay() {
            if (replayWriter != null) {
                replayWriter.close();
                // Move to archive
                try {
                    Path source = Paths.get("replays", lobbyId + ".ndjson");
                    Path target = Paths.get("replays/archive", lobbyId + ".ndjson.gz");
                    Files.createDirectories(target.getParent());
                    // TODO: Compress to gz
                    Files.move(source, target);
                } catch (IOException e) {
                    logger.error("Failed to archive replay", e);
                }
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