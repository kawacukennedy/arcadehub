package com.arcadehub.shared;

import java.util.List;

public class LeaderboardResponsePacket implements Packet {
    private final LeaderboardResponsePayload payload;

    public LeaderboardResponsePacket(LeaderboardResponsePayload payload) {
        this.payload = payload;
    }

    public LeaderboardResponsePayload getPayload() {
        return payload;
    }

    @Override
    public String getType() {
        return "LEADERBOARD_RESPONSE";
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public String toString() {
        return "LeaderboardResponsePacket{" +
               "payload=" + payload +
               '}';
    }
}