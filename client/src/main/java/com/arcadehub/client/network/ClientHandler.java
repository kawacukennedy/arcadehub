package com.arcadehub.client.network;

import com.arcadehub.shared.HeartbeatPacket;
import com.arcadehub.shared.HeartbeatPayload;
import com.arcadehub.shared.JoinAcceptPacket;
import com.arcadehub.shared.Packet;
import com.arcadehub.shared.StateUpdatePacket;
import com.arcadehub.shared.NetworkPacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientHandler extends SimpleChannelInboundHandler<NetworkPacket> {
    private static final Logger logger = LoggerFactory.getLogger(ClientHandler.class);

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
            if (msg instanceof HeartbeatPacket) {
                logger.debug("Received Heartbeat from server.");
            } else if (msg instanceof JoinAcceptPacket) {
                JoinAcceptPacket accept = (JoinAcceptPacket) msg;
                ClientNetworkManager.setSessionToken(netPacket.getSessionId());
                logger.info("Joined lobby, session: {}", netPacket.getSessionId());
            } else if (msg instanceof StateUpdatePacket) {
            StateUpdatePacket stateUpdate = (StateUpdatePacket) msg;
            logger.info("Received StateUpdatePacket from server at timestamp: {}", stateUpdate.getTimestamp());
            // TODO: Update game UI based on stateUpdate
        } else {
            logger.info("Received packet from server: {}", msg.getClass().getSimpleName());
            // TODO: Handle other packet types (ChatPacket, LobbyUpdatePacket, LeaderboardResponsePacket)
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
                HeartbeatPayload hbPayload = new HeartbeatPayload(System.currentTimeMillis());
                ctx.writeAndFlush(new NetworkPacket("HEARTBEAT", 1, null, hbPayload));
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
