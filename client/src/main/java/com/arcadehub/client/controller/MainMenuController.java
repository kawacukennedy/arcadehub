package com.arcadehub.client.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainMenuController {
    private static final Logger logger = LoggerFactory.getLogger(MainMenuController.class);

    @FXML
    private Button startGameButton;
    @FXML
    private Button leaderboardsButton;
    @FXML
    private Button settingsButton;
    @FXML
    private Button exitButton;

    @FXML
    private void handleStartGame(ActionEvent event) {
        logger.info("Start Game button clicked.");
        // TODO: Navigate to lobby screen
    }

    @FXML
    private void handleLeaderboards(ActionEvent event) {
        logger.info("Leaderboards button clicked.");
        // TODO: Navigate to leaderboards screen
    }

    @FXML
    private void handleSettings(ActionEvent event) {
        logger.info("Settings button clicked.");
        // TODO: Navigate to settings screen
    }

    @FXML
    private void handleExit(ActionEvent event) {
        logger.info("Exit button clicked. Shutting down application.");
        System.exit(0);
    }
}
