package com.arcadehub.server.leaderboard;

import com.arcadehub.server.entity.PlayerEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LeaderboardManagerTest {

    private LeaderboardManager leaderboardManager;

    @Mock
    private EntityManagerFactory mockEmf;
    @Mock
    private EntityManager mockEm;
    @Mock
    private EntityTransaction mockTx;
    @Mock
    private TypedQuery<PlayerEntity> mockQuery;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mockEmf.createEntityManager()).thenReturn(mockEm);
        when(mockEm.getTransaction()).thenReturn(mockTx);

        // Inject mockEmf into LeaderboardManager (requires a way to set it, or modify constructor)
        // For now, we'll create a LeaderboardManager that uses our mockEmf
        leaderboardManager = new LeaderboardManager() {
            @Override
            protected EntityManagerFactory createEntityManagerFactory() {
                return mockEmf;
            }
        };
    }

    @Test
    void testGetTopPlayers() {
        PlayerEntity player1 = new PlayerEntity("user1", 1500, 10, 5, Instant.now());
        player1.setId(UUID.randomUUID());
        PlayerEntity player2 = new PlayerEntity("user2", 1400, 8, 7, Instant.now());
        player2.setId(UUID.randomUUID());
        List<PlayerEntity> mockPlayers = Arrays.asList(player1, player2);

        when(mockEm.createQuery(anyString(), eq(PlayerEntity.class))).thenReturn(mockQuery);
        when(mockQuery.setMaxResults(anyInt())).thenReturn(mockQuery);
        when(mockQuery.getResultList()).thenReturn(mockPlayers);

        List<PlayerEntity> topPlayers = leaderboardManager.getTopPlayers(2);

        assertNotNull(topPlayers);
        assertEquals(2, topPlayers.size());
        assertEquals("user1", topPlayers.get(0).getUsername());
        assertEquals("user2", topPlayers.get(1).getUsername());

        verify(mockEm).createQuery("SELECT p FROM PlayerEntity p ORDER BY p.elo DESC", PlayerEntity.class);
        verify(mockQuery).setMaxResults(2);
        verify(mockQuery).getResultList();
        verify(mockEm).close();
    }

    @Test
    void testUpdatePlayerStats() {
        String username = "testUser";
        PlayerEntity player = new PlayerEntity(username, 1000, 0, 0, Instant.now());
        player.setId(UUID.randomUUID());

        when(mockEm.createQuery(anyString(), eq(PlayerEntity.class))).thenReturn(mockQuery);
        when(mockQuery.setParameter(anyString(), any())).thenReturn(mockQuery);
        when(mockQuery.getSingleResult()).thenReturn(player);

        leaderboardManager.updatePlayerStats(username, 50, true);

        verify(mockTx).begin();
        verify(mockEm).merge(player);
        verify(mockTx).commit();
        verify(mockEm).close();

        assertEquals(1050, player.getElo());
        assertEquals(1, player.getWins());
        assertEquals(0, player.getLosses());

        leaderboardManager.updatePlayerStats(username, -20, false);

        verify(mockTx, times(2)).begin();
        verify(mockEm, times(2)).merge(player);
        verify(mockTx, times(2)).commit();
        verify(mockEm, times(2)).close();

        assertEquals(1030, player.getElo());
        assertEquals(1, player.getWins());
        assertEquals(1, player.getLosses());
    }

    @Test
    void testUpdatePlayerStatsRollbackOnException() {
        String username = "testUser";
        PlayerEntity player = new PlayerEntity(username, 1000, 0, 0, Instant.now());
        player.setId(UUID.randomUUID());

        when(mockEm.createQuery(anyString(), eq(PlayerEntity.class))).thenReturn(mockQuery);
        when(mockQuery.setParameter(anyString(), any())).thenReturn(mockQuery);
        when(mockQuery.getSingleResult()).thenReturn(player);

        doThrow(new RuntimeException("DB Error")).when(mockTx).commit();
        when(mockTx.isActive()).thenReturn(true);

        leaderboardManager.updatePlayerStats(username, 50, true);

        verify(mockTx).begin();
        verify(mockEm).merge(player);
        verify(mockTx).commit();
        verify(mockTx).rollback();
        verify(mockEm).close();

        // ELO and wins should not have changed due to rollback
        assertEquals(1000, player.getElo());
        assertEquals(0, player.getWins());
        assertEquals(0, player.getLosses());
    }
}
