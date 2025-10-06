package com.arcadehub.server.lobby;

import com.arcadehub.common.GameType;
import com.arcadehub.common.Lobby;
import com.arcadehub.common.Player;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class LobbyManager {
    private static final Logger logger = LoggerFactory.getLogger(LobbyManager.class);
    private final Map<UUID, Lobby> activeLobbies;
    private final Map<String, UUID> playerLobbyMap; // Player username to Lobby ID

    public LobbyManager() {
        this.activeLobbies = new ConcurrentHashMap<>();
        this.playerLobbyMap = new ConcurrentHashMap<>();
    }

    public Lobby createLobby(String lobbyName, GameType gameType, int maxPlayers, String host) {
        UUID lobbyId = UUID.randomUUID();
        Lobby lobby = new Lobby(lobbyId, lobbyName, gameType, maxPlayers, host);
        activeLobbies.put(lobbyId, lobby);
        logger.info("Lobby created: {}", lobby);
        return lobby;
    }

    public boolean joinLobby(UUID lobbyId, Player player, ChannelHandlerContext ctx) {
        Lobby lobby = activeLobbies.get(lobbyId);
        if (lobby == null) {
            logger.warn("Attempted to join non-existent lobby: {}", lobbyId);
            return false;
        }
        if (lobby.isGameStarted()) {
            logger.warn("Attempted to join started game in lobby: {}", lobbyId);
            return false;
        }
        if (lobby.getPlayers().size() >= lobby.getMaxPlayers()) {
            logger.warn("Attempted to join full lobby: {}", lobbyId);
            return false;
        }

        if (lobby.addPlayer(player)) {
            playerLobbyMap.put(player.getUsername(), lobbyId);
            logger.info("Player {} joined lobby {}", player.getUsername(), lobbyId);
            // TODO: Notify all players in the lobby about the new player
            return true;
        }
        return false;
    }

    public void leaveLobby(String username) {
        UUID lobbyId = playerLobbyMap.remove(username);
        if (lobbyId != null) {
            Lobby lobby = activeLobbies.get(lobbyId);
            if (lobby != null) {
                lobby.removePlayer(new Player(null, username, 0, 0, 0, null)); // Create a dummy player for removal
                logger.info("Player {} left lobby {}", username, lobbyId);
                if (lobby.getPlayers().isEmpty()) {
                    activeLobbies.remove(lobbyId);
                    logger.info("Lobby {} is empty and removed.", lobbyId);
                }
                // TODO: Notify all players in the lobby about the player leaving
            }
        }
    }

    public Lobby getLobbyById(UUID lobbyId) {
        return activeLobbies.get(lobbyId);
    }

    public Lobby getLobbyByPlayerUsername(String username) {
        UUID lobbyId = playerLobbyMap.get(username);
        return lobbyId != null ? activeLobbies.get(lobbyId) : null;
    }

    public Map<UUID, Lobby> getActiveLobbies() {
        return Collections.unmodifiableMap(activeLobbies);
    }
}
