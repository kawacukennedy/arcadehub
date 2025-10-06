package com.arcadehub.common;

import java.io.Serializable;

public class ChatPacket implements Packet {
    private final String senderUsername;
    private final String message;
    private final long timestamp;

    public ChatPacket(String senderUsername, String message, long timestamp) {
        this.senderUsername = senderUsername;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public String getMessage() {
        return message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "ChatPacket{" +
               "senderUsername='" + senderUsername + "'" +
               ", message='" + message + "'" +
               ", timestamp=" + timestamp +
               '}';
    }
}