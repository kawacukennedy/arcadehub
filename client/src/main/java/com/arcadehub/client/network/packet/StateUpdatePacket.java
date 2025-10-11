
package com.arcadehub.client.network.packet;

import com.arcadehub.shared.Ball;
import com.arcadehub.shared.Food;
import com.arcadehub.shared.Paddle;
import com.arcadehub.shared.Snake;

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
