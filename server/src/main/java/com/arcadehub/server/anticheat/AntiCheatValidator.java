package com.arcadehub.server.anticheat;

import com.arcadehub.shared.InputPacket;
import com.arcadehub.shared.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AntiCheatValidator {
    private static final Logger logger = LoggerFactory.getLogger(AntiCheatValidator.class);

    // Client rate limits
    private static final int INPUT_PACKETS_PER_SECOND = 20; // From project spec
    private final Map<String, Long> lastInputTimestamp = new ConcurrentHashMap<>();
    private final Map<String, Integer> inputCount = new ConcurrentHashMap<>();
    private final Map<String, Integer> violationPoints = new ConcurrentHashMap<>();

    public boolean validateInput(Player player, InputPacket inputPacket) {
        // Validate input rate
        long currentTime = System.currentTimeMillis();
        String username = player.getUsername();

        lastInputTimestamp.putIfAbsent(username, currentTime);
        inputCount.putIfAbsent(username, 0);

        long lastTime = lastInputTimestamp.get(username);
        int count = inputCount.get(username);

        if (currentTime - lastTime < 1000) { // Within the same second
            if (count >= INPUT_PACKETS_PER_SECOND) {
                logger.warn("Anti-cheat: Player {} exceeding input rate limit.", username);
                addViolationPoints(username, 3); // excessive_rate
                return false;
            }
            inputCount.put(username, count + 1);
        } else { // New second
            lastInputTimestamp.put(username, currentTime);
            inputCount.put(username, 1);
        }

        // Server-side validations (placeholders for now)
        // "Reject inputs beyond tick +2"
        // "Reject position deltas exceeding max speed"
        // "Reject negative/NaN coordinates"

        // Reject invalid signatures (already checked in ServerHandler)
        // For now, assume signature is valid

        // TODO: Add game-specific validations (impossible movements, etc.)

        // Add more specific game-related anti-cheat checks here

        return true;
    }

    private void addViolationPoints(String username, int points) {
        int current = violationPoints.getOrDefault(username, 0);
        current += points;
        violationPoints.put(username, current);
        if (current >= 20) { // auto_block_threshold
            logger.error("Auto-blocking player {} for excessive violations", username);
            // TODO: Block player
        }
    }

    // TODO: Implement penalty system (warning, kick, ban)
    public void applyPenalty(Player player, int errorCode) {
        logger.warn("Applying penalty for player {} with error code {}", player.getUsername(), errorCode);
        // 1st_offense: "Warning log"
        // 2nd_offense: "Temporary kick (300s)"
        // 3rd_offense: "Permanent ban (DB flag)"
    }
}
