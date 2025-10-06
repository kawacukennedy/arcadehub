package com.arcadehub.common;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.UUID;

public class PlayerTest {

    @Test
    void testPlayerConstructorAndGetters() {
        UUID id = UUID.randomUUID();
        String username = "testUser";
        int elo = 1200;
        int wins = 10;
        int losses = 5;
        Instant lastActive = Instant.now();

        Player player = new Player(id, username, elo, wins, losses, lastActive);

        assertEquals(id, player.getId());
        assertEquals(username, player.getUsername());
        assertEquals(elo, player.getElo());
        assertEquals(wins, player.getWins());
        assertEquals(losses, player.getLosses());
        assertEquals(lastActive, player.getLastActive());
    }

    @Test
    void testPlayerSetters() {
        Player player = new Player(UUID.randomUUID(), "initialUser", 1000, 0, 0, Instant.now());

        player.setElo(1500);
        player.setWins(1);
        player.setLosses(1);
        Instant newLastActive = Instant.now().plusSeconds(3600);
        player.setLastActive(newLastActive);

        assertEquals(1500, player.getElo());
        assertEquals(1, player.getWins());
        assertEquals(1, player.getLosses());
        assertEquals(newLastActive, player.getLastActive());
    }

    @Test
    void testPlayerEqualsAndHashCode() {
        UUID id1 = UUID.randomUUID();
        Player player1 = new Player(id1, "user1", 1000, 0, 0, Instant.now());
        Player player2 = new Player(id1, "user1", 1000, 0, 0, Instant.now()); // Same ID
        Player player3 = new Player(UUID.randomUUID(), "user2", 1100, 1, 0, Instant.now()); // Different ID

        assertEquals(player1, player2);
        assertEquals(player1.hashCode(), player2.hashCode());
        assertNotEquals(player1, player3);
        assertNotEquals(player1.hashCode(), player3.hashCode());
    }
}
