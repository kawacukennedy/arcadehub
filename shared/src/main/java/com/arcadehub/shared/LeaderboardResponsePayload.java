package com.arcadehub.shared;

import java.util.List;

public class LeaderboardResponsePayload {
    private List<Player> leaderboard;

    public LeaderboardResponsePayload() {}

    public LeaderboardResponsePayload(List<Player> leaderboard) {
        this.leaderboard = leaderboard;
    }

    public List<Player> getLeaderboard() { return leaderboard; }
    public void setLeaderboard(List<Player> leaderboard) { this.leaderboard = leaderboard; }
}