package com.arcadehub.server.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "replays")
public class ReplayEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "lobby_id")
    private UUID lobbyId;

    @Column(nullable = false)
    private String path;

    @Column(name = "tick_start")
    private Long tickStart;

    @Column(name = "tick_end")
    private Long tickEnd;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "size_bytes")
    private Long sizeBytes;

    public ReplayEntity() {}

    public ReplayEntity(UUID lobbyId, String path, Long tickStart, Long tickEnd, Long sizeBytes) {
        this.lobbyId = lobbyId;
        this.path = path;
        this.tickStart = tickStart;
        this.tickEnd = tickEnd;
        this.sizeBytes = sizeBytes;
    }

    // Getters and setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getLobbyId() { return lobbyId; }
    public void setLobbyId(UUID lobbyId) { this.lobbyId = lobbyId; }

    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }

    public Long getTickStart() { return tickStart; }
    public void setTickStart(Long tickStart) { this.tickStart = tickStart; }

    public Long getTickEnd() { return tickEnd; }
    public void setTickEnd(Long tickEnd) { this.tickEnd = tickEnd; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Long getSizeBytes() { return sizeBytes; }
    public void setSizeBytes(Long sizeBytes) { this.sizeBytes = sizeBytes; }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }
}