package com.arcadehub.server;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AdminHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private static final Logger logger = LoggerFactory.getLogger(AdminHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        if (!request.decoderResult().isSuccess()) {
            sendError(ctx, HttpResponseStatus.BAD_REQUEST);
            return;
        }

        String uri = request.uri();
        HttpMethod method = request.method();

        String responseContent;
        if ("/health".equals(uri) && HttpMethod.GET.equals(method)) {
            responseContent = "{\"status\":\"OK\"}";
        } else if ("/ready".equals(uri) && HttpMethod.GET.equals(method)) {
            responseContent = "{\"status\":\"OK\"}";
        } else if ("/metrics".equals(uri) && HttpMethod.GET.equals(method)) {
            responseContent = "# HELP arcadehub_active_lobbies Active lobbies\n# TYPE arcadehub_active_lobbies gauge\narcadehub_active_lobbies 0\n";
        } else if (uri.startsWith("/admin/replay/") && HttpMethod.GET.equals(method)) {
            String id = uri.substring("/admin/replay/".length());
            // TODO: Auth check
            try {
                byte[] data = Files.readAllBytes(Paths.get("replays/archive", id + ".ndjson.gz"));
                FullHttpResponse response = new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK,
                    Unpooled.wrappedBuffer(data)
                );
                response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/octet-stream");
                response.headers().set(HttpHeaderNames.CONTENT_DISPOSITION, "attachment; filename=\"" + id + ".ndjson.gz\"");
                response.headers().set(HttpHeaderNames.CONTENT_LENGTH, data.length);
                ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
                return;
            } catch (IOException e) {
                sendError(ctx, HttpResponseStatus.NOT_FOUND);
                return;
            }
        } else {
            sendError(ctx, HttpResponseStatus.NOT_FOUND);
            return;
        }

        FullHttpResponse response = new DefaultFullHttpResponse(
            HttpVersion.HTTP_1_1,
            HttpResponseStatus.OK,
            Unpooled.copiedBuffer(responseContent, CharsetUtil.UTF_8)
        );
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(
            HttpVersion.HTTP_1_1,
            status,
            Unpooled.copiedBuffer(status.toString(), CharsetUtil.UTF_8)
        );
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("Error in admin handler", cause);
        ctx.close();
    }
}