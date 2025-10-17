 package com.arcadehub.server.game;

import com.arcadehub.shared.*;
import java.util.*;
import java.util.function.Consumer;

/**
 * Authoritative game engine for Snake multiplayer.
 */
public class GameEngineSnake {
    private Map<String, Snake> players = new HashMap<>();
    private Set<Food> foods = new HashSet<>();
    private boolean running = false;
    private boolean ended = false;
    private long tickId = 0;
    private final int GRID_WIDTH = 30;
    private final int GRID_HEIGHT = 30;
    private final int MAX_FOOD = 6;
    private final long TICK_RATE_MS = 50;
    private Consumer<Packet> broadcaster;
    private Consumer<MatchResult> onMatchEnd;

    public GameEngineSnake(Consumer<Packet> broadcaster, Consumer<MatchResult> onMatchEnd) {
        this.broadcaster = broadcaster;
        this.onMatchEnd = onMatchEnd;
    }

    public void addPlayer(String username) {
        // Simple positioning, can be improved
        int startX = 5 + players.size() * 5;
        int startY = 5;
        players.put(username, new Snake(username, new ArrayList<>(List.of(new Position(startX, startY))), Direction.RIGHT));
    }

    public void removePlayer(String username) {
        players.remove(username);
    }

    public void start() {
        running = true;
        // Initialize foods, etc.
        spawnFood();
        // Start tick loop in a thread
        new Thread(this::tickLoop).start();
    }

    private void tickLoop() {
        while (running) {
            tick();
            try {
                Thread.sleep(TICK_RATE_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void tick() {
        if (ended) return;
        tickId++;
        // Apply movements, check collisions, spawn food
        for (Snake snake : players.values()) {
            moveSnake(snake);
        }
        checkCollisions();
        spawnFoodIfNeeded();
        // Broadcast state update
        Map<String, Object> state = Map.of("snakes", new ArrayList<>(players.values()), "food", new ArrayList<>(foods));
        Packet packet = new Packet();
        packet.type = PacketType.STATE_UPDATE;
        packet.payload = Map.of("tick", tickId, "game", "SNAKE", "state", state);
        broadcaster.accept(packet);
        // Check if game ended
        if (players.size() <= 1) {
            ended = true;
            MatchResult result = computeResult();
            onMatchEnd.accept(result);
        }
    }

    private void moveSnake(Snake snake) {
        Position head = snake.getHeadPosition();
        Position newHead = switch (snake.getDirection()) {
            case UP -> new Position(head.getX(), head.getY() - 1);
            case DOWN -> new Position(head.getX(), head.getY() + 1);
            case LEFT -> new Position(head.getX() - 1, head.getY());
            case RIGHT -> new Position(head.getX() + 1, head.getY());
        };
        snake.getBody().add(0, newHead);
        // Check food
        boolean ate = foods.removeIf(f -> f.getPosition().equals(newHead));
        if (ate) {
            snake.setScore(snake.getScore() + 1);
        } else {
            snake.getBody().remove(snake.getBody().size() - 1);
        }
    }

    private void checkCollisions() {
        Set<String> toRemove = new HashSet<>();
        for (Snake snake : players.values()) {
            Position head = snake.getHeadPosition();
            // Wall
            if (head.getX() < 0 || head.getX() >= GRID_WIDTH || head.getY() < 0 || head.getY() >= GRID_HEIGHT) {
                toRemove.add(snake.getUsername());
                continue;
            }
            // Self
            if (snake.getBody().subList(1, snake.getBody().size()).contains(head)) {
                toRemove.add(snake.getUsername());
                continue;
            }
            // Other snakes
            for (Snake other : players.values()) {
                if (!other.getUsername().equals(snake.getUsername()) && other.getBody().contains(head)) {
                    toRemove.add(snake.getUsername());
                    break;
                }
            }
        }
        for (String p : toRemove) {
            players.remove(p);
        }
    }

    private void spawnFoodIfNeeded() {
        while (foods.size() < MAX_FOOD) {
            spawnFood();
        }
    }

    private void spawnFood() {
        Random rand = new Random();
        while (true) {
            Position pos = new Position(rand.nextInt(GRID_WIDTH), rand.nextInt(GRID_HEIGHT));
            boolean collision = players.values().stream().anyMatch(s -> s.getBody().contains(pos)) || foods.stream().anyMatch(f -> f.getPosition().equals(pos));
            if (!collision) {
                foods.add(new Food(pos, 0));
                break;
            }
        }
    }

    public void applyInput(String player, InputAction action, long tick) {
        if (players.containsKey(player)) {
            Direction dir = switch (action) {
                case UP -> Direction.UP;
                case DOWN -> Direction.DOWN;
                case LEFT -> Direction.LEFT;
                case RIGHT -> Direction.RIGHT;
            };
            players.get(player).setDirection(dir);
        }
    }

    private MatchResult computeResult() {
        MatchResult result = new MatchResult();
        result.scores = new HashMap<>();
        for (Snake s : players.values()) {
            result.scores.put(s.getUsername(), s.getScore());
        }
        if (players.size() == 1) {
            String winner = players.keySet().iterator().next();
            result.scores.put(winner, result.scores.get(winner) + 5);
            result.winner = winner;
        }
        return result;
    }
}
