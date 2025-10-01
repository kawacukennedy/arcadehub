package com.arcadehub.server.db;

import com.arcadehub.common.Player;

import javax.sql.DataSource;
import java.util.List;

/**
 * Handles leaderboard queries and ELO updates using transactional DB operations.
 */
public class LeaderboardManager {
    private DataSource dbPool;

    public LeaderboardManager(DataSource dbPool) {
        this.dbPool = dbPool;
    }

    public void updateElo(String username, int delta) {
        // Placeholder: Update ELO in database
        System.out.println("Updating ELO for " + username + " by " + delta + " (placeholder)");
        // try (Connection conn = dbPool.getConnection()) {
        //     // SQL update statement
        // }
    }

    public List<Player> getTopPlayers(int limit) {
        // Placeholder: Retrieve top players from database
        System.out.println("Retrieving top " + limit + " players (placeholder)");
        // Example: return a dummy list of players
        return java.util.Arrays.asList(
                new Player(java.util.UUID.randomUUID(), "Player1", 1500),
                new Player(java.util.UUID.randomUUID(), "Player2", 1400),
                new Player(java.util.UUID.randomUUID(), "Player3", 1300)
        );
    }
}
