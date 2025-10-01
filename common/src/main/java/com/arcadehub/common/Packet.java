package com.arcadehub.common;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.io.Serializable;

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
    @JsonSubTypes.Type(value = LobbyUpdatePacket.class, name = "LOBBY_UPDATE"),
    @JsonSubTypes.Type(value = LeaderboardResponsePacket.class, name = "LEADERBOARD_RESPONSE")
})
public abstract class Packet implements Serializable {
    private static final long serialVersionUID = 1L;

    // Common fields for all packets can go here if any
}
