package com.arcadehub.server.leaderboard;

import com.arcadehub.server.entity.PlayerEntity;
import com.arcadehub.shared.MatchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;

public class LeaderboardManager {
    private static final Logger logger = LoggerFactory.getLogger(LeaderboardManager.class);
    private static final String PERSISTENCE_UNIT_NAME = "arcadehub-pu";
    private EntityManagerFactory emf;

    public LeaderboardManager() {
        try {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        } catch (Exception e) {
            logger.error("Failed to initialize EntityManagerFactory: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to initialize LeaderboardManager", e);
        }
    }

    public List<PlayerEntity> getTopPlayers(int limit) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT p FROM PlayerEntity p ORDER BY p.elo DESC", PlayerEntity.class)
                     .setMaxResults(limit)
                     .getResultList();
        } finally {
            em.close();
        }
    }

    public void updateMatchResult(MatchResult result) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            for (String username : result.scores.keySet()) {
                PlayerEntity player = em.createQuery("SELECT p FROM PlayerEntity p WHERE p.username = :username", PlayerEntity.class)
                                        .setParameter("username", username)
                                        .getSingleResult();
                int score = result.scores.get(username);
                player.setGamesPlayed(player.getGamesPlayed() + 1);
                if (score > player.getHighestScore()) {
                    player.setHighestScore(score);
                }
                if (result.winner != null && result.winner.equals(username)) {
                    player.setWins(player.getWins() + 1);
                    // Simple ELO: winner +10, others -10
                    player.setElo(player.getElo() + 10);
                } else {
                    player.setLosses(player.getLosses() + 1);
                    player.setElo(player.getElo() - 10);
                }
                em.merge(player);
            }
            em.getTransaction().commit();
            logger.info("Updated match result: winner={}, scores={}", result.winner, result.scores);
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            logger.error("Failed to update match result: {}", e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    public void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
