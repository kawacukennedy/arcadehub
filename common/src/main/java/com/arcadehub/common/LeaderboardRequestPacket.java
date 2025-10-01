package com.arcadehub.common;

import java.io.Serializable;

public class LeaderboardRequestPacket extends Packet implements Serializable {
    private static final long serialVersionUID = 1L;

    private int limit;

    public LeaderboardRequestPacket() {
        // Default constructor for serialization
    }

    public LeaderboardRequestPacket(int limit) {
        this.limit = limit;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
