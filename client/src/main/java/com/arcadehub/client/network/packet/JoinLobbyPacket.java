
package com.arcadehub.client.network.packet;

import java.util.UUID;

public class JoinLobbyPacket extends BasePacket {

    private String username;
    private UUID lobbyId;

    public JoinLobbyPacket() {
        super(PacketType.JOIN_LOBBY);
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
