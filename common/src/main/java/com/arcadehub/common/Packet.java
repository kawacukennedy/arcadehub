package com.arcadehub.common;

import java.io.Serializable;

/**
 * Marker interface for all network packets.
 * All packets sent between client and server should implement this interface.
 */
public interface Packet extends Serializable {
}