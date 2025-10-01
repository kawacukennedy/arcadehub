package com.arcadehub.common;

import java.io.Serializable;

public class Food implements Serializable {
    private Position position;
    private int type;

    public Food() {
        // Default constructor for serialization
    }

    public Food(Position position, int type) {
        this.position = position;
        this.type = type;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
