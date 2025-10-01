package com.arcadehub.common;

import java.io.Serializable;
import java.util.List;

public class LeaderboardResponsePacket extends Packet implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Player> topPlayers;

    public LeaderboardResponsePacket() {
        // Default constructor for serialization
    }

    public LeaderboardResponsePacket(List<Player> topPlayers) {
        this.topPlayers = topPlayers;
    }

    public List<Player> getTopPlayers() {
        return topPlayers;
    }

    public void setTopPlayers(List<Player> topPlayers) {
        this.topPlayers = topPlayers;
    }
}
