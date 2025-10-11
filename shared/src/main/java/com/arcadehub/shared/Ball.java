package com.arcadehub.shared;

import java.io.Serializable;

public class Ball implements Serializable {
    private Position position;
    private float velocityX;
    private float velocityY;
    private float radius;

    public Ball() {
        // Default constructor for serialization
    }

    public Ball(Position position, float velocityX, float velocityY, float radius) {
        this.position = position;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.radius = radius;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public float getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(float velocityX) {
        this.velocityX = velocityX;
    }

    public float getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(float velocityY) {
        this.velocityY = velocityY;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }
}
