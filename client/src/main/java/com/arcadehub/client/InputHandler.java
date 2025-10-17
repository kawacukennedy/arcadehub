package com.arcadehub.client;

import com.arcadehub.client.network.ClientNetworkManager;
import com.arcadehub.client.game.GameRenderer;
import com.arcadehub.shared.Packet;
import com.arcadehub.shared.PacketType;
import java.util.Map;


import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InputHandler {
    private static final Logger logger = LoggerFactory.getLogger(InputHandler.class);
    private final ClientNetworkManager networkManager;
    private final GameRenderer gameRenderer;

    public InputHandler(Scene scene, ClientNetworkManager networkManager, GameRenderer gameRenderer) {
        this.networkManager = networkManager;
        this.gameRenderer = gameRenderer;
        scene.setOnKeyPressed(this::handleKeyPressed);
        scene.setOnKeyReleased(this::handleKeyReleased);
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
            gameRenderer.applyLocalInput(username, action, tick);
            Packet packet = new Packet();
            packet.type = PacketType.INPUT;
            packet.payload = Map.of("player", username, "action", action, "tick", tick);
            networkManager.sendPacket(packet);
            logger.debug("Key pressed: {} -> Sent Packet: {}", event.getCode(), action);
        }
    }

    private void handleKeyReleased(KeyEvent event) {
        // For continuous actions, key released might also trigger an InputPacket (e.g., STOP_MOVE_UP)
        // For now, we only send on key pressed for simplicity.
        logger.debug("Key released: {}", event.getCode());
    }


}