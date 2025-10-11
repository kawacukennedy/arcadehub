package com.arcadehub.client.game;

import com.arcadehub.shared.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientGameState {
    private GameState authoritativeState;
    private GameState predictedState;
    private List<InputEnvelope> pendingInputs = new ArrayList<>();
    private int lastProcessedTick = 0;

    public void applyInput(String username, String action, int tick) {
        // Apply to predicted state immediately
        // For now, assume Snake direction change
        // TODO: Update predictedState

        // Add to pending
        pendingInputs.add(new InputEnvelope(System.nanoTime(), tick, username, action, "", ""));
    }

    public void reconcile(GameState newAuthoritative) {
        authoritativeState = newAuthoritative;
        // Roll back to authoritative tick, replay pending inputs
        // TODO: Implement rollback and replay
        predictedState = newAuthoritative; // Simple for now
        lastProcessedTick = newAuthoritative.getTick();
        // Remove processed inputs
        pendingInputs.removeIf(env -> env.getClientTick() <= lastProcessedTick);
    }

    public GameState getPredictedState() {
        return predictedState;
    }
}