package com.arcadehub.server.game;

import com.arcadehub.shared.*;
import java.util.*;
import java.util.function.Consumer;

/**
 * Authoritative game engine for Pong.
 */
public class GameEnginePong {
    private Map<String, Paddle> paddles = new HashMap<>();
    private Ball ball;
    private Map<String, Integer> scores = new HashMap<>();
    private boolean running = false;
    private boolean ended = false;
    private long tickId = 0;
    private final int ARENA_WIDTH = 1280;
    private final int ARENA_HEIGHT = 720;
    private final long TICK_RATE_MS = 16; // ~60 FPS
    private final float BALL_SPEED_INITIAL = 300f;
    private final float BALL_SIZE = 16f;
    private final float PADDLE_WIDTH = 24f;
    private final float PADDLE_HEIGHT = 128f;
    private final float MAX_PADDLE_SPEED = 400f;
    private final int POINTS_TO_WIN = 10;
    private float currentBallSpeed = BALL_SPEED_INITIAL;
    private int bounceCount = 0;
    private Consumer<Packet> broadcaster;
    private Consumer<MatchResult> onMatchEnd;

    public GameEnginePong(Consumer<Packet> broadcaster, Consumer<MatchResult> onMatchEnd) {
        this.broadcaster = broadcaster;
        this.onMatchEnd = onMatchEnd;
    }

    public void addPlayer(String username, boolean leftSide) {
        float x = leftSide ? 50 : ARENA_WIDTH - 50 - PADDLE_WIDTH;
        paddles.put(username, new Paddle(new Position(x, ARENA_HEIGHT / 2 - PADDLE_HEIGHT / 2), PADDLE_WIDTH, PADDLE_HEIGHT, username));
        scores.put(username, 0);
    }

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
        if (ended) return;
        tickId++;
        updateBall();
        // Broadcast state
        Map<String, Object> state = Map.of("paddles", new ArrayList<>(paddles.values()), "ball", ball, "scores", new HashMap<>(scores));
        Packet packet = new Packet();
        packet.type = PacketType.STATE_UPDATE;
        packet.payload = Map.of("tick", tickId, "game", "PONG", "state", state);
        broadcaster.accept(packet);
        // Check win
        for (int score : scores.values()) {
            if (score >= POINTS_TO_WIN) {
                ended = true;
                MatchResult result = computeResult();
                onMatchEnd.accept(result);
                break;
            }
        }
    }

    private void updateBall() {
        if (ball == null) return;
        // Move ball
        float deltaTime = TICK_RATE_MS / 1000f;
        Position pos = ball.getPosition();
        float newX = pos.getX() + ball.getVelocityX() * deltaTime;
        float newY = pos.getY() + ball.getVelocityY() * deltaTime;
        ball.setPosition(new Position(newX, newY));
        // Bounce off top/bottom
        if (newY <= 0 || newY + BALL_SIZE >= ARENA_HEIGHT) {
            ball.setVelocityY(-ball.getVelocityY());
            bounceCount++;
            if (bounceCount % 5 == 0) {
                currentBallSpeed *= 1.1f;
                ball.setVelocityX(ball.getVelocityX() > 0 ? currentBallSpeed : -currentBallSpeed);
                ball.setVelocityY(ball.getVelocityY() > 0 ? currentBallSpeed * 0.5f : -currentBallSpeed * 0.5f); // adjust
            }
        }
        // Paddle collision
        for (Paddle paddle : paddles.values()) {
            if (checkPaddleCollision(ball, paddle)) {
                ball.setVelocityX(-ball.getVelocityX());
                bounceCount++;
                if (bounceCount % 5 == 0) {
                    currentBallSpeed *= 1.1f;
                    ball.setVelocityX(ball.getVelocityX() > 0 ? currentBallSpeed : -currentBallSpeed);
                }
                break;
            }
        }
        // Score
        if (newX < 0) {
            // Right player scores
            String rightPlayer = getRightPlayer();
            if (rightPlayer != null) {
                scores.put(rightPlayer, scores.get(rightPlayer) + 1);
            }
            resetBall();
        } else if (newX + BALL_SIZE > ARENA_WIDTH) {
            // Left player scores
            String leftPlayer = getLeftPlayer();
            if (leftPlayer != null) {
                scores.put(leftPlayer, scores.get(leftPlayer) + 1);
            }
            resetBall();
        }
    }

    private boolean checkPaddleCollision(Ball ball, Paddle paddle) {
        return ball.getPosition().getX() < paddle.getPosition().getX() + paddle.getWidth() &&
               ball.getPosition().getX() + BALL_SIZE > paddle.getPosition().getX() &&
               ball.getPosition().getY() < paddle.getPosition().getY() + paddle.getHeight() &&
               ball.getPosition().getY() + BALL_SIZE > paddle.getPosition().getY();
    }

    private String getLeftPlayer() {
        for (Paddle p : paddles.values()) {
            if (p.getPosition().getX() < ARENA_WIDTH / 2) return p.getUsername();
        }
        return null;
    }

    private String getRightPlayer() {
        for (Paddle p : paddles.values()) {
            if (p.getPosition().getX() > ARENA_WIDTH / 2) return p.getUsername();
        }
        return null;
    }

    public void applyInput(String player, InputAction action, long tick) {
        Paddle paddle = paddles.get(player);
        if (paddle == null) return;
        float deltaTime = TICK_RATE_MS / 1000f;
        float moveAmount = MAX_PADDLE_SPEED * deltaTime;
        Position pos = paddle.getPosition();
        switch (action) {
            case UP -> paddle.setPosition(new Position(pos.getX(), Math.max(0, pos.getY() - moveAmount)));
            case DOWN -> paddle.setPosition(new Position(pos.getX(), Math.min(ARENA_HEIGHT - PADDLE_HEIGHT, pos.getY() + moveAmount)));
        }
    }

    private void initializeBall() {
        ball = new Ball(new Position(ARENA_WIDTH / 2 - BALL_SIZE / 2, ARENA_HEIGHT / 2 - BALL_SIZE / 2), 1, 0, BALL_SIZE);
        currentBallSpeed = BALL_SPEED_INITIAL;
        bounceCount = 0;
        // Random direction
        Random rand = new Random();
        float dirX = rand.nextBoolean() ? 1 : -1;
        float dirY = (rand.nextFloat() - 0.5f) * 0.5f; // small vertical
        ball.setVelocityX(dirX * currentBallSpeed);
        ball.setVelocityY(dirY * currentBallSpeed);
    }

    private void resetBall() {
        initializeBall();
    }

    private MatchResult computeResult() {
        MatchResult result = new MatchResult();
        result.scores = new HashMap<>(scores);
        // Winner is the one with max score
        String winner = null;
        int maxScore = -1;
        for (Map.Entry<String, Integer> entry : scores.entrySet()) {
            if (entry.getValue() > maxScore) {
                maxScore = entry.getValue();
                winner = entry.getKey();
            }
        }
        result.winner = winner;
        return result;
    }
}
