package com.arcadehub.server.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "chat_logs")
public class ChatLogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lobby_id", nullable = false)
    private UUID lobbyId;

    @Column(nullable = false, length = 32)
    private String username;

    @Column(nullable = false, length = 200)
    private String message;

    @Column(nullable = false)
    private Instant timestamp;

    // Constructors
    public ChatLogEntity() {
    }

    public ChatLogEntity(UUID lobbyId, String username, String message) {
        this.lobbyId = lobbyId;
        this.username = username;
        this.message = message;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getLobbyId() {
        return lobbyId;
    }

    public void setLobbyId(UUID lobbyId) {
        this.lobbyId = lobbyId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    @PrePersist
    protected void onCreate() {
        if (timestamp == null) {
            timestamp = Instant.now();
        }
    }

    @Override
    public String toString() {
        return "ChatLogEntity{" +
               "id=" + id +
               ", lobbyId=" + lobbyId +
               ", username='" + username + "'" +
               ", message='" + message + "'" +
               ", timestamp=" + timestamp +
               '}';
    }
}
