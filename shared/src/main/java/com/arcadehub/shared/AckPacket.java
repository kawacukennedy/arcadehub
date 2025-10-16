package com.arcadehub.shared;

public class AckPacket implements Packet {
    private final AckPayload payload;

    public AckPacket(AckPayload payload) {
        this.payload = payload;
    }

    public AckPayload getPayload() {
        return payload;
    }

    @Override
    public String getType() {
        return "ACK";
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
        return "AckPacket{" +
               "payload=" + payload +
               '}';
    }
}