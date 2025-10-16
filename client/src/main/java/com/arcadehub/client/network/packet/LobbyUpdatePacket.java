
package com.arcadehub.client.network.packet;

import com.arcadehub.shared.Lobby;

public class LobbyUpdatePacket extends BasePacket {

    private Lobby lobby;

    public LobbyUpdatePacket() {
        super(PacketType.LOBBY_UPDATE);
    }

    public Lobby getLobby() {
        return lobby;
    }

    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }
}
