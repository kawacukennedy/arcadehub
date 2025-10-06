package com.arcadehub.common;

import java.io.Serializable;

/**
 * Represents a packet sent from the server to the client to update the game state.
 * This is a marker interface/base class and should be extended for specific game state updates.
 */
public class StateUpdatePacket implements Packet {
    private final long timestamp;

    public StateUpdatePacket(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "StateUpdatePacket{" +
               "timestamp=" + timestamp +
               '}';
    }
}