package com.arcadehub.shared;

public class LeaderboardRequestPacket implements Packet {
    // Empty payload for request
    public LeaderboardRequestPacket() {
    }

    @Override
    public String getType() {
        return "LEADERBOARD_REQUEST";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public String toString() {
        return "LeaderboardRequestPacket{}";
    }
}