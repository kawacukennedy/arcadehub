package com.arcadehub.server;

import java.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
    private static final Logger logger = LoggerFactory.getLogger(SessionManager.class);
    private static final String HMAC_ALGO = "HmacSHA256";
    private final Map<String, byte[]> sessionKeys = new ConcurrentHashMap<>();
    private final SecureRandom random = new SecureRandom();

    public String createSession() {
        byte[] key = new byte[32]; // 256 bits
        random.nextBytes(key);
        String token = Base64.getUrlEncoder().encodeToString(key); // Use first 32 bytes as token? No, token is base64 of key.
        sessionKeys.put(token, key);
        logger.debug("Created session: {}", token);
        return token;
    }

    public boolean verifySignature(String sessionToken, int tick, String action, String signature) {
        byte[] key = sessionKeys.get(sessionToken);
        if (key == null) {
            logger.warn("Invalid session token: {}", sessionToken);
            return false;
        }
        String data = sessionToken + ":" + tick + ":" + action;
        try {
            Mac mac = Mac.getInstance(HMAC_ALGO);
            SecretKeySpec keySpec = new SecretKeySpec(key, HMAC_ALGO);
            mac.init(keySpec);
            byte[] computed = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            String computedSig = Base64.getUrlEncoder().encodeToString(computed);
            return computedSig.equals(signature);
        } catch (Exception e) {
            logger.error("Error verifying signature", e);
            return false;
        }
    }

    public void removeSession(String token) {
        sessionKeys.remove(token);
        logger.debug("Removed session: {}", token);
    }
}