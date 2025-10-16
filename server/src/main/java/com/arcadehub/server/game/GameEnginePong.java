package com.arcadehub.server.game;

import com.arcadehub.shared.*;
import java.util.*;

/**
 * Authoritative game engine for Pong.
 */
public class GameEnginePong {
    private Map<String, Paddle> paddles = new HashMap<>();
    private Ball ball;
    private Map<String, Integer> scores = new HashMap<>();
    private boolean running = false;
    private long tickId = 0;
    private final int ARENA_WIDTH = 1280;
    private final int ARENA_HEIGHT = 720;
    private final long TICK_RATE_MS = 16; // ~60 FPS

    public void start() {
        running = true;
        initializeBall();
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
        updateBall();
        // Broadcast state
    }

    private void updateBall() {
        // Move ball, check collisions
        // TODO: Implement physics
    }

    public void applyInput(String player, InputAction action, long tick) {
        // Update paddle position
    }

    private void initializeBall() {
        ball = new Ball();
        ball.setPosition(new Position(ARENA_WIDTH / 2, ARENA_HEIGHT / 2));
        // Set velocity
    }
}
