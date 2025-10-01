package com.arcadehub.client.network;

import com.arcadehub.common.Packet;
import com.arcadehub.common.StateUpdatePacket;
import com.arcadehub.common.ChatPacket;
import com.arcadehub.common.LobbyUpdatePacket;
import com.arcadehub.common.LeaderboardResponsePacket;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientHandler extends SimpleChannelInboundHandler<String> {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final com.arcadehub.client.game.GameRenderer gameRenderer;
    private com.arcadehub.client.ui.UIManager uiManager; // Removed 'final'

    public ClientHandler(com.arcadehub.client.game.GameRenderer gameRenderer, com.arcadehub.client.ui.UIManager uiManager) {
        this.gameRenderer = gameRenderer;
        this.uiManager = uiManager;
    }

    public void setUIManager(com.arcadehub.client.ui.UIManager uiManager) {
        // This method is used to set the UIManager after it has been fully initialized in Main.
        // This helps resolve circular dependency issues during initialization.
        // Note: In a more robust application, consider using dependency injection frameworks.
        this.uiManager = uiManager;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Packet basePacket = objectMapper.readValue(msg, Packet.class);

        switch (basePacket.getClass().getSimpleName()) {
            case "StateUpdatePacket":
                gameRenderer.updatePositions(((StateUpdatePacket) basePacket).getGameState());
                break;
            case "LeaderboardResponsePacket":
                handleLeaderboardResponse(ctx, (LeaderboardResponsePacket) basePacket);
                break;
            case "ChatPacket":
                handleChat(ctx, (ChatPacket) basePacket);
                break;
            case "LobbyUpdatePacket":
                handleLobbyUpdate(ctx, (LobbyUpdatePacket) basePacket);
                break;
            // TODO: Handle other packet types
            default:
                System.out.println("Unknown packet type: " + basePacket.getClass().getSimpleName());
        }
    }

    private void handleLobbyUpdate(ChannelHandlerContext ctx, LobbyUpdatePacket packet) {
        uiManager.showLobby(packet.getLobby());
    }

    private void handleChat(ChannelHandlerContext ctx, ChatPacket packet) {
        uiManager.appendChatMessage(packet.getUsername(), packet.getMessage());
    }

    private void handleLeaderboardResponse(ChannelHandlerContext ctx, LeaderboardResponsePacket packet) {
        uiManager.showLeaderboard(packet.getTopPlayers());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}