package com.arcadehub.common;

import java.util.List;
import java.util.Map;
import com.arcadehub.common.Position;

public class StateUpdatePacket extends Packet {
    private static final long serialVersionUID = 1L;

    private long tick;
    private Map<String, Snake> snakes;
    private Map<String, Paddle> paddles;
    private List<Ball> balls;
    private List<Food> foodItems;
    private Position eatenFoodPosition;
    private Position collisionPosition;

    public StateUpdatePacket() {
        // Default constructor for serialization
    }

    public StateUpdatePacket(long tick, Map<String, Snake> snakes, Map<String, Paddle> paddles, List<Ball> balls, List<Food> foodItems, Position eatenFoodPosition, Position collisionPosition) {
        this.tick = tick;
        this.snakes = snakes;
        this.paddles = paddles;
        this.balls = balls;
        this.foodItems = foodItems;
        this.eatenFoodPosition = eatenFoodPosition;
        this.collisionPosition = collisionPosition;
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

    public Map<String, Paddle> getPaddles() {
        return paddles;
    }

    public void setPaddles(Map<String, Paddle> paddles) {
        this.paddles = paddles;
    }

    public List<Ball> getBalls() {
        return balls;
    }

    public void setBalls(List<Ball> balls) {
        this.balls = balls;
    }

    public List<Food> getFoodItems() {
        return foodItems;
    }

    public void setFoodItems(List<Food> foodItems) {
        this.foodItems = foodItems;
    }

    public Position getEatenFoodPosition() {
        return eatenFoodPosition;
    }

    public void setEatenFoodPosition(Position eatenFoodPosition) {
        this.eatenFoodPosition = eatenFoodPosition;
    }

    public Position getCollisionPosition() {
        return collisionPosition;
    }

    public void setCollisionPosition(Position collisionPosition) {
        this.collisionPosition = collisionPosition;
    }

    public GameState getGameState() {
        // Assuming GameState constructor can take these parameters
        // Note: GameType and LobbyId are missing here, might need to be added to StateUpdatePacket if they are part of GameState
        return new GameState(tick, snakes, foodItems, null, paddles, null, null); // Placeholder for Ball, LobbyId, GameType
    }
}
