package com.arcadehub.shared;

import java.util.List;

public class LobbyUpdatePayload {
    private List<Lobby> lobbies;

    public LobbyUpdatePayload() {}

    public LobbyUpdatePayload(List<Lobby> lobbies) {
        this.lobbies = lobbies;
    }

    public List<Lobby> getLobbies() { return lobbies; }
    public void setLobbies(List<Lobby> lobbies) { this.lobbies = lobbies; }
}