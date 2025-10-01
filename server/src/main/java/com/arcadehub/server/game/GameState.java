package com.arcadehub.server.game;

import com.arcadehub.common.Ball;
import com.arcadehub.common.Food;
import com.arcadehub.common.Paddle;
import com.arcadehub.common.Position;
import com.arcadehub.common.Snake;
import com.arcadehub.common.GameType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class GameState {
    private UUID lobbyId;
    private GameType gameType;
    private long tick;
    private Map<String, Snake> snakes;
    private List<Food> foodItems;
    private Ball pongBall;
    private Map<String, Paddle> paddles;
    private Map<String, Integer> playerScores;

    public GameState(UUID lobbyId, GameType gameType) {
        this.lobbyId = lobbyId;
        this.gameType = gameType;
        this.tick = 0;
        this.snakes = new ConcurrentHashMap<>();
        this.foodItems = new ArrayList<>();
        this.paddles = new ConcurrentHashMap<>();
        this.playerScores = new ConcurrentHashMap<>();

        // Initialize game objects based on game type
        if (gameType == GameType.SNAKE) {
            // Example initial food
            foodItems.add(new Food(new Position(20, 20), 1)); // Corrected constructor call
        } else if (gameType == GameType.PONG) {
            // Example initial ball
            pongBall = new Ball(new Position(400, 300), 5.0f, 5.0f, 8.0f); // Corrected constructor call
        }
    }

    public UUID getLobbyId() {
        return lobbyId;
    }

    public void setLobbyId(UUID lobbyId) {
        this.lobbyId = lobbyId;
    }

    public GameType getGameType() {
        return gameType;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public long getTick() {
        return tick;
    }

    public void setTick(long tick) {
        this.tick = tick;
    }

    public Map<String, Snake> getSnakes() {
        return snakes;
    }

    public void setSnakes(Map<String, Snake> snakes) {
        this.snakes = snakes;
    }

    public List<Food> getFoodItems() {
        return foodItems;
    }

    public void setFoodItems(List<Food> foodItems) {
        this.foodItems = foodItems;
    }

    public Ball getPongBall() {
        return pongBall;
    }

    public void setPongBall(Ball pongBall) {
        this.pongBall = pongBall;
    }

    public Map<String, Paddle> getPaddles() {
        return paddles;
    }

    public void setPaddles(Map<String, Paddle> paddles) {
        this.paddles = paddles;
    }

    public Map<String, Integer> getPlayerScores() {
        return playerScores;
    }

    public void setPlayerScores(Map<String, Integer> playerScores) {
        this.playerScores = playerScores;
    }

    public void addPlayer(String username) {
        if (gameType == GameType.SNAKE) {
            // Add a new snake for the player
            snakes.put(username, new Snake(username, new Position(100, 100)));
        } else if (gameType == GameType.PONG) {
            // Add a new paddle for the player
            // Assuming two players for Pong, position paddles accordingly
            if (paddles.size() == 0) {
                paddles.put(username, new Paddle(new Position(50, 300), 16, 128));
            } else if (paddles.size() == 1) {
                paddles.put(username, new Paddle(new Position(750, 300), 16, 128));
            }
            playerScores.put(username, 0);
        }
    }

    public void generateNewFood() {
        int x, y;
        boolean collision;
        do {
            collision = false;
            x = (int) (Math.random() * (800 / 16)) * 16; // Assuming 800 width, 16 unit size
            y = (int) (Math.random() * (600 / 16)) * 16; // Assuming 600 height, 16 unit size
            Position newFoodPosition = new Position(x, y);

            // Check collision with existing snakes
            for (Snake snake : snakes.values()) {
                for (Position segment : snake.getBody()) {
                    if (segment.equals(newFoodPosition)) {
                        collision = true;
                        break;
                    }
                }
                if (collision) break;
            }

            // Check collision with existing food items
            if (!collision) {
                for (Food food : foodItems) {
                    if (food.getPosition().equals(newFoodPosition)) {
                        collision = true;
                        break;
                    }
                }
            }
        } while (collision);

        foodItems.add(new Food(new Position(x, y), 1));
    }
}