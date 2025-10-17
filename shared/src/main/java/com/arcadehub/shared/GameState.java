package com.arcadehub.shared;

import java.util.Map;

public class GameState {
    private final long tickId;
    private final GameType gameType;
    private final Map<String, Object> payload;

    public GameState(long tickId, GameType gameType, Map<String, Object> payload) {
        this.tickId = tickId;
        this.gameType = gameType;
        this.payload = payload;
    }

    public long getTickId() { return tickId; }
    public GameType getGameType() { return gameType; }
    public Map<String, Object> getPayload() { return payload; }
}
