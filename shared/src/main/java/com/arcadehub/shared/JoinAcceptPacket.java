package com.arcadehub.shared;

public class JoinAcceptPacket implements Packet {
    private final JoinAcceptPayload payload;

    public JoinAcceptPacket(JoinAcceptPayload payload) {
        this.payload = payload;
    }

    public JoinAcceptPayload getPayload() {
        return payload;
    }

    @Override
    public String getType() {
        return "JOIN_ACCEPT";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public String toString() {
        return "JoinAcceptPacket{" +
               "payload=" + payload +
               '}';
    }
}