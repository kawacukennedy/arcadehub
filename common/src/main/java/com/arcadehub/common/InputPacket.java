package com.arcadehub.common;

import java.io.Serializable;

public class InputPacket implements Packet {
    public enum Action implements Serializable {
        MOVE_UP,
        MOVE_DOWN,
        MOVE_LEFT,
        MOVE_RIGHT,
        SHOOT,
        JUMP,
        CHAT_MESSAGE
    }

    private final Action action;
    private final long timestamp;
    private final String message; // For chat messages

    public InputPacket(Action action, long timestamp) {
        this(action, timestamp, null);
    }

    public InputPacket(Action action, long timestamp, String message) {
        this.action = action;
        this.timestamp = timestamp;
        this.message = message;
    }

    public Action getAction() {
        return action;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "InputPacket{" +
               "action=" + action +
               ", timestamp=" + timestamp +
               ", message='" + message + "'" +
               '}';
    }
}