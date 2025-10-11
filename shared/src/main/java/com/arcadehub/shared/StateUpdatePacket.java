package com.arcadehub.shared;

/**
 * State update packet sent from server to client.
 */
public class StateUpdatePacket implements Packet {
    private final StateUpdatePayload payload;

    public StateUpdatePacket(StateUpdatePayload payload) {
        this.payload = payload;
    }

    public StateUpdatePayload getPayload() {
        return payload;
    }

    @Override
    public String getType() {
        return "STATE_UPDATE";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public String toString() {
        return "StateUpdatePacket{" +
               "payload=" + payload +
               '}';
    }
}