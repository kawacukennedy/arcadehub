package com.arcadehub.shared;

import java.util.List;
import java.util.Map;

public class StateUpdatePayload {
    private String lobbyId;
    private int tick;
    private List<Snake> snakes;
    private List<Paddle> paddles;
    private List<Ball> balls;
    private Map<String, Integer> scores; // username -> score
    private long serverTimestamp;

    public StateUpdatePayload() {}

    public StateUpdatePayload(String lobbyId, int tick, List<Snake> snakes, List<Paddle> paddles, List<Ball> balls, Map<String, Integer> scores, long serverTimestamp) {
        this.lobbyId = lobbyId;
        this.tick = tick;
        this.snakes = snakes;
        this.paddles = paddles;
        this.balls = balls;
        this.scores = scores;
        this.serverTimestamp = serverTimestamp;
    }

    public String getLobbyId() { return lobbyId; }
    public void setLobbyId(String lobbyId) { this.lobbyId = lobbyId; }

    public int getTick() { return tick; }
    public void setTick(int tick) { this.tick = tick; }

    public List<Snake> getSnakes() { return snakes; }
    public void setSnakes(List<Snake> snakes) { this.snakes = snakes; }

    public List<Paddle> getPaddles() { return paddles; }
    public void setPaddles(List<Paddle> paddles) { this.paddles = paddles; }

    public List<Ball> getBalls() { return balls; }
    public void setBalls(List<Ball> balls) { this.balls = balls; }

    public Map<String, Integer> getScores() { return scores; }
    public void setScores(Map<String, Integer> scores) { this.scores = scores; }

    public long getServerTimestamp() { return serverTimestamp; }
    public void setServerTimestamp(long serverTimestamp) { this.serverTimestamp = serverTimestamp; }
}