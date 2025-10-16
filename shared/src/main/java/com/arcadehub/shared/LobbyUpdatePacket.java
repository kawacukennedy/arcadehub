package com.arcadehub.shared;

import java.util.List;

public class LobbyUpdatePacket implements Packet {
    private final LobbyUpdatePayload payload;

    public LobbyUpdatePacket(LobbyUpdatePayload payload) {
        this.payload = payload;
    }

    public LobbyUpdatePayload getPayload() {
        return payload;
    }

    @Override
    public String getType() {
        return "LOBBY_UPDATE";
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
        return "LobbyUpdatePacket{" +
               "payload=" + payload +
               '}';
    }
}