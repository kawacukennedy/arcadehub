
package com.arcadehub.server.network.packet;

import com.arcadehub.common.Player;

import java.util.List;

public class LeaderboardResponsePacket extends BasePacket {

    private List<Player> topPlayers;

    public LeaderboardResponsePacket() {
        super(PacketType.LEADERBOARD_RESPONSE);
    }

    public List<Player> getTopPlayers() {
        return topPlayers;
    }

    public void setTopPlayers(List<Player> topPlayers) {
        this.topPlayers = topPlayers;
    }
}
