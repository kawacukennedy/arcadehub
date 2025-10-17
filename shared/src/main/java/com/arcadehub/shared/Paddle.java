package com.arcadehub.shared;

import java.io.Serializable;

public class Paddle implements Serializable {
    private Position position;
    private float width;
    private float height;
    private String username;

    public Paddle() {
        // Placeholder constructor
    }

    public Paddle(Position position, float width, float height, String username) {
        this.position = position;
        this.width = width;
        this.height = height;
        this.username = username;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
