package com.arcadehub.server.game;

import com.arcadehub.common.GameState;
import com.arcadehub.common.GameType;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Executes game logic 20 ticks/sec per lobby, updates positions, handles collisions, broadcasts STATE_UPDATE.
 */
public class GameLoopManager {
    private ScheduledExecutorService gameLoopExecutor;
    private Map<UUID, GameState> lobbyStates;

    public GameLoopManager() {
        this.gameLoopExecutor = Executors.newSingleThreadScheduledExecutor();
        this.lobbyStates = new java.util.concurrent.ConcurrentHashMap<>();
    }

    public void startLoop(UUID lobbyId, GameType gameType) {
        if (!lobbyStates.containsKey(lobbyId)) {
            lobbyStates.put(lobbyId, new GameState(lobbyId, gameType)); // Initialize new GameState for the lobby
            gameLoopExecutor.scheduleAtFixedRate(() -> tick(lobbyId), 0, 50, TimeUnit.MILLISECONDS); // 20 ticks/sec
            System.out.println("Game loop started for lobby: " + lobbyId);
        } else {
            System.out.println("Game loop already running for lobby: " + lobbyId);
        }
    }

    private void tick(UUID lobbyId) {
        GameState state = lobbyStates.get(lobbyId);
        if (state != null) {
            // Placeholder: Update positions, handle collisions, etc.
            System.out.println("Lobby " + lobbyId + " ticking...");
            // Placeholder: Broadcast STATE_UPDATE to clients
            // networkServer.broadcast(lobbyId, new StateUpdatePacket(state));
        } else {
            System.out.println("No game state found for lobby: " + lobbyId);
            // Optionally stop the loop if state is null
        }
    }

    public GameState getGameState(UUID lobbyId) {
        return lobbyStates.get(lobbyId);
    }
}
