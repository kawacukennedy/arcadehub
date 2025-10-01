package com.arcadehub.common;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public class CheatLog implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID id;
    private String username;
    private String violation;
    private LocalDateTime timestamp;

    public CheatLog() {
        // Default constructor for serialization
    }

    public CheatLog(String username, String violation) {
        this.id = UUID.randomUUID();
        this.username = username;
        this.violation = violation;
        this.timestamp = LocalDateTime.now();
    }

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

    public String getViolation() {
        return violation;
    }

    public void setViolation(String violation) {
        this.violation = violation;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}