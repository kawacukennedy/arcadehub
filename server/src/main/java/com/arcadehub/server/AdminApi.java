package com.arcadehub.server;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AdminApi {
    private static final Logger logger = LoggerFactory.getLogger(AdminApi.class);
    private final DataSource dataSource;
    private final String adminToken;
    private HttpServer server;

    public AdminApi(DataSource dataSource, String adminToken) {
        this.dataSource = dataSource;
        this.adminToken = adminToken;
    }

    public void start(int port) throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/admin/metrics", new MetricsHandler());
        server.createContext("/admin/lobbies", new LobbiesHandler());
        server.createContext("/admin/ban", new BanHandler());
        server.createContext("/admin/unban", new UnbanHandler());
        server.createContext("/health", new HealthHandler());
        server.setExecutor(null);
        server.start();
        logger.info("Admin API started on port {}", port);
    }

    public void stop() {
        if (server != null) server.stop(0);
    }

    private boolean authenticate(HttpExchange exchange) {
        String token = exchange.getRequestHeaders().getFirst("X-ADMIN-TOKEN");
        return adminToken.equals(token);
    }

    private class MetricsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!authenticate(exchange)) {
                exchange.sendResponseHeaders(401, -1);
                return;
            }
            String response = "{ \"active_matches\": 0, \"players_online\": 0 }"; // Placeholder
            exchange.sendResponseHeaders(200, response.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    private class LobbiesHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!authenticate(exchange)) {
                exchange.sendResponseHeaders(401, -1);
                return;
            }
            String response = "[]"; // Placeholder
            exchange.sendResponseHeaders(200, response.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    private class BanHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!authenticate(exchange)) {
                exchange.sendResponseHeaders(401, -1);
                return;
            }
            // TODO: Implement ban logic
            exchange.sendResponseHeaders(200, 0);
        }
    }

    private class UnbanHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!authenticate(exchange)) {
                exchange.sendResponseHeaders(401, -1);
                return;
            }
            // TODO: Implement unban logic
            exchange.sendResponseHeaders(200, 0);
        }
    }

    private class HealthHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.sendResponseHeaders(200, 0);
        }
    }
}