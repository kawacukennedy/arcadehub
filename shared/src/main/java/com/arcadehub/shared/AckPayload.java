package com.arcadehub.shared;

public class AckPayload {
    private String refType;
    private String refId;
    private String status;

    public AckPayload() {}

    public AckPayload(String refType, String refId, String status) {
        this.refType = refType;
        this.refId = refId;
        this.status = status;
    }

    public String getRefType() { return refType; }
    public void setRefType(String refType) { this.refType = refType; }

    public String getRefId() { return refId; }
    public void setRefId(String refId) { this.refId = refId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}