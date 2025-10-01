
package com.arcadehub.client.network.packet;

public class ReadyPacket extends BasePacket {

    private String username;
    private boolean ready;

    public ReadyPacket() {
        super(PacketType.READY);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }
}
