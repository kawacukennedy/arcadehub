package com.arcadehub.server.network;

import com.arcadehub.common.ChatPacket;
import com.arcadehub.common.InputPacket;
import com.arcadehub.common.JoinLobbyPacket;
import com.arcadehub.common.LeaderboardRequestPacket;
import com.arcadehub.common.LeaderboardResponsePacket;
import com.arcadehub.common.LobbyUpdatePacket;
import com.arcadehub.common.Move;
import com.arcadehub.common.PaddleMove;
import com.arcadehub.common.Packet;
import com.arcadehub.common.Player;
import com.arcadehub.common.StateUpdatePacket;
import com.arcadehub.common.GameState;
import com.arcadehub.common.Lobby;
import com.arcadehub.common.GameType;
import com.arcadehub.server.db.LeaderboardManager;
import com.arcadehub.server.game.AntiCheatValidator;
import com.arcadehub.server.game.GameLoopManager;
import com.arcadehub.server.lobby.LobbyManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ServerHandler extends SimpleChannelInboundHandler<String> {

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
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Packet basePacket = objectMapper.readValue(msg, Packet.class);
        String username = ctx.channel().attr(ServerInitializer.USERNAME_KEY).get();
        UUID lobbyId = ctx.channel().attr(ServerInitializer.LOBBY_ID_KEY).get();

        switch (basePacket.getClass().getSimpleName()) {
            case "JoinLobbyPacket":
                JoinLobbyPacket joinLobbyPacket = (JoinLobbyPacket) basePacket;
                // For simplicity, let's assume a new lobby is always created for now
                // In a real scenario, you'd check for existing lobbies or allow client to specify
                lobbyId = lobbyManager.createLobby("Lobby for " + joinLobbyPacket.getUsername(), joinLobbyPacket.getUsername(), GameType.SNAKE); // Default to SNAKE
                lobbyManager.joinLobby(lobbyId, joinLobbyPacket.getUsername());
                ctx.channel().attr(ServerInitializer.USERNAME_KEY).set(joinLobbyPacket.getUsername());
                ctx.channel().attr(ServerInitializer.LOBBY_ID_KEY).set(lobbyId);
                gameLoopManager.startLoop(lobbyId, GameType.SNAKE);

                // Send initial lobby update to the joining client
                sendPacketToClient(ctx.channel(), new LobbyUpdatePacket(lobbyManager.getLobby(lobbyId)));
                break;
            case "InputPacket":
                InputPacket inputPacket = (InputPacket) basePacket;
                if (username != null && lobbyId != null) {
                    GameState gameState = gameLoopManager.getGameState(lobbyId);
                    if (gameState != null) {
                        // Validate input with anti-cheat
                        if (gameState.getGameType() == GameType.SNAKE) {
                            if (antiCheatValidator.validateSnakeMove(username, null, inputPacket.getTick())) {
                                // Apply snake movement
                                // This logic would typically be in GameLoopManager.tick()
                            }
                        } else if (gameState.getGameType() == GameType.PONG) {
                            if (antiCheatValidator.validatePaddleMove(username, null, inputPacket.getTick())) {
                                // Apply paddle movement
                                // This logic would typically be in GameLoopManager.tick()
                            }
                        }
                    }
                }
                break;
            case "ChatPacket":
                ChatPacket chatPacket = (ChatPacket) basePacket;
                if (username != null && lobbyId != null) {
                    // Broadcast chat message to all clients in the lobby
                    broadcastPacketToLobby(lobbyId, chatPacket);
                }
                break;
            case "HeartbeatPacket":
                // Acknowledge heartbeat (no action needed for now, connection is kept alive)
                break;
            case "LeaderboardRequestPacket":
                LeaderboardRequestPacket leaderboardRequestPacket = (LeaderboardRequestPacket) basePacket;
                List<Player> topPlayers = leaderboardManager.getTopPlayers(leaderboardRequestPacket.getLimit());
                LeaderboardResponsePacket leaderboardResponsePacket = new LeaderboardResponsePacket(topPlayers);
                sendPacketToClient(ctx.channel(), leaderboardResponsePacket);
                break;
            default:
                System.out.println("Unknown packet type: " + basePacket.getClass().getSimpleName());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    private void sendPacketToClient(Channel channel, Packet packet) {
        try {
            String json = objectMapper.writeValueAsString(packet);
            channel.writeAndFlush(json + "\n");
        } catch (Exception e) {
            System.err.println("Error sending packet to client: " + e.getMessage());
        }
    }

    private void broadcastPacketToLobby(UUID lobbyId, Packet packet) {
        lobbyManager.getLobby(lobbyId).getPlayers().forEach(player -> {
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
