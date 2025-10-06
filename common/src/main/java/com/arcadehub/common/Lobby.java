package com.arcadehub.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Lobby implements Serializable {
    private UUID id;
    private String name;
    private GameType gameType;
    private List<Player> players;
    private int maxPlayers;
    private boolean gameStarted;
    private String host; // New field for host username

    public Lobby(UUID id, String name, GameType gameType, int maxPlayers, String host) {
        this.id = id;
        this.name = name;
        this.gameType = gameType;
        this.players = new ArrayList<>();
        this.maxPlayers = maxPlayers;
        this.gameStarted = false;
        this.host = host;
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public GameType getGameType() {
        return gameType;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public String getHost() {
        return host;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public boolean addPlayer(Player player) {
        if (players.size() < maxPlayers && !gameStarted) {
            return players.add(player);
        }
        return false;
    }

    public boolean removePlayer(Player player) {
        return players.remove(player);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lobby lobby = (Lobby) o;
        return Objects.equals(id, lobby.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Lobby{"
               + "id=" + id + ", "
               + "name='" + name + "'" + ", "
               + "gameType=" + gameType + ", "
               + "players=" + players + ", "
               + "maxPlayers=" + maxPlayers + ", "
               + "gameStarted=" + gameStarted + 
               '}';
    }
}
