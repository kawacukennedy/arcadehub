package com.arcadehub.shared;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Network packet envelope as per spec.
 * Contains type, version, optional sessionId, and payload.
 */
public class NetworkPacket {
    @JsonProperty("type")
    private String type;

    @JsonProperty("version")
    private int version;

    @JsonProperty("sessionId")
    private String sessionId; // optional

    @JsonProperty("payload")
    private Object payload; // The actual packet data

    public NetworkPacket() {}

    public NetworkPacket(String type, int version, Object payload) {
        this.type = type;
        this.version = version;
        this.payload = payload;
    }

    public NetworkPacket(String type, int version, String sessionId, Object payload) {
        this.type = type;
        this.version = version;
        this.sessionId = sessionId;
        this.payload = payload;
    }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public int getVersion() { return version; }
    public void setVersion(int version) { this.version = version; }

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public Object getPayload() { return payload; }
    public void setPayload(Object payload) { this.payload = payload; }
}