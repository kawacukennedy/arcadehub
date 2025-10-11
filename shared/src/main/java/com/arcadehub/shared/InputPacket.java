package com.arcadehub.shared;

/**
 * Input packet for client->server input transmission.
 * Uses InputPayload for the actual data.
 */
public class InputPacket implements Packet {
    private final InputPayload payload;

    public InputPacket(InputPayload payload) {
        this.payload = payload;
    }

    public InputPayload getPayload() {
        return payload;
    }

    @Override
    public String getType() {
        return "INPUT";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public Object getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return "InputPacket{" +
               "payload=" + payload +
               '}';
    }
}