package com.arcadehub.common;

public class HeartbeatPacket extends Packet {
    private static final long serialVersionUID = 1L;

    private long timestamp;

    public HeartbeatPacket() {
        // Default constructor for serialization
    }

    public HeartbeatPacket(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
