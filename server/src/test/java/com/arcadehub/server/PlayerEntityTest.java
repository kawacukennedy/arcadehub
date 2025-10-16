package com.arcadehub.server;

import com.arcadehub.server.entity.PlayerEntity;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class PlayerEntityTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @Test
    void testPlayerPersistence() {
        // Set system properties for test DB
        System.setProperty("jakarta.persistence.jdbc.url", postgres.getJdbcUrl());
        System.setProperty("jakarta.persistence.jdbc.user", postgres.getUsername());
        System.setProperty("jakarta.persistence.jdbc.password", postgres.getPassword());

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("arcadehub");
        EntityManager em = emf.createEntityManager();

        PlayerEntity player = new PlayerEntity("alice");
        player.setElo(1200);

        em.getTransaction().begin();
        em.persist(player);
        em.getTransaction().commit();

        PlayerEntity found = em.find(PlayerEntity.class, player.getId());
        assertNotNull(found);
        assertEquals("alice", found.getUsername());
        assertEquals(1200, found.getElo());

        em.close();
        emf.close();
    }
}