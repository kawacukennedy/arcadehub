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
    private int elo = 1000;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "last_login")
    private Instant lastLogin;

    // Constructors
    public PlayerEntity() {
    }

    public PlayerEntity(String username) {
        this.username = username;
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

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getLastLogin() { return lastLogin; }
    public void setLastLogin(Instant lastLogin) { this.lastLogin = lastLogin; }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }

    @Override
    public String toString() {
        return "PlayerEntity{" +
               "id=" + id +
               ", username='" + username + "'" +
               ", elo=" + elo +
               ", createdAt=" + createdAt +
               ", lastLogin=" + lastLogin +
               '}';
    }
}
