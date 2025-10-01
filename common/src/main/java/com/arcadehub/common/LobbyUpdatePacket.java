package com.arcadehub.common;

import java.io.Serializable;

public class LobbyUpdatePacket extends Packet implements Serializable {
    private static final long serialVersionUID = 1L;

    private Lobby lobby;

    public LobbyUpdatePacket() {
        // Default constructor for serialization
    }

    public LobbyUpdatePacket(Lobby lobby) {
        this.lobby = lobby;
    }

    public Lobby getLobby() {
        return lobby;
    }

    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }
}
