package com.arcadehub.common;

import java.io.Serializable;
import java.util.List;

public class LeaderboardResponsePacket implements Packet {
    private final List<Player> leaderboard;

    public LeaderboardResponsePacket(List<Player> leaderboard) {
        this.leaderboard = leaderboard;
    }

    public List<Player> getLeaderboard() {
        return leaderboard;
    }

    @Override
    public String toString() {
        return "LeaderboardResponsePacket{" +
               "leaderboard=" + leaderboard +
               '}';
    }
}