package com.arcadehub.server.network.packet;

import com.arcadehub.common.Ball;
import com.arcadehub.common.Food;
import com.arcadehub.common.Paddle;
import com.arcadehub.common.Snake;
import com.arcadehub.common.Position;

import java.util.List;
import java.util.Map;
import java.util.Map;

public class StateUpdatePacket extends BasePacket {

    private long tick;
    private List<Snake> snakes;
    private List<Paddle> paddles;
    private List<Object> balls;
    private List<Food> foodItems;
    private Ball ball;
    private Map<String, Integer> playerScores;
    private Position eatenFoodPosition;
    private Position collisionPosition;

    public StateUpdatePacket() {
        super(PacketType.STATE_UPDATE);
    }

    public long getTick() {
        return tick;
    }

    public void setTick(long tick) {
        this.tick = tick;
    }

    public List<Snake> getSnakes() {
        return snakes;
    }

    public void setSnakes(List<Snake> snakes) {
        this.snakes = snakes;
    }

    public List<Paddle> getPaddles() {
        return paddles;
    }

    public void setPaddles(List<Paddle> paddles) {
        this.paddles = paddles;
    }

    public List<Object> getBalls() {
        return balls;
    }

    public void setBalls(List<Object> balls) {
        this.balls = balls;
    }

    public List<Food> getFoodItems() {
        return foodItems;
    }

    public void setFoodItems(List<Food> foodItems) {
        this.foodItems = foodItems;
    }

    public Ball getBall() {
        return ball;
    }

    public void setBall(Ball ball) {
        this.ball = ball;
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

    public Map<String, Integer> getPlayerScores() {
        return playerScores;
    }

    public void setPlayerScores(Map<String, Integer> playerScores) {
        this.playerScores = playerScores;
    }
}