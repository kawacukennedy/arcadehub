package com.arcadehub.shared;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

public class GameState {
    public long tickId;
    public GameType gameType;
    public Map<String, Object> payload;

    private static final ObjectMapper mapper = new ObjectMapper();

    public GameState deepCopy() {
        // Implement deep copy
        try {
            return mapper.readValue(mapper.writeValueAsString(this), GameState.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String toCompactJson() {
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
