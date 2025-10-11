package com.arcadehub.shared;

public class JoinAcceptPayload {
    private String sessionToken;
    private String assignedPlayerId;
    private int currentTick;

    public JoinAcceptPayload() {}

    public JoinAcceptPayload(String sessionToken, String assignedPlayerId, int currentTick) {
        this.sessionToken = sessionToken;
        this.assignedPlayerId = assignedPlayerId;
        this.currentTick = currentTick;
    }

    public String getSessionToken() { return sessionToken; }
    public void setSessionToken(String sessionToken) { this.sessionToken = sessionToken; }

    public String getAssignedPlayerId() { return assignedPlayerId; }
    public void setAssignedPlayerId(String assignedPlayerId) { this.assignedPlayerId = assignedPlayerId; }

    public int getCurrentTick() { return currentTick; }
    public void setCurrentTick(int currentTick) { this.currentTick = currentTick; }
}