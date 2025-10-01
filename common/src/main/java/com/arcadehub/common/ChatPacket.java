package com.arcadehub.common;

public class ChatPacket extends Packet {
    private static final long serialVersionUID = 1L;

    private String username;
    private String message;

    public ChatPacket() {
        // Default constructor for serialization
    }

    public ChatPacket(String username, String message) {
        this.username = username;
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
