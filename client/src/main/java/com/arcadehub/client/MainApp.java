package com.arcadehub.client;

import com.arcadehub.client.network.ClientNetworkManager;
import com.arcadehub.client.game.GameRenderer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class MainApp extends Application {
    private static final Logger logger = LoggerFactory.getLogger(MainApp.class);

    @Override
    public void start(Stage primaryStage) throws IOException {
        logger.info("Starting ArcadeHub Client Application...");

        ClientNetworkManager networkManager = new ClientNetworkManager();
        // Start network connection in a separate thread to not block UI thread
        new Thread(() -> {
            try {
                networkManager.connect();
            } catch (Exception e) {
                logger.error("Failed to connect to server: {}", e.getMessage(), e);
            }
        }).start();

        // Load the main menu FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/assets/fxml/MainMenu.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        primaryStage.setTitle("ArcadeHub");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Initialize GameRenderer (placeholder)
        GameRenderer gameRenderer = new GameRenderer(null, null); // TODO: Proper init
        ClientHandler.setGameRenderer(gameRenderer);

        // Initialize InputHandler
        InputHandler inputHandler = new InputHandler(scene, networkManager, gameRenderer);

        primaryStage.setOnCloseRequest(event -> {
            logger.info("Shutting down network manager.");
            networkManager.disconnect();
        });

        logger.info("ArcadeHub Client Application started successfully.");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
