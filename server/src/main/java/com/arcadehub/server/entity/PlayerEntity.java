package com.arcadehub.server.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "players")
public class PlayerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(unique = true, nullable = false, length = 32)
    private String username;

    @Column(nullable = false)
    private int elo;

    @Column(nullable = false)
    private int wins;

    @Column(nullable = false)
    private int losses;

    @Column(name = "last_active")
    private Instant lastActive;

    // Constructors
    public PlayerEntity() {
    }

    public PlayerEntity(String username, int elo, int wins, int losses, Instant lastActive) {
        this.username = username;
        this.elo = elo;
        this.wins = wins;
        this.losses = losses;
        this.lastActive = lastActive;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getElo() {
        return elo;
    }

    public void setElo(int elo) {
        this.elo = elo;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public Instant getLastActive() {
        return lastActive;
    }

    public void setLastActive(Instant lastActive) {
        this.lastActive = lastActive;
    }

    @PrePersist
    protected void onCreate() {
        if (id == null) {
            id = UUID.randomUUID();
        }
        if (lastActive == null) {
            lastActive = Instant.now();
        }
        if (elo == 0) {
            elo = 1000;
        }
    }

    @Override
    public String toString() {
        return "PlayerEntity{" +
               "id=" + id +
               ", username='" + username + "'" +
               ", elo=" + elo +
               ", wins=" + wins +
               ", losses=" + losses +
               ", lastActive=" + lastActive +
               '}';
    }
}
