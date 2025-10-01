
package com.arcadehub.client.network.packet;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = JoinLobbyPacket.class, name = "JOIN_LOBBY"),
    @JsonSubTypes.Type(value = StateUpdatePacket.class, name = "STATE_UPDATE"),
    @JsonSubTypes.Type(value = InputPacket.class, name = "INPUT"),
    @JsonSubTypes.Type(value = ChatPacket.class, name = "CHAT"),
    @JsonSubTypes.Type(value = HeartbeatPacket.class, name = "HEARTBEAT"),
    @JsonSubTypes.Type(value = ReadyPacket.class, name = "READY"),
    @JsonSubTypes.Type(value = LeaderboardRequestPacket.class, name = "LEADERBOARD_REQUEST"),
    @JsonSubTypes.Type(value = LeaderboardResponsePacket.class, name = "LEADERBOARD_RESPONSE"),
    @JsonSubTypes.Type(value = LobbyUpdatePacket.class, name = "LOBBY_UPDATE")
})
public abstract class BasePacket implements Packet {

    private final PacketType type;

    public BasePacket(PacketType type) {
        this.type = type;
    }

    public PacketType getType() {
        return type;
    }
}
