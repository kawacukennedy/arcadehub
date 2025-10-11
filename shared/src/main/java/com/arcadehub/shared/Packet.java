package com.arcadehub.shared;

/**
 * Base interface for all network packets.
 * All packets must have a type and version for the envelope.
 */
public interface Packet {
    String getType();
    int getVersion();
}