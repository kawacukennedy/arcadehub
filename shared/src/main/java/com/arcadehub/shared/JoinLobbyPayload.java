package com.arcadehub.shared;

public class JoinLobbyPayload {
    private String username;
    private String lobbyId; // optional, null for matchmaking, format: uuid
    private String clientVersion;

    public JoinLobbyPayload() {}

    public JoinLobbyPayload(String username, String lobbyId, String clientVersion) {
        this.username = username;
        this.lobbyId = lobbyId;
        this.clientVersion = clientVersion;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getLobbyId() { return lobbyId; }
    public void setLobbyId(String lobbyId) { this.lobbyId = lobbyId; }

    public String getClientVersion() { return clientVersion; }
    public void setClientVersion(String clientVersion) { this.clientVersion = clientVersion; }
}