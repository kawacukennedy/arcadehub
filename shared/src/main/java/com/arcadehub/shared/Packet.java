package com.arcadehub.shared;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;

/**
 * Envelope pattern for all packets.
 */
public class Packet {
    public PacketType type;
    public Map<String, Object> payload;

    // Jackson mapper for serialization
    private static final ObjectMapper mapper = new ObjectMapper();

    public String getType() {
        return type != null ? type.name() : null;
    }

    public int getVersion() {
        return 1; // Assume version 1
    }

    public Object getPayload() {
        return payload;
    }

    public byte[] toBytes() throws JsonProcessingException {
        return mapper.writeValueAsBytes(this);
    }

    public static Packet fromBytes(byte[] b) throws IOException {
        return mapper.readValue(b, Packet.class);
    }
}