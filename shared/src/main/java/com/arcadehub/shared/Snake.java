package com.arcadehub.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Snake implements Serializable {
    private String username;
    private List<Position> body;
    private Direction direction;

    public Snake() {
        // Default constructor for serialization
    }

    public Snake(String username, List<Position> body, Direction direction) {
        this.username = username;
        this.body = body;
        this.direction = direction;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Position> getBody() {
        return body;
    }

    public void setBody(List<Position> body) {
        this.body = body;
    }

    public Position getHeadPosition() {
        return body.get(0);
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}