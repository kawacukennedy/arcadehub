package com.arcadehub.common;

public class InputPacket extends Packet {
    private static final long serialVersionUID = 1L;

    private String username;
    private Action action;
    private long tick;

    public InputPacket() {
        // Default constructor for serialization
    }

    public InputPacket(String username, Action action, long tick) {
        this.username = username;
        this.action = action;
        this.tick = tick;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public long getTick() {
        return tick;
    }

    public void setTick(long tick) {
        this.tick = tick;
    }

    public enum Action {
        MOVE_UP, MOVE_DOWN, MOVE_LEFT, MOVE_RIGHT, SHOOT, READY, CHAT
    }
}
