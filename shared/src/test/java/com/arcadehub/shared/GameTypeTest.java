package com.arcadehub.shared;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameTypeTest {

    @Test
    void testGameTypeEnumValues() {
        assertEquals(GameType.SNAKE, GameType.valueOf("SNAKE"));
        assertEquals(GameType.PONG, GameType.valueOf("PONG"));

        assertEquals(2, GameType.values().length);
    }
}
