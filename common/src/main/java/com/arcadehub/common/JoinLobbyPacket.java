package com.arcadehub.common;

import java.util.UUID;

public class JoinLobbyPacket extends Packet {
    private static final long serialVersionUID = 1L;

    private String username;
    private UUID lobbyId;

    public JoinLobbyPacket() {
        // Default constructor for serialization
    }

    public JoinLobbyPacket(String username, UUID lobbyId) {
        this.username = username;
        this.lobbyId = lobbyId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UUID getLobbyId() {
        return lobbyId;
    }

    public void setLobbyId(UUID lobbyId) {
        this.lobbyId = lobbyId;
    }
}
