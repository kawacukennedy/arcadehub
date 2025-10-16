package com.arcadehub.server.leaderboard;

import com.arcadehub.server.entity.PlayerEntity;
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

    public void updatePlayerStats(String username, int eloChange, boolean won) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        try {
            PlayerEntity player = em.createQuery("SELECT p FROM PlayerEntity p WHERE p.username = :username", PlayerEntity.class)
                                    .setParameter("username", username)
                                    .getSingleResult();
            player.setElo(player.getElo() + eloChange);
            if (won) {
                player.setWins(player.getWins() + 1);
            } else {
                player.setLosses(player.getLosses() + 1);
            }
            em.merge(player);
            em.getTransaction().commit();
            logger.info("Updated stats for player {}: ELO={}, Wins={}, Losses={}", username, player.getElo(), player.getWins(), player.getLosses());
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            logger.error("Failed to update player stats for {}: {}", username, e.getMessage(), e);
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
