package com.arcadehub.server;

import com.arcadehub.shared.Packet;
import com.arcadehub.shared.NetworkPacket;
import com.arcadehub.shared.HeartbeatPacket;
import com.arcadehub.shared.HeartbeatPayload;
import com.arcadehub.shared.InputPacket;
import com.arcadehub.shared.Player;
import com.arcadehub.shared.ChatPacket;
import com.arcadehub.shared.JoinLobbyPacket;
import com.arcadehub.shared.JoinAcceptPayload;
import com.arcadehub.shared.ErrorPayload;
import com.arcadehub.shared.InputEnvelope;
import com.arcadehub.shared.Lobby;
import com.arcadehub.shared.GameType;
import com.arcadehub.shared.LeaderboardRequestPacket;
import com.arcadehub.shared.LeaderboardResponsePacket;
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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerHandler extends SimpleChannelInboundHandler<NetworkPacket> {
    private static final Logger logger = LoggerFactory.getLogger(ServerHandler.class);

    private final LobbyManager lobbyManager;
    private final GameLoopManager gameLoopManager;
    private final LeaderboardManager leaderboardManager;
    private final AntiCheatValidator antiCheatValidator;
    private final SessionManager sessionManager;
    private final Map<String, UUID> sessionToLobby = new ConcurrentHashMap<>();

    public ServerHandler(LobbyManager lobbyManager, GameLoopManager gameLoopManager, LeaderboardManager leaderboardManager) {
        this.lobbyManager = lobbyManager;
        this.gameLoopManager = gameLoopManager;
        this.leaderboardManager = leaderboardManager;
        this.antiCheatValidator = new AntiCheatValidator();
        this.sessionManager = new SessionManager();
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
    protected void channelRead0(ChannelHandlerContext ctx, NetworkPacket netPacket) throws Exception {
        Object payload = netPacket.getPayload();
        if (payload instanceof Packet) {
            Packet msg = (Packet) payload;
        if (msg instanceof HeartbeatPacket) {
            // Respond to heartbeat to keep connection alive
            HeartbeatPayload hbPayload = new HeartbeatPayload(System.currentTimeMillis());
            ctx.writeAndFlush(new NetworkPacket("HEARTBEAT", 1, null, hbPayload));
        } else if (msg instanceof InputPacket) {
            InputPacket inputPacket = (InputPacket) msg;
            String sessionToken = netPacket.getSessionId();
            if (sessionToken == null || !sessionManager.verifySignature(sessionToken, inputPacket.getPayload().getTick(), inputPacket.getPayload().getAction(), inputPacket.getPayload().getSignature())) {
                logger.warn("Invalid signature for input from {}", ctx.channel().remoteAddress());
                ErrorPayload errorPayload = new ErrorPayload("INVALID_SIGNATURE", "HMAC signature mismatch");
                ctx.writeAndFlush(new NetworkPacket("ERROR", 1, null, errorPayload));
                return;
            }
            // TODO: Get player from session
            Player dummyPlayer = new Player(UUID.randomUUID(), inputPacket.getPayload().getUsername(), 1000, 0, 0, Instant.now());
            if (antiCheatValidator.validateInput(dummyPlayer, inputPacket)) {
                logger.debug("Received valid InputPacket: {}", inputPacket.getPayload().getAction());
                // Queue input for game loop
                InputEnvelope envelope = new InputEnvelope(System.nanoTime(), inputPacket.getPayload().getTick(),
                    inputPacket.getPayload().getUsername(), inputPacket.getPayload().getAction(),
                    inputPacket.getPayload().getSignature(), "{}");
                UUID lobbyId = sessionToLobby.get(sessionToken);
                if (lobbyId != null) {
                    gameLoopManager.queueInput(lobbyId, envelope);
                }
            } else {
                logger.warn("Input failed anti-cheat validation.");
            }
        } else if (msg instanceof ChatPacket) {
            ChatPacket chatPacket = (ChatPacket) msg;
            logger.info("Chat message from {}: {}", chatPacket.getSenderUsername(), chatPacket.getMessage());
            // TODO: Broadcast chat message to other clients in the same lobby
        } else if (msg instanceof JoinLobbyPacket) {
            JoinLobbyPacket joinPacket = (JoinLobbyPacket) msg;
            String username = joinPacket.getPayload().getUsername();
            logger.info("Client {} wants to join lobby {}", username, joinPacket.getPayload().getLobbyId());
            // Create session
            String sessionToken = sessionManager.createSession();
            // For now, dummy player
            Player player = new Player(UUID.randomUUID(), username, 1000, 0, 0, Instant.now());
            boolean joined = lobbyManager.joinLobby(joinPacket.getPayload().getLobbyId(), player, ctx);
            if (joined) {
                // Send JOIN_ACCEPT
                JoinAcceptPayload acceptPayload = new JoinAcceptPayload(sessionToken, player.getId().toString(), 0); // currentTick 0
                ctx.writeAndFlush(new NetworkPacket("JOIN_ACCEPT", 1, sessionToken, acceptPayload));
                logger.info("Player {} joined lobby {}", username, joinPacket.getPayload().getLobbyId());
            } else {
                // Create new lobby
                Lobby newLobby = lobbyManager.createLobby("Lobby for " + username, GameType.SNAKE, 2, username);
                if (newLobby != null) {
                    lobbyManager.joinLobby(newLobby.getId(), player, ctx);
                sessionToLobby.put(sessionToken, joinPacket.getPayload().getLobbyId() != null ? UUID.fromString(joinPacket.getPayload().getLobbyId()) : newLobby.getId());
                JoinAcceptPayload acceptPayload = new JoinAcceptPayload(sessionToken, player.getId().toString(), 0);
                ctx.writeAndFlush(new NetworkPacket("JOIN_ACCEPT", 1, sessionToken, acceptPayload));
                    logger.info("Player {} created and joined new lobby {}", username, newLobby.getId());
                } else {
                    // Send error
                    ErrorPayload errorPayload = new ErrorPayload("LOBBY_FULL", "Unable to join or create lobby");
                    ctx.writeAndFlush(new NetworkPacket("ERROR", 1, null, errorPayload));
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
        } else {
            logger.warn("Unknown payload type: {}", payload.getClass().getName());
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
