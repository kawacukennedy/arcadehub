package com.arcadehub.common;

import com.arcadehub.common.GameType;
import com.arcadehub.common.Player;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "lobbies")
public class Lobby {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(length = 64)
    private String name;

    @Column(length = 32)
    private String host;

    @Enumerated(EnumType.STRING)
    private GameType type;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Transient // Not persisted to DB
    private List<Player> players = new ArrayList<>(); // Added players field

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public Lobby() {
        // Default constructor for Hibernate
    }

    public Lobby(String name, String host, GameType type) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.host = host;
        this.type = type;
        this.createdAt = new Timestamp(System.currentTimeMillis());
    }

    // Getters and setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public GameType getType() {
        return type;
    }

    public void setType(GameType type) {
        this.type = type;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}