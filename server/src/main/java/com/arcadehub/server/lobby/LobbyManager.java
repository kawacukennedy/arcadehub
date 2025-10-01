package com.arcadehub.server.lobby;

import com.arcadehub.common.Lobby;
import com.arcadehub.common.GameType;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Manages lobby lifecycle, broadcasts state, cleans up inactive lobbies.
 */
public class LobbyManager {
    private ConcurrentHashMap<UUID, Lobby> activeLobbies = new ConcurrentHashMap<>();
    private ScheduledExecutorService cleanupScheduler;

    public UUID createLobby(String name, String host, GameType gameType) {
        // Implementation for creating a lobby
        Lobby newLobby = new Lobby(name, host, gameType);
        activeLobbies.put(newLobby.getId(), newLobby);
        System.out.println("Lobby " + name + " created with ID " + newLobby.getId());
        return newLobby.getId();
    }

    public void joinLobby(UUID lobbyId, String username) {
        Lobby lobby = activeLobbies.get(lobbyId);
        if (lobby != null) {
            // Placeholder: Add user to lobby, broadcast update
            System.out.println(username + " joined lobby " + lobby.getName() + " (placeholder)");
        } else {
            System.out.println("Lobby " + lobbyId + " not found.");
        }
    }

    public void removeLobby(UUID lobbyId) {
        Lobby removedLobby = activeLobbies.remove(lobbyId);
        if (removedLobby != null) {
            System.out.println("Lobby " + removedLobby.getName() + " removed (placeholder)");
        } else {
            System.out.println("Lobby " + lobbyId + " not found for removal.");
        }
    }

    public Lobby getLobby(UUID lobbyId) {
        return activeLobbies.get(lobbyId);
    }
}