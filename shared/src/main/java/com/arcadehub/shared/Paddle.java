package com.arcadehub.shared;

import java.io.Serializable;

public class Paddle implements Serializable {
    private Position position;
    private float width;
    private float height;

    public Paddle() {
        // Placeholder constructor
    }

    public Paddle(Position position, float width, float height) {
        this.position = position;
        this.width = width;
        this.height = height;
    }

    public Position getPosition() {
        return position;
    }
}
