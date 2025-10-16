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
        // Roll back to authoritative state
        predictedState = new GameState(newAuthoritative.getLobbyId(), newAuthoritative.getTick(),
                                       new ArrayList<>(newAuthoritative.getSnakes()),
                                       new ArrayList<>(newAuthoritative.getPaddles()),
                                       new ArrayList<>(newAuthoritative.getBalls()),
                                       new HashMap<>(newAuthoritative.getScores()),
                                       newAuthoritative.getSeed());
        lastProcessedTick = newAuthoritative.getTick();
        // Replay pending inputs
        for (InputEnvelope env : pendingInputs) {
            if (env.getClientTick() > lastProcessedTick) {
                applyInputToPredicted(env);
            }
        }
        // Remove old inputs
        pendingInputs.removeIf(env -> env.getClientTick() <= lastProcessedTick);
    }

    private void applyInputToPredicted(InputEnvelope env) {
        // Apply to predictedState
        // Similar to server logic
        // TODO: Implement based on game type
    }

    public GameState getPredictedState() {
        return predictedState;
    }
}