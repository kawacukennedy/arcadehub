package com.arcadehub.client;

import com.arcadehub.client.audio.AudioManager;
import com.arcadehub.client.game.GameRenderer;
import com.arcadehub.client.network.ClientHandler;
import com.arcadehub.client.network.ClientInitializer;
import com.arcadehub.client.network.NetworkClient;
import com.arcadehub.client.ui.UIManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    private NetworkClient networkClient;
    private AudioManager audioManager;
    private GameRenderer gameRenderer;
    private UIManager uiManager;
    private InputHandler inputHandler;

    // LibGDX components
    private OrthographicCamera camera;
    private SpriteBatch batch;

    @Override
    public void start(Stage primaryStage) {
        // 1. Initialize core components
        audioManager = new AudioManager();

        // Initialize LibGDX components
        camera = new OrthographicCamera();
        batch = new SpriteBatch();
        gameRenderer = new GameRenderer(camera, batch);

        // 2. Initialize Network components
        ClientHandler clientHandler = new ClientHandler(gameRenderer, uiManager); // uiManager is null here, will set later
        ClientInitializer clientInitializer = new ClientInitializer(clientHandler);
        networkClient = new NetworkClient(clientInitializer);

        // 3. Initialize UI Manager (requires networkClient, audioManager, gameRenderer)
        uiManager = new UIManager(primaryStage, networkClient, audioManager, gameRenderer);
        clientHandler.setUIManager(uiManager); // Set uiManager in clientHandler after it's initialized

        // 4. Initialize Input Handler (requires networkClient and a Scene)
        inputHandler = new InputHandler(networkClient);

        // 5. Show Main Menu
        uiManager.showMainMenu();
        inputHandler.captureInput(uiManager.getMainMenuScene());
        primaryStage.setTitle("ArcadeHub");
        primaryStage.show();

        // 6. Connect to server in a separate thread to avoid blocking UI thread
        new Thread(() -> {
            try {
                networkClient.connect("localhost", 5050); // Connect to game server port
            } catch (Exception e) {
                System.err.println("Failed to connect to server: " + e.getMessage());
            }
        }).start();
    }

    @Override
    public void stop() throws Exception {
        if (networkClient != null) {
            networkClient.disconnect();
        }
        // Dispose of LibGDX resources
        if (batch != null) {
            batch.dispose();
        }
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}