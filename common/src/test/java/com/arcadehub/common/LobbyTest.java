package com.arcadehub.common;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.UUID;

public class LobbyTest {

    @Test
    void testLobbyConstructorAndGetters() {
        UUID id = UUID.randomUUID();
        String name = "Test Lobby";
        GameType gameType = GameType.SNAKE;
        int maxPlayers = 4;
        String host = "hostUser";

        Lobby lobby = new Lobby(id, name, gameType, maxPlayers, host);

        assertEquals(id, lobby.getId());
        assertEquals(name, lobby.getName());
        assertEquals(gameType, lobby.getGameType());
        assertEquals(maxPlayers, lobby.getMaxPlayers());
        assertEquals(host, lobby.getHost());
        assertFalse(lobby.isGameStarted());
        assertTrue(lobby.getPlayers().isEmpty());
    }

    @Test
    void testAddPlayer() {
        Lobby lobby = new Lobby(UUID.randomUUID(), "Test Lobby", GameType.PONG, 2, "hostUser");
        Player player1 = new Player(UUID.randomUUID(), "player1", 1000, 0, 0, Instant.now());
        Player player2 = new Player(UUID.randomUUID(), "player2", 1000, 0, 0, Instant.now());
        Player player3 = new Player(UUID.randomUUID(), "player3", 1000, 0, 0, Instant.now());

        assertTrue(lobby.addPlayer(player1));
        assertEquals(1, lobby.getPlayers().size());
        assertTrue(lobby.getPlayers().contains(player1));

        assertTrue(lobby.addPlayer(player2));
        assertEquals(2, lobby.getPlayers().size());
        assertTrue(lobby.getPlayers().contains(player2));

        // Lobby is full, should not add player3
        assertFalse(lobby.addPlayer(player3));
        assertEquals(2, lobby.getPlayers().size());
    }

    @Test
    void testRemovePlayer() {
        Lobby lobby = new Lobby(UUID.randomUUID(), "Test Lobby", GameType.SNAKE, 2, "hostUser");
        Player player1 = new Player(UUID.randomUUID(), "player1", 1000, 0, 0, Instant.now());
        Player player2 = new Player(UUID.randomUUID(), "player2", 1000, 0, 0, Instant.now());

        lobby.addPlayer(player1);
        lobby.addPlayer(player2);

        assertTrue(lobby.removePlayer(player1));
        assertEquals(1, lobby.getPlayers().size());
        assertFalse(lobby.getPlayers().contains(player1));

        assertFalse(lobby.removePlayer(new Player(UUID.randomUUID(), "nonExistent", 0, 0, 0, null))); // Remove non-existent player
        assertEquals(1, lobby.getPlayers().size());
    }

    @Test
    void testGameStartedPreventsAddingPlayers() {
        Lobby lobby = new Lobby(UUID.randomUUID(), "Test Lobby", GameType.PONG, 2, "hostUser");
        Player player1 = new Player(UUID.randomUUID(), "player1", 1000, 0, 0, Instant.now());
        Player player2 = new Player(UUID.randomUUID(), "player2", 1000, 0, 0, Instant.now());

        lobby.addPlayer(player1);
        lobby.setGameStarted(true);

        assertFalse(lobby.addPlayer(player2)); // Should not add if game started
        assertEquals(1, lobby.getPlayers().size());
    }
}
