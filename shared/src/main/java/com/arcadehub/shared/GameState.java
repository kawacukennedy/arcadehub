package com.arcadehub.shared;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

public class GameState {
    public long tickId;
    public GameType gameType;
    public Map<String, Object> payload;

    private static final ObjectMapper mapper = new ObjectMapper();

    public GameState deepCopy() {
        // Implement deep copy
        try {
            return mapper.readValue(mapper.writeValueAsString(this), GameState.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String toCompactJson() {
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
    }

    public String toCompactJson() {
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
=======
import java.util.List;
import java.util.Map;

public class GameState {
    private final String lobbyId;
    private final int tick;
    private final List<Snake> snakes;
    private final List<Paddle> paddles;
    private final List<Ball> balls;
    private final Map<String, Integer> scores;
    private final long seed;

    public GameState(String lobbyId, int tick, List<Snake> snakes, List<Paddle> paddles, List<Ball> balls, Map<String, Integer> scores, long seed) {
        this.lobbyId = lobbyId;
        this.tick = tick;
        this.snakes = snakes;
        this.paddles = paddles;
        this.balls = balls;
        this.scores = scores;
        this.seed = seed;
    }

    public String getLobbyId() { return lobbyId; }
    public int getTick() { return tick; }
    public List<Snake> getSnakes() { return snakes; }
    public List<Paddle> getPaddles() { return paddles; }
    public List<Ball> getBalls() { return balls; }
    public Map<String, Integer> getScores() { return scores; }
    public long getSeed() { return seed; }
>>>>>>> 9e3054a61e4187c918966cb5fbc09c3ce49652c3
}
