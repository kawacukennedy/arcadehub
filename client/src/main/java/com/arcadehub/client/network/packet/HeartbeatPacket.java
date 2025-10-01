
package com.arcadehub.client.network.packet;

public class HeartbeatPacket extends BasePacket {

    private long timestamp;

    public HeartbeatPacket() {
        super(PacketType.HEARTBEAT);
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
