package com.arcadehub.common;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "players")
public class Player implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private UUID id;
    private String username;
    private int elo;

    public Player() {
        // Default constructor for Hibernate
    }

    public Player(String username) {
        this.id = UUID.randomUUID();
        this.username = username;
        this.elo = 1000; // Default ELO
    }

    public Player(UUID id, String username, int elo) {
        this.id = id;
        this.username = username;
        this.elo = elo;
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

    public int getElo() {
        return elo;
    }

    public void setElo(int elo) {
        this.elo = elo;
    }
}