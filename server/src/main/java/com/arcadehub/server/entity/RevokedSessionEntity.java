package com.arcadehub.server.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "revoked_sessions")
public class RevokedSessionEntity {
    @Id
    @Column(name = "session_hash", columnDefinition = "BYTEA")
    private byte[] sessionHash;

    @Column(name = "revoked_at")
    private Instant revokedAt;

    public RevokedSessionEntity() {}

    public RevokedSessionEntity(byte[] sessionHash) {
        this.sessionHash = sessionHash;
    }

    // Getters and setters
    public byte[] getSessionHash() { return sessionHash; }
    public void setSessionHash(byte[] sessionHash) { this.sessionHash = sessionHash; }

    public Instant getRevokedAt() { return revokedAt; }
    public void setRevokedAt(Instant revokedAt) { this.revokedAt = revokedAt; }

    @PrePersist
    protected void onCreate() {
        if (revokedAt == null) {
            revokedAt = Instant.now();
        }
    }
}