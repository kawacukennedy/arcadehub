package com.arcadehub.common;

import java.io.Serializable;
import java.util.UUID;

public class JoinLobbyPacket implements Packet {
    private final UUID lobbyId;
    private final String username;

    public JoinLobbyPacket(UUID lobbyId, String username) {
        this.lobbyId = lobbyId;
        this.username = username;
    }

    public UUID getLobbyId() {
        return lobbyId;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "JoinLobbyPacket{" +
               "lobbyId=" + lobbyId +
               ", username='" + username + "'" +
               '}';
    }
}