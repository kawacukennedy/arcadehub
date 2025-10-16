package com.arcadehub.client.input;

import com.arcadehub.client.network.NetworkClient;
import com.arcadehub.client.game.GameRenderer;
import com.arcadehub.shared.InputAction;
import com.arcadehub.shared.Packet;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

/**
 * Maps keys to InputAction, throttles inputs, attaches tick numbers.
 */
public class InputHandler {
    private Scene scene;
    private NetworkClient networkClient;
    private GameRenderer gameRenderer;

    public InputHandler(Scene scene, NetworkClient networkClient, GameRenderer gameRenderer) {
        this.scene = scene;
        this.networkClient = networkClient;
        this.gameRenderer = gameRenderer;
        setupKeyHandlers();
    }

    private void setupKeyHandlers() {
        scene.setOnKeyPressed(event -> {
            InputAction action = null;
            switch (event.getCode()) {
                case UP: action = InputAction.UP; break;
                case DOWN: action = InputAction.DOWN; break;
                case LEFT: action = InputAction.LEFT; break;
                case RIGHT: action = InputAction.RIGHT; break;
            }
            if (action != null) {
                // Create packet and send
                Packet packet = new Packet();
                packet.type = com.arcadehub.shared.PacketType.INPUT;
                packet.payload = Map.of("action", action.name(), "tick", 0); // TODO: proper tick
                networkClient.sendPacket(packet);
            }
        });
    }
}
