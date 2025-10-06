package com.arcadehub.common;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class Player implements Serializable {
    private UUID id;
    private String username;
    private int elo;
    private int wins;
    private int losses;
    private Instant lastActive;

    public Player(UUID id, String username, int elo, int wins, int losses, Instant lastActive) {
        this.id = id;
        this.username = username;
        this.elo = elo;
        this.wins = wins;
        this.losses = losses;
        this.lastActive = lastActive;
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public int getElo() {
        return elo;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public Instant getLastActive() {
        return lastActive;
    }

    // Setters (if needed, for example, when updating player stats)
    public void setElo(int elo) {
        this.elo = elo;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public void setLastActive(Instant lastActive) {
        this.lastActive = lastActive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(id, player.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Player{" +
               "id=" + id +
               ", username='" + username + '\'' +
               ", elo=" + elo +
               ", wins=" + wins +
               ", losses=" + losses +
               ", lastActive=" + lastActive +
               '}';
    }
}
