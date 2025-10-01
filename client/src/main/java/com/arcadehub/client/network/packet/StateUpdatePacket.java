
package com.arcadehub.client.network.packet;

import com.arcadehub.common.Ball;
import com.arcadehub.common.Food;
import com.arcadehub.common.Paddle;
import com.arcadehub.common.Snake;

import java.util.List;

public class StateUpdatePacket extends BasePacket {

    private long tick;
    private List<Snake> snakes;
    private List<Paddle> paddles;
    private List<Ball> balls;
    private List<Food> foodItems;
    private Ball ball;

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

    public Ball getBall() {
        return ball;
    }

    public void setBall(Ball ball) {
        this.ball = ball;
    }
}
