package com.arcadehub.shared;

public class InputPayload {
    private String username;
    private String action;
    private int tick;
    private String signature;

    public InputPayload() {}

    public InputPayload(String username, String action, int tick, String signature) {
        this.username = username;
        this.action = action;
        this.tick = tick;
        this.signature = signature;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public int getTick() { return tick; }
    public void setTick(int tick) { this.tick = tick; }

    public String getSignature() { return signature; }
    public void setSignature(String signature) { this.signature = signature; }
}