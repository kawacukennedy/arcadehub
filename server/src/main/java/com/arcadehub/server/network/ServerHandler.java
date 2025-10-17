package com.arcadehub.server.network;

import com.arcadehub.shared.Packet;
import com.arcadehub.shared.PacketType;
import com.arcadehub.shared.Player;
import com.arcadehub.shared.GameType;
import com.arcadehub.shared.GameState;
import com.arcadehub.shared.Lobby;
import com.arcadehub.server.entity.PlayerEntity;
import com.arcadehub.server.leaderboard.LeaderboardManager;
import com.arcadehub.server.game.AntiCheatValidator;
import com.arcadehub.server.game.GameLoopManager;
import com.arcadehub.server.lobby.LobbyManager;
import com.arcadehub.server.ServerInitializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ServerHandler extends SimpleChannelInboundHandler<Packet> {

    private static final java.util.Map<String, Channel> channels = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final LobbyManager lobbyManager;
    private final GameLoopManager gameLoopManager;
    private final LeaderboardManager leaderboardManager;
    private final AntiCheatValidator antiCheatValidator;

    public ServerHandler(LobbyManager lobbyManager, GameLoopManager gameLoopManager, LeaderboardManager leaderboardManager, AntiCheatValidator antiCheatValidator) {
        this.lobbyManager = lobbyManager;
        this.gameLoopManager = gameLoopManager;
        this.leaderboardManager = leaderboardManager;
        this.antiCheatValidator = antiCheatValidator;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client connected: " + ctx.channel().remoteAddress());
        channels.put(ctx.channel().id().asShortText(), ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client disconnected: " + ctx.channel().remoteAddress());
        channels.remove(ctx.channel().id().asShortText());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet packet) throws Exception {
        String username = ctx.channel().attr(ServerInitializer.USERNAME_KEY).get();
        UUID lobbyId = ctx.channel().attr(ServerInitializer.LOBBY_ID_KEY).get();

        switch (packet.type) {
            case JOIN_LOBBY:
                String joinUsername = (String) packet.payload.get("username");
                // For simplicity, let's assume a new lobby is always created for now
                // In a real scenario, you'd check for existing lobbies or allow client to specify
                Lobby lobby = lobbyManager.createLobby("Lobby for " + joinUsername, GameType.SNAKE, 4, joinUsername); // Default to SNAKE
                lobbyId = lobby.getId();
                Player player = new Player(UUID.randomUUID(), joinUsername, 1000, 0, 0, Instant.now());
                lobbyManager.joinLobby(lobbyId, player, ctx);
                ctx.channel().attr(ServerInitializer.USERNAME_KEY).set(joinUsername);
                ctx.channel().attr(ServerInitializer.LOBBY_ID_KEY).set(lobbyId);
                gameLoopManager.startLoop(lobbyId, GameType.SNAKE);

                // Send initial lobby update to the joining client
                Packet lobbyUpdatePacket = new Packet();
                lobbyUpdatePacket.type = PacketType.LOBBY_UPDATE;
                lobbyUpdatePacket.payload = Map.of("lobby", lobbyManager.getLobbyById(lobbyId));
                sendPacketToClient(ctx.channel(), lobbyUpdatePacket);
                break;
            case INPUT:
                if (username != null && lobbyId != null) {
                    GameState gameState = gameLoopManager.getGameState(lobbyId);
                    if (gameState != null) {
                        long tick = (Long) packet.payload.get("tick");
                        // Validate input with anti-cheat
                        if (gameState.getGameType() == GameType.SNAKE) {
                            if (antiCheatValidator.validateSnakeMove(username, null, tick)) {
                                // Apply snake movement
                                // This logic would typically be in GameLoopManager.tick()
                            }
                        } else if (gameState.getGameType() == GameType.PONG) {
                            if (antiCheatValidator.validatePaddleMove(username, null, tick)) {
                                // Apply paddle movement
                                // This logic would typically be in GameLoopManager.tick()
                            }
                        }
                    }
                }
                break;
            case CHAT:
                if (username != null && lobbyId != null) {
                    // Broadcast chat message to all clients in the lobby
                    broadcastPacketToLobby(lobbyId, packet);
                }
                break;
            case HEARTBEAT:
                // Acknowledge heartbeat (no action needed for now, connection is kept alive)
                break;
            case LEADERBOARD_REQUEST:
                int limit = (Integer) packet.payload.get("limit");
                List<PlayerEntity> topPlayerEntities = leaderboardManager.getTopPlayers(limit);
                List<Player> topPlayers = topPlayerEntities.stream()
                        .map(pe -> new Player(pe.getId(), pe.getUsername(), pe.getElo(), pe.getWins(), pe.getLosses(), pe.getLastLogin()))
                        .collect(Collectors.toList());
                Packet leaderboardPacket = new Packet();
                leaderboardPacket.type = PacketType.LEADERBOARD_RESPONSE;
                leaderboardPacket.payload = Map.of("players", topPlayers);
                sendPacketToClient(ctx.channel(), leaderboardPacket);
                break;
            default:
                System.out.println("Unknown packet type: " + basePacket.type);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    private void sendPacketToClient(Channel channel, Packet packet) {
        channel.writeAndFlush(packet);
    }

    private void broadcastPacketToLobby(UUID lobbyId, Packet packet) {
        lobbyManager.getLobbyById(lobbyId).getPlayers().forEach(player -> {
            Channel playerChannel = channels.values().stream()
                    .filter(channel -> player.getUsername().equals(channel.attr(ServerInitializer.USERNAME_KEY).get()))
                    .findFirst()
                    .orElse(null);
            if (playerChannel != null) {
                sendPacketToClient(playerChannel, packet);
            }
        });
    }

    public static java.util.List<Channel> getChannelsInLobby(UUID lobbyId) {
        return channels.values().stream()
                .filter(channel -> lobbyId.equals(channel.attr(ServerInitializer.LOBBY_ID_KEY).get()))
                .collect(java.util.stream.Collectors.toList());
    }
}
