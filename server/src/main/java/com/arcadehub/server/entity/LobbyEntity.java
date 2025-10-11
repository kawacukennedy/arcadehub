package com.arcadehub.server.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "lobbies")
public class LobbyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;

    private String host;

    @Column(nullable = false, length = 16)
    @Enumerated(EnumType.STRING)
    private GameType type;

    @Column(nullable = false)
    private long seed;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "last_active")
    private Instant lastActive;

    public enum GameType {
        SNAKE, PONG
    }

    public LobbyEntity() {}

    public LobbyEntity(String name, String host, GameType type, long seed) {
        this.name = name;
        this.host = host;
        this.type = type;
        this.seed = seed;
    }

    // Getters and setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getHost() { return host; }
    public void setHost(String host) { this.host = host; }

    public GameType getType() { return type; }
    public void setType(GameType type) { this.type = type; }

    public long getSeed() { return seed; }
    public void setSeed(long seed) { this.seed = seed; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getLastActive() { return lastActive; }
    public void setLastActive(Instant lastActive) { this.lastActive = lastActive; }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
        if (lastActive == null) {
            lastActive = Instant.now();
        }
    }
}