package com.arcadehub.shared;

public class ChatPacket implements Packet {
    private final ChatPayload payload;

    public ChatPacket(ChatPayload payload) {
        this.payload = payload;
    }

    public ChatPayload getPayload() {
        return payload;
    }

    @Override
    public String getType() {
        return "CHAT";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public String toString() {
        return "ChatPacket{" +
               "payload=" + payload +
               '}';
    }
}