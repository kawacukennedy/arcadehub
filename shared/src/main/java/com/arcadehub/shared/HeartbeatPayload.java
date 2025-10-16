package com.arcadehub.shared;

public class HeartbeatPayload {
    private long timestamp;

    public HeartbeatPayload() {}

    public HeartbeatPayload(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}