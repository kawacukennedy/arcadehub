package com.arcadehub.shared;

public class JoinLobbyPacket implements Packet {
    private final JoinLobbyPayload payload;

    public JoinLobbyPacket(JoinLobbyPayload payload) {
        this.payload = payload;
    }

    public JoinLobbyPayload getPayload() {
        return payload;
    }

    @Override
    public String getType() {
        return "JOIN_LOBBY";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public String toString() {
        return "JoinLobbyPacket{" +
               "payload=" + payload +
               '}';
    }
}