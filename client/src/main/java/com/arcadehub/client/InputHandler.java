package com.arcadehub.client;

import com.arcadehub.client.network.ClientNetworkManager;
import com.arcadehub.shared.InputPacket;
import com.arcadehub.shared.InputPayload;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InputHandler {
    private static final Logger logger = LoggerFactory.getLogger(InputHandler.class);
    private final ClientNetworkManager networkManager;

    public InputHandler(Scene scene, ClientNetworkManager networkManager) {
        this.networkManager = networkManager;
        scene.setOnKeyPressed(this::handleKeyPressed);
        scene.setOnKeyReleased(this::handleKeyReleased);
        // TODO: Add mouse input handling if necessary for specific games
    }

    private void handleKeyPressed(KeyEvent event) {
        String action = null;
        switch (event.getCode()) {
            case W:
            case UP:
                action = "MOVE_UP";
                break;
            case S:
            case DOWN:
                action = "MOVE_DOWN";
                break;
            case A:
            case LEFT:
                action = "MOVE_LEFT";
                break;
            case D:
            case RIGHT:
                action = "MOVE_RIGHT";
                break;
            case SPACE:
                action = "SHOOT";
                break;
            // Add more key mappings as needed
        }

        if (action != null) {
            String username = "alice"; // placeholder
            int tick = 0; // placeholder
            String sessionToken = ClientNetworkManager.getSessionToken();
            if (sessionToken == null) {
                logger.warn("No session token, cannot send input");
                return;
            }
            String signature = computeSignature(sessionToken, tick, action);
            InputPayload payload = new InputPayload(username, action, tick, signature);
            networkManager.sendPacket(new InputPacket(payload));
            logger.debug("Key pressed: {} -> Sent InputPacket: {}", event.getCode(), action);
        }
    }

    private void handleKeyReleased(KeyEvent event) {
        // For continuous actions, key released might also trigger an InputPacket (e.g., STOP_MOVE_UP)
        // For now, we only send on key pressed for simplicity.
        logger.debug("Key released: {}", event.getCode());
    }

    private String computeSignature(String sessionToken, int tick, String action) {
        try {
            String data = sessionToken + ":" + tick + ":" + action;
            // For client, we don't have the key, assume it's the token itself for demo
            // In real, key is derived or stored
            byte[] key = Base64.decodeBase64(sessionToken);
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec keySpec = new SecretKeySpec(key, "HmacSHA256");
            mac.init(keySpec);
            byte[] sig = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.encodeBase64URLSafeString(sig);
        } catch (Exception e) {
            logger.error("Error computing signature", e);
            return "";
        }
    }
}