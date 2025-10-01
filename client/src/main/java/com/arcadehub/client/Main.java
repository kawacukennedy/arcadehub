package com.arcadehub.client;

import com.arcadehub.client.audio.AudioManager;
import com.arcadehub.client.game.GameRenderer;
import com.arcadehub.client.network.ClientHandler;
import com.arcadehub.client.network.ClientInitializer;
import com.arcadehub.client.network.NetworkClient;
import com.arcadehub.client.ui.UIManager;
import javafx.application.Application;
import javafx.stage.Stage;

// Placeholder imports for LibGDX dependencies
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Initialize placeholder dependencies for GameRenderer
        OrthographicCamera camera = new OrthographicCamera();
        SpriteBatch batch = new SpriteBatch();

        // Instantiate GameRenderer first as it's a dependency for ClientHandler
        GameRenderer gameRenderer = new GameRenderer(camera, batch);

        // Instantiate ClientHandler (UIManager will be set later to resolve circular dependency)
        ClientHandler clientHandler = new ClientHandler(gameRenderer, null);

        // Instantiate ClientInitializer with ClientHandler
        ClientInitializer clientInitializer = new ClientInitializer(clientHandler);

        // Instantiate NetworkClient with ClientInitializer
        NetworkClient networkClient = new NetworkClient(clientInitializer);

        // Instantiate AudioManager
        AudioManager audioManager = new AudioManager();

        // Instantiate UIManager
        UIManager uiManager = new UIManager(primaryStage, networkClient, audioManager, gameRenderer);

        // Resolve circular dependency by setting UIManager in ClientHandler
        clientHandler.setUIManager(uiManager);

        // Show the main menu
        uiManager.showMainMenu();
    }

    public static void main(String[] args) {
        launch(args);
    }
}