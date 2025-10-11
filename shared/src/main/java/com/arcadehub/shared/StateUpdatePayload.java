package com.arcadehub.shared;

public class StateUpdatePayload {
    private GameState state;
    private long serverTimestamp;

    public StateUpdatePayload() {}

    public StateUpdatePayload(GameState state, long serverTimestamp) {
        this.state = state;
        this.serverTimestamp = serverTimestamp;
    }

    public GameState getState() { return state; }
    public void setState(GameState state) { this.state = state; }

    public long getServerTimestamp() { return serverTimestamp; }
    public void setServerTimestamp(long serverTimestamp) { this.serverTimestamp = serverTimestamp; }
}