package com.arcadehub.server.network;

import io.netty.util.AttributeKey;
import java.util.UUID;

public class ServerInitializer {
    public static final AttributeKey<String> USERNAME_KEY = AttributeKey.valueOf("username");
    public static final AttributeKey<UUID> LOBBY_ID_KEY = AttributeKey.valueOf("lobbyId");
}