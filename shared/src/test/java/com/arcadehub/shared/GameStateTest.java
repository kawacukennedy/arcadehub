package com.arcadehub.shared;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

class GameStateTest {

    @Test
    void testGameStateCreation() {
        List<Snake> snakes = List.of();
        List<Paddle> paddles = List.of();
        List<Ball> balls = List.of();
        Map<String, Integer> scores = Map.of("alice", 10);

        GameState state = new GameState("lobby1", 1, snakes, paddles, balls, scores, 12345L);

        assertEquals("lobby1", state.getLobbyId());
        assertEquals(1, state.getTick());
        assertEquals(snakes, state.getSnakes());
        assertEquals(scores, state.getScores());
        assertEquals(12345L, state.getSeed());
    }
}