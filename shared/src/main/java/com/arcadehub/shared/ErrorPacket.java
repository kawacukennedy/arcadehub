package com.arcadehub.shared;

public class ErrorPacket implements Packet {
    private final ErrorPayload payload;

    public ErrorPacket(ErrorPayload payload) {
        this.payload = payload;
    }

    public ErrorPayload getPayload() {
        return payload;
    }

    @Override
    public String getType() {
        return "ERROR";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public String toString() {
        return "ErrorPacket{" +
               "payload=" + payload +
               '}';
    }
}