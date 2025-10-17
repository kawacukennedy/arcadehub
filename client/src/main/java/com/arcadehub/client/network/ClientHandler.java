package com.arcadehub.client.network;

import com.arcadehub.shared.Packet;
import com.arcadehub.shared.PacketType;
import com.arcadehub.shared.NetworkPacket;
import java.util.Map;
import com.arcadehub.client.game.GameRenderer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientHandler extends SimpleChannelInboundHandler<NetworkPacket> {
    private static final Logger logger = LoggerFactory.getLogger(ClientHandler.class);
    private static GameRenderer gameRenderer;

    public static void setGameRenderer(GameRenderer gr) {
        gameRenderer = gr;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("Connected to server: {}", ctx.channel().remoteAddress());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("Disconnected from server: {}", ctx.channel().remoteAddress());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, NetworkPacket netPacket) throws Exception {
        Object payload = netPacket.getPayload();
        if (payload instanceof Packet) {
            Packet msg = (Packet) payload;
            if (msg.type == PacketType.HEARTBEAT) {
                logger.debug("Received Heartbeat from server.");
            } else if (msg.type == PacketType.JOIN_RESPONSE) {
                ClientNetworkManager.setSessionToken(netPacket.getSessionId());
                logger.info("Joined lobby, session: {}", netPacket.getSessionId());
            } else if (msg.type == PacketType.STATE_UPDATE) {
                if (gameRenderer != null) {
                    // Assume payload has "state" key
                    gameRenderer.updatePositions((com.arcadehub.shared.GameState) msg.payload.get("state"));
                }
                logger.info("Received StateUpdate from server");
            } else {
                logger.info("Received packet from server: {}", msg.type);
            }
        } else {
            logger.warn("Unknown payload type: {}", payload.getClass().getName());
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            if (e.state() == IdleState.WRITER_IDLE) {
                // Send heartbeat to server to keep connection alive
                Packet hbPacket = new Packet();
                hbPacket.type = PacketType.HEARTBEAT;
                hbPacket.payload = Map.of("timestamp", System.currentTimeMillis());
                ctx.writeAndFlush(new NetworkPacket("HEARTBEAT", 1, null, hbPacket));
                logger.debug("Sending Heartbeat to server.");
            }
        }
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("Error in client handler: {}", cause.getMessage(), cause);
        ctx.close();
    }
}
