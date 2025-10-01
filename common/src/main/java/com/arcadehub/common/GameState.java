package com.arcadehub.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;

    private long tick;
    private Map<String, Snake> snakes;
    private List<Food> foodItems;
    private Ball pongBall;
    private Map<String, Paddle> paddles;
    private UUID lobbyId;
    private GameType gameType;

    public GameState() {
        // Default constructor for serialization
        this.snakes = new HashMap<>();
        this.foodItems = new ArrayList<>();
        this.paddles = new HashMap<>();
    }

    public GameState(UUID lobbyId, GameType gameType) {
        this(); // Call default constructor to initialize collections
        this.lobbyId = lobbyId;
        this.gameType = gameType;
    }

    public GameState(long tick, Map<String, Snake> snakes, List<Food> foodItems, Ball pongBall, Map<String, Paddle> paddles, UUID lobbyId, GameType gameType) {
        this.tick = tick;
        this.snakes = snakes;
        this.foodItems = foodItems;
        this.pongBall = pongBall;
        this.paddles = paddles;
        this.lobbyId = lobbyId;
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
}
