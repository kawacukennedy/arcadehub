package com.arcadehub.common;

import java.io.Serializable;

public class HeartbeatPacket implements Packet {
    private final long timestamp;

    public HeartbeatPacket(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "HeartbeatPacket{" +
               "timestamp=" + timestamp +
               '}';
    }
}