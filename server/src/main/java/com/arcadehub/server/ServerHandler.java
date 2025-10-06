package com.arcadehub.server;

import com.arcadehub.common.Packet;
import com.arcadehub.common.HeartbeatPacket;
import com.arcadehub.common.InputPacket;
import com.arcadehub.common.Player;
import com.arcadehub.common.ChatPacket;
import com.arcadehub.common.JoinLobbyPacket;
import com.arcadehub.common.Lobby;
import com.arcadehub.common.GameType;
import com.arcadehub.common.LeaderboardRequestPacket;
import com.arcadehub.common.LeaderboardResponsePacket;
import com.arcadehub.server.anticheat.AntiCheatValidator;
import com.arcadehub.server.game.GameLoopManager;
import com.arcadehub.server.leaderboard.LeaderboardManager;
import com.arcadehub.server.lobby.LobbyManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;
import java.time.Instant;
import java.util.stream.Collectors;

public class ServerHandler extends SimpleChannelInboundHandler<Packet> {
    private static final Logger logger = LoggerFactory.getLogger(ServerHandler.class);

    private final LobbyManager lobbyManager;
    private final GameLoopManager gameLoopManager;
    private final LeaderboardManager leaderboardManager;
    private final AntiCheatValidator antiCheatValidator;

    public ServerHandler(LobbyManager lobbyManager, GameLoopManager gameLoopManager, LeaderboardManager leaderboardManager) {
        this.lobbyManager = lobbyManager;
        this.gameLoopManager = gameLoopManager;
        this.leaderboardManager = leaderboardManager;
        this.antiCheatValidator = new AntiCheatValidator(); // Instantiate AntiCheatValidator
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("Client connected: {}", ctx.channel().remoteAddress());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("Client disconnected: {}", ctx.channel().remoteAddress());
        // TODO: Handle player leaving lobby on disconnect
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet msg) throws Exception {
        if (msg instanceof HeartbeatPacket) {
            // Respond to heartbeat to keep connection alive
            ctx.writeAndFlush(new HeartbeatPacket(System.currentTimeMillis()));
        } else if (msg instanceof InputPacket) {
            InputPacket inputPacket = (InputPacket) msg;
            // For now, we'll assume a player is associated with the channel for anti-cheat validation.
            // In a real scenario, player information would be retrieved from a session or authentication.
            Player dummyPlayer = new Player(UUID.randomUUID(), "dummyUser", 1000, 0, 0, Instant.now()); // Placeholder
            if (antiCheatValidator.validateInput(dummyPlayer, inputPacket)) {
                logger.debug("Received InputPacket from {}: {}", ctx.channel().remoteAddress(), inputPacket.getAction());
                // TODO: Process input based on game state and player's current lobby
                // Example: gameLoopManager.startGame(lobbyManager.getLobbyById(player.getLobbyId()), /* player channels */);
            } else {
                logger.warn("InputPacket from {} failed anti-cheat validation.", ctx.channel().remoteAddress());
                // Anti-cheat validator will handle penalties
            }
        } else if (msg instanceof ChatPacket) {
            ChatPacket chatPacket = (ChatPacket) msg;
            logger.info("Chat message from {}: {}", chatPacket.getSenderUsername(), chatPacket.getMessage());
            // TODO: Broadcast chat message to other clients in the same lobby
        } else if (msg instanceof JoinLobbyPacket) {
            JoinLobbyPacket joinLobbyPacket = (JoinLobbyPacket) msg;
            logger.info("Client {} wants to join lobby {}", joinLobbyPacket.getUsername(), joinLobbyPacket.getLobbyId());
            // For now, create a dummy player. In a real scenario, this would come from authentication.
            Player player = new Player(UUID.randomUUID(), joinLobbyPacket.getUsername(), 1000, 0, 0, Instant.now());
            boolean joined = lobbyManager.joinLobby(joinLobbyPacket.getLobbyId(), player, ctx);
            if (joined) {
                // TODO: Send success response to client and update other lobby members
                logger.info("Player {} successfully joined lobby {}", player.getUsername(), joinLobbyPacket.getLobbyId());
                // Example: gameLoopManager.startGame(lobbyManager.getLobbyById(joinLobbyPacket.getLobbyId()), /* player channels */);
            } else {
                // If joining failed, try to create a new lobby
                Lobby newLobby = lobbyManager.createLobby("Lobby for " + player.getUsername(), GameType.SNAKE, 2, player.getUsername());
                if (newLobby != null) {
                    lobbyManager.joinLobby(newLobby.getId(), player, ctx);
                    logger.info("Player {} created and joined new lobby {}", player.getUsername(), newLobby.getId());
                    // Example: gameLoopManager.startGame(newLobby, /* player channels */);
                } else {
                    // TODO: Send failure response to client
                    logger.warn("Player {} failed to join or create lobby {}", player.getUsername(), joinLobbyPacket.getLobbyId());
                }
            }
        } else if (msg instanceof LeaderboardRequestPacket) {
            logger.info("Received LeaderboardRequestPacket from {}", ctx.channel().remoteAddress());
            List<Player> topPlayers = leaderboardManager.getTopPlayers(10) // Get top 10 players
                                                        .stream()
                                                        .map(pe -> new Player(pe.getId(), pe.getUsername(), pe.getElo(), pe.getWins(), pe.getLosses(), pe.getLastActive()))
                                                        .collect(Collectors.toList());
            ctx.writeAndFlush(new LeaderboardResponsePacket(topPlayers));
        } else {
            logger.warn("Unknown packet type received: {}", msg.getClass().getName());
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            if (e.state() == IdleState.READER_IDLE) {
                logger.warn("Client {} idle for too long, disconnecting.", ctx.channel().remoteAddress());
                ctx.close();
            }
        }
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("Error in server handler for client {}: {}", ctx.channel().remoteAddress(), cause.getMessage(), cause);
        ctx.close();
    }
}
