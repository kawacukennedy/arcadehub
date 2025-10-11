package com.arcadehub.client;

import com.arcadehub.client.network.ClientNetworkManager;
import com.arcadehub.shared.InputPacket;
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
        InputPacket.Action action = null;
        switch (event.getCode()) {
            case W:
            case UP:
                action = InputPacket.Action.MOVE_UP;
                break;
            case S:
            case DOWN:
                action = InputPacket.Action.MOVE_DOWN;
                break;
            case A:
            case LEFT:
                action = InputPacket.Action.MOVE_LEFT;
                break;
            case D:
            case RIGHT:
                action = InputPacket.Action.MOVE_RIGHT;
                break;
            case SPACE:
                action = InputPacket.Action.SHOOT;
                break;
            // Add more key mappings as needed
        }

        if (action != null) {
            networkManager.sendPacket(new InputPacket(action, System.currentTimeMillis()));
            logger.debug("Key pressed: {} -> Sent InputPacket: {}", event.getCode(), action);
        }
    }

    private void handleKeyReleased(KeyEvent event) {
        // For continuous actions, key released might also trigger an InputPacket (e.g., STOP_MOVE_UP)
        // For now, we only send on key pressed for simplicity.
        logger.debug("Key released: {}", event.getCode());
    }
}