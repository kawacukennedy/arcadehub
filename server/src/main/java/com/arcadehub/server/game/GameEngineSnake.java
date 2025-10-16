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
        // Move head in direction, add to body, remove tail if no food eaten
        Position head = snake.getHeadPosition();
        Position newHead = new Position(head.getX(), head.getY());
        switch (snake.getDirection()) {
            case UP: newHead.setY(newHead.getY() - 1); break;
            case DOWN: newHead.setY(newHead.getY() + 1); break;
            case LEFT: newHead.setX(newHead.getX() - 1); break;
            case RIGHT: newHead.setX(newHead.getX() + 1); break;
        }
        snake.getBody().add(0, newHead);
        // TODO: Check if food eaten, else remove tail
    }

    private void checkCollisions() {
        // Wall, self, other snakes
        // TODO: Implement
    }

    private void spawnFoodIfNeeded() {
        if (foods.size() < MAX_FOOD) {
            spawnFood();
        }
    }

    private void spawnFood() {
        // Random empty cell
        // TODO: Implement
    }

    public void applyInput(String player, Direction direction, long tick) {
        if (players.containsKey(player)) {
            players.get(player).setDirection(direction);
        }
    }

    private MatchResult computeResult() {
        // TODO: Implement
        return null;
    }
}
