# ArcadeHub

ArcadeHub is a cross-platform Java desktop arcade platform featuring real-time multiplayer Snake & Pong, leaderboards, lobby-based chat, anti-cheat mechanisms, a neon retro UI, animated particle effects, and native installers for Windows, macOS, and Linux.

## Features

*   **Real-time Multiplayer**: Engage in competitive Snake and Pong games with friends.
*   **Lobby System**: Create and join lobbies with integrated chat functionality.
*   **Leaderboards**: Compete for the top spot with an ELO-based ranking system.
*   **Anti-Cheat**: Robust server-side validation to ensure fair gameplay.
*   **Neon Retro UI**: A visually appealing user interface with animated particle effects.
*   **Cross-Platform**: Native installers for Windows, macOS, and Linux, bundling Java 17 LTS.

## Compatibility

*   **Operating Systems**:
    *   Windows 10/11 x64
    *   macOS 12+ Intel/Apple Silicon
    *   Linux Ubuntu 20.04+/Debian
*   **Java Runtime**: Java 17 LTS (bundled with installers)

## Frameworks and Technologies

*   **UI**: JavaFX 20 with Scene Builder integration
*   **Game Rendering**: LibGDX 2D
*   **Networking**: Netty 4.x TCP with JSON serialization
*   **Database**: Hibernate ORM 6 with PostgreSQL (production) and SQLite (development)
*   **Audio**: OpenAL or JavaFX MediaPlayer
*   **Logging**: SLF4J + Logback
*   **Testing**: JUnit5 + Mockito
*   **Build System**: Gradle Kotlin DSL

## Project Structure

The project is organized into three main modules:

*   `client/`: Contains the client-side application logic, UI, game rendering, and network communication.
*   `server/`: Houses the server-side logic, game loops, lobby management, database interactions, and anti-cheat validation.
*   `common/`: Shared classes and utilities used by both the client and server (e.g., `Packet` definitions, `Position`, `Snake`, `Ball`, `Paddle`).

## Getting Started

### Prerequisites

*   Java Development Kit (JDK) 17 LTS
*   Gradle (optional, as `./gradlew` wrapper is included)

### Building the Project

To build the entire project, navigate to the root directory (`ArcadeHub/`) and run:

```bash
./gradlew build
```

This will compile all modules and generate JAR files in their respective `build/libs` directories.

### Running the Server

To run the server, navigate to the `server/` directory and execute the generated JAR:

```bash
java -jar build/libs/server-1.0.0.jar
```

The server will start on `localhost:5050` for game traffic and `localhost:5051` for chat (though chat port is currently not explicitly used in the provided code).

### Running the Client

To run the client, navigate to the `client/` directory and execute the generated JAR:

```bash
java -jar build/libs/client-1.0.0.jar
```

The client application will launch, and it will attempt to connect to the server running on `localhost:5050`.

## How to Play

### Main Menu

Upon launching the client, you will be presented with the main menu:

*   **Start**: (To be implemented: Leads to lobby creation/joining)
*   **Leaderboard**: Displays the top players by ELO.
*   **Settings**: (To be implemented: Opens a settings menu)
*   **Exit**: Closes the application.

### Snake Game

*   **Objective**: Control your snake to eat food and grow. Avoid colliding with walls or your own body.
*   **Controls**: Use W, A, S, D keys to change the snake's direction.

### Pong Game

*   **Objective**: Control your paddle to hit the ball past your opponent. First to 10 points wins.
*   **Controls**: Use W, S keys to move your paddle up and down.

## Contributing

We welcome contributions! Please see our [CONTRIBUTING.md](CONTRIBUTING.md) for guidelines on how to contribute, report bugs, and suggest features.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
