package com.arcadehub.shared;

public enum PacketType {
    JOIN_REQUEST,
    JOIN_RESPONSE,
    LEAVE,
    INPUT,
    STATE_UPDATE,
    CHAT,
    HEARTBEAT,
    MATCH_RESULT,
    ERROR,
    ADMIN_CMD
}