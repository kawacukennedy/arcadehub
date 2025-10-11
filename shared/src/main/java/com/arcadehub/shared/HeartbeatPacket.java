package com.arcadehub.shared;

public class HeartbeatPacket implements Packet {
    private final HeartbeatPayload payload;

    public HeartbeatPacket(HeartbeatPayload payload) {
        this.payload = payload;
    }

    public HeartbeatPayload getPayload() {
        return payload;
    }

    @Override
    public String getType() {
        return "HEARTBEAT";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public String toString() {
        return "HeartbeatPacket{" +
               "payload=" + payload +
               '}';
    }
}