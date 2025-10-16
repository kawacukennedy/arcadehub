package com.arcadehub.server.game;

import com.arcadehub.shared.*;
import java.util.*;

/**
 * Authoritative game engine for Snake multiplayer.
 */
public class GameEngineSnake {
    private Map<String, Snake> players = new HashMap<>();
    private Set<Food> foods = new HashSet<>();
    private boolean running = false;
    private long tickId = 0;
    private final int GRID_WIDTH = 30;
    private final int GRID_HEIGHT = 30;
    private final int MAX_FOOD = 6;
    private final long TICK_RATE_MS = 50;

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
        tickId++;
        // Apply movements, check collisions, spawn food
        for (Snake snake : players.values()) {
            moveSnake(snake);
        }
        checkCollisions();
        spawnFoodIfNeeded();
        // Broadcast state update
        // TODO: Send STATE_UPDATE packet
    }

    private void moveSnake(Snake snake) {
        Position head = snake.getHeadPosition();
        Position newHead = new Position(head.getX(), head.getY());
        switch (snake.getDirection()) {
            case UP: newHead.setY(newHead.getY() - 1); break;
            case DOWN: newHead.setY(newHead.getY() + 1); break;
            case LEFT: newHead.setX(newHead.getX() - 1); break;
            case RIGHT: newHead.setX(newHead.getX() + 1); break;
        }
        snake.getBody().add(0, newHead);
        // Check food
        boolean ate = foods.removeIf(f -> f.getPosition().equals(newHead));
        if (!ate) {
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
        Position pos;
        do {
            pos = new Position(rand.nextInt(GRID_WIDTH), rand.nextInt(GRID_HEIGHT));
        } while (players.values().stream().anyMatch(s -> s.getBody().contains(pos)) || foods.stream().anyMatch(f -> f.getPosition().equals(pos)));
        foods.add(new Food(pos));
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
        if (players.size() <= 1) {
            MatchResult result = new MatchResult();
            result.scores = new HashMap<>();
            for (Snake s : players.values()) {
                result.scores.put(s.getUsername(), s.getBody().size());
            }
            if (!players.isEmpty()) {
                result.winner = players.keySet().iterator().next();
            }
            return result;
        }
        return null;
    }
}
