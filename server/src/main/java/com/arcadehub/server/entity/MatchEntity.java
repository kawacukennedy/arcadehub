package com.arcadehub.server.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "matches")
public class MatchEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "game_type", nullable = false, length = 16)
    private String gameType;

    @Column(name = "winner_username", nullable = false, length = 32)
    private String winnerUsername;

    @Column(name = "created_at")
    private Instant createdAt;

    // Constructors
    public MatchEntity() {
    }

    public MatchEntity(String gameType, String winnerUsername) {
        this.gameType = gameType;
        this.winnerUsername = winnerUsername;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public String getWinnerUsername() {
        return winnerUsername;
    }

    public void setWinnerUsername(String winnerUsername) {
        this.winnerUsername = winnerUsername;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    @PrePersist
    protected void onCreate() {
        if (id == null) {
            id = UUID.randomUUID();
        }
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }

    @Override
    public String toString() {
        return "MatchEntity{" +
               "id=" + id +
               ", gameType='" + gameType + "'" +
               ", winnerUsername='" + winnerUsername + "'" +
               ", createdAt=" + createdAt +
               '}';
    }
}
