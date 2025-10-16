package com.arcadehub.server.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "cheat_logs")
public class CheatLogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String username;

    @Column(nullable = false)
    private String violation;

    @Column(nullable = false)
    private int points;

    private Long tick;

    @Column(name = "created_at")
    private Instant createdAt;

    public CheatLogEntity() {}

    public CheatLogEntity(String username, String violation, int points, Long tick) {
        this.username = username;
        this.violation = violation;
        this.points = points;
        this.tick = tick;
    }

    // Getters and setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getViolation() { return violation; }
    public void setViolation(String violation) { this.violation = violation; }

    public int getPoints() { return points; }
    public void setPoints(int points) { this.points = points; }

    public Long getTick() { return tick; }
    public void setTick(Long tick) { this.tick = tick; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }
}