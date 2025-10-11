package com.arcadehub.client.network;

import com.arcadehub.shared.HeartbeatPacket;
import com.arcadehub.shared.Packet;
import com.arcadehub.shared.StateUpdatePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientHandler extends SimpleChannelInboundHandler<Packet> {
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
    protected void channelRead0(ChannelHandlerContext ctx, Packet msg) throws Exception {
        if (msg instanceof HeartbeatPacket) {
            logger.debug("Received Heartbeat from server.");
        } else if (msg instanceof StateUpdatePacket) {
            StateUpdatePacket stateUpdate = (StateUpdatePacket) msg;
            logger.info("Received StateUpdatePacket from server at timestamp: {}", stateUpdate.getTimestamp());
            // TODO: Update game UI based on stateUpdate
        } else {
            logger.info("Received packet from server: {}", msg.getClass().getSimpleName());
            // TODO: Handle other packet types (ChatPacket, LobbyUpdatePacket, LeaderboardResponsePacket)
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            if (e.state() == IdleState.WRITER_IDLE) {
                // Send heartbeat to server to keep connection alive
                ctx.writeAndFlush(new HeartbeatPacket(System.currentTimeMillis()));
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
