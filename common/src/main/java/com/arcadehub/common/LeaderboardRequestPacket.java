package com.arcadehub.common;

import java.io.Serializable;

public class LeaderboardRequestPacket implements Packet {
    // No specific fields needed for a simple request, but can be extended later
    public LeaderboardRequestPacket() {
    }

    @Override
    public String toString() {
        return "LeaderboardRequestPacket{}";
    }
}