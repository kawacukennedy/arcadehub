
package com.arcadehub.client.network.packet;

public class LeaderboardRequestPacket extends BasePacket {

    private int limit;

    public LeaderboardRequestPacket() {
        super(PacketType.LEADERBOARD_REQUEST);
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
