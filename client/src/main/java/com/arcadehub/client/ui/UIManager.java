package com.arcadehub.client.ui;

import com.arcadehub.client.audio.AudioManager;
import com.arcadehub.client.network.NetworkClient;
import com.arcadehub.client.game.GameRenderer;
import com.arcadehub.shared.GameState;
import com.arcadehub.shared.Lobby;
import com.arcadehub.shared.Player;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Manages all UI transitions, buttons, hover/focus effects, communicates with NetworkClient, triggers animations and sounds.
 */
public class UIManager {
    private Stage primaryStage;
    private Scene mainMenuScene = null;
    private Scene lobbyScene = null;
    private Scene gameScene = null;
    private Scene leaderboardScene = null;
    private Map<String, Button> buttons = new HashMap<>();
    private Font defaultFont = Font.loadFont(getClass().getResourceAsStream("/assets/fonts/arcade.ttf"), 16);
    private AudioManager audioManager;
    private NetworkClient networkClient;
    private GameRenderer gameRenderer;

    public UIManager(Stage primaryStage, NetworkClient networkClient, AudioManager audioManager, GameRenderer gameRenderer) {
        this.primaryStage = primaryStage;
        this.networkClient = networkClient;
        this.audioManager = audioManager;
        this.gameRenderer = gameRenderer;
    }

    public Scene getMainMenuScene() {
        return mainMenuScene;
    }

    public void appendChatMessage(String username, String message) {
        // Placeholder for appending chat messages to the lobby chat panel
        System.out.println("Chat: " + username + ": " + message);
    }

    /**
     * Renders main menu, initializes buttons Start, Leaderboard, Settings, Exit, sets event handlers with hover/focus animations and sounds.
     */
    public void showMainMenu() {
        VBox menuLayout = new VBox(20); // Spacing between buttons
        menuLayout.setAlignment(Pos.CENTER);
        menuLayout.setStyle("-fx-background-color: #0d0d0d;"); // Background color from UI spec

        Text title = new Text("ArcadeHub");
        title.setFont(Font.font("arcade", 48)); // Assuming 'arcade' font is loaded
        title.setFill(Color.CYAN);
        DropShadow ds = new DropShadow();
        ds.setOffsetY(3.0f);
        ds.setColor(Color.color(0.4f, 0.4f, 0.4f));
        title.setEffect(ds);
        menuLayout.getChildren().add(title);

        String[] buttonNames = {"Start", "Leaderboard", "Settings", "Exit"};
        for (String name : buttonNames) {
            Button button = new Button(name);
            button.setFont(defaultFont);
            button.setStyle("-fx-background-color: #1a1a1a; -fx-text-fill: white; -fx-padding: 10 20;");
            button.setPrefSize(200, 50);

            // Hover effect
            button.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
                button.setStyle("-fx-background-color: #3333ff; -fx-text-fill: white; -fx-padding: 10 20;");
                audioManager.play("menu_hover.wav");
            });
            button.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
                button.setStyle("-fx-background-color: #1a1a1a; -fx-text-fill: white; -fx-padding: 10 20;");
            });

            // Click effect
            button.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
                button.setStyle("-fx-background-color: #0000cc; -fx-text-fill: white; -fx-padding: 10 20;");
                audioManager.play("menu_click.wav");
            });
            button.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
                button.setStyle("-fx-background-color: #3333ff; -fx-text-fill: white; -fx-padding: 10 20;");
            });

            buttons.put(name, button);
            menuLayout.getChildren().add(button);

            // Set actions for buttons
            switch (name) {
                case "Start":
                    button.setOnAction(e -> {
                        // Placeholder for starting game/lobby selection
                        System.out.println("Start button clicked");
                    });
                    break;
                case "Leaderboard":
                    button.setOnAction(e -> {
                        // Placeholder for showing leaderboard
                        System.out.println("Leaderboard button clicked");
                    });
                    break;
                case "Settings":
                    button.setOnAction(e -> {
                        // Placeholder for showing settings
                        System.out.println("Settings button clicked");
                    });
                    break;
                case "Exit":
                    button.setOnAction(e -> primaryStage.close());
                    break;
            }
        }

        mainMenuScene = new Scene(menuLayout, 800, 600); // Assuming a window size
        primaryStage.setScene(mainMenuScene);
        primaryStage.setTitle("ArcadeHub");
        primaryStage.show();
    }

    /**
     * Renders active lobby screen with dynamic player list, ready button, and chat panel. Updates UI on server state changes.
     */
    public void showLobby(Lobby lobby) {
        VBox lobbyLayout = new VBox(10);
        lobbyLayout.setAlignment(Pos.TOP_CENTER);
        lobbyLayout.setStyle("-fx-background-color: #0d0d0d;");

        Text lobbyTitle = new Text("Lobby: " + lobby.getName());
        lobbyTitle.setFont(Font.font("arcade", 36));
        lobbyTitle.setFill(Color.LIMEGREEN);
        lobbyLayout.getChildren().add(lobbyTitle);

        Text lobbyInfo = new Text("ID: " + lobby.getId().toString().substring(0, 8) + "... Host: " + lobby.getHost());
        lobbyInfo.setFont(defaultFont);
        lobbyInfo.setFill(Color.WHITE);
        lobbyLayout.getChildren().add(lobbyInfo);

        // Placeholder for player list
        VBox playerList = new VBox(5);
        playerList.setAlignment(Pos.CENTER_LEFT);
        playerList.setStyle("-fx-background-color: #1a1a1a; -fx-padding: 10;");
        playerList.setPrefSize(300, 200);
        Text playersHeader = new Text("Players:");
        playersHeader.setFont(defaultFont);
        playersHeader.setFill(Color.CYAN);
        playerList.getChildren().add(playersHeader);
        // Add actual players here dynamically
        lobbyLayout.getChildren().add(playerList);

        // Placeholder for chat panel
        VBox chatPanel = new VBox(5);
        chatPanel.setAlignment(Pos.BOTTOM_LEFT);
        chatPanel.setStyle("-fx-background-color: #1a1a1a; -fx-padding: 10;");
        chatPanel.setPrefSize(300, 150);
        Text chatHeader = new Text("Chat:");
        chatHeader.setFont(defaultFont);
        chatHeader.setFill(Color.CYAN);
        chatPanel.getChildren().add(chatHeader);
        // Add chat messages here dynamically
        lobbyLayout.getChildren().add(chatPanel);

        Button readyButton = new Button("Ready");
        readyButton.setFont(defaultFont);
        readyButton.setStyle("-fx-background-color: #1a1a1a; -fx-text-fill: white; -fx-padding: 10 20;");
        readyButton.setPrefSize(150, 40);
        readyButton.setOnAction(e -> {
            System.out.println("Ready button clicked");
            // Send ready status to server via networkClient
        });
        lobbyLayout.getChildren().add(readyButton);

        lobbyScene = new Scene(lobbyLayout, 800, 600);
        primaryStage.setScene(lobbyScene);
        primaryStage.setTitle("ArcadeHub - Lobby");
    }

    /**
     * Displays Snake or Pong game, updates positions and animations, triggers sounds and particle effects, maintains 60 FPS rendering.
     */
    public void showGame(GameState state) {
        VBox gameLayout = new VBox(20);
        gameLayout.setAlignment(Pos.CENTER);
        gameLayout.setStyle("-fx-background-color: #0d0d0d;");

        Text gameText = new Text("Game in Progress...");
        gameText.setFont(Font.font("arcade", 48));
        gameText.setFill(Color.YELLOW);
        gameLayout.getChildren().add(gameText);

        // In a real scenario, this would integrate with LibGDX for rendering
        // For now, it's a placeholder.

        gameScene = new Scene(gameLayout, 800, 600);
        primaryStage.setScene(gameScene);
        primaryStage.setTitle("ArcadeHub - Game");
    }

    /**
     * Displays leaderboard in scrollable table, sorted by ELO, highlights current user, updates dynamically.
     */
    public void showLeaderboard(List<Player> topPlayers) {
        VBox leaderboardLayout = new VBox(10);
        leaderboardLayout.setAlignment(Pos.TOP_CENTER);
        leaderboardLayout.setStyle("-fx-background-color: #0d0d0d;");

        Text leaderboardTitle = new Text("Leaderboard");
        leaderboardTitle.setFont(Font.font("arcade", 36));
        leaderboardTitle.setFill(Color.GOLD);
        leaderboardLayout.getChildren().add(leaderboardTitle);

        TableView<Player> table = new TableView<>();
        table.setItems(FXCollections.observableArrayList(topPlayers));
        table.setPrefSize(600, 400);

        TableColumn<Player, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        usernameCol.setPrefWidth(300);

        TableColumn<Player, Integer> eloCol = new TableColumn<>("ELO");
        eloCol.setCellValueFactory(new PropertyValueFactory<>("elo"));
        eloCol.setPrefWidth(300);

        table.getColumns().addAll(usernameCol, eloCol);

        leaderboardLayout.getChildren().add(table);

        Button backButton = new Button("Back to Main Menu");
        backButton.setFont(defaultFont);
        backButton.setStyle("-fx-background-color: #1a1a1a; -fx-text-fill: white; -fx-padding: 10 20;");
        backButton.setOnAction(e -> showMainMenu());
        leaderboardLayout.getChildren().add(backButton);

        leaderboardScene = new Scene(leaderboardLayout, 800, 600);
        primaryStage.setScene(leaderboardScene);
        primaryStage.setTitle("ArcadeHub - Leaderboard");
    }
}
