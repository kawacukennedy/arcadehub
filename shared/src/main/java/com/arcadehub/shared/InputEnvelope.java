package com.arcadehub.shared;

public class InputEnvelope {
    private long arrivedAtNs;
    private int clientTick;
    private String username;
    private String action;
    private String signature;
    private String rawJson;

    public InputEnvelope() {}

    public InputEnvelope(long arrivedAtNs, int clientTick, String username, String action, String signature, String rawJson) {
        this.arrivedAtNs = arrivedAtNs;
        this.clientTick = clientTick;
        this.username = username;
        this.action = action;
        this.signature = signature;
        this.rawJson = rawJson;
    }

    // Getters and setters
    public long getArrivedAtNs() { return arrivedAtNs; }
    public void setArrivedAtNs(long arrivedAtNs) { this.arrivedAtNs = arrivedAtNs; }

    public int getClientTick() { return clientTick; }
    public void setClientTick(int clientTick) { this.clientTick = clientTick; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getSignature() { return signature; }
    public void setSignature(String signature) { this.signature = signature; }

    public String getRawJson() { return rawJson; }
    public void setRawJson(String rawJson) { this.rawJson = rawJson; }
}