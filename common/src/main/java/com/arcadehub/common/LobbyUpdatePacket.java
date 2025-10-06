package com.arcadehub.common;

import java.io.Serializable;
import java.util.List;

public class LobbyUpdatePacket implements Packet {
    private final List<Lobby> lobbies;

    public LobbyUpdatePacket(List<Lobby> lobbies) {
        this.lobbies = lobbies;
    }

    public List<Lobby> getLobbies() {
        return lobbies;
    }

    @Override
    public String toString() {
        return "LobbyUpdatePacket{" +
               "lobbies=" + lobbies +
               '}';
    }
}