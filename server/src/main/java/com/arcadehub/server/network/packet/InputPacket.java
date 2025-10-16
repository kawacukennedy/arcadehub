
package com.arcadehub.server.network.packet;

public class InputPacket extends BasePacket {

    public enum Action {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    private String username;
    private Action action;
    private long tick;

    public InputPacket() {
        super(PacketType.INPUT);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public long getTick() {
        return tick;
    }

    public void setTick(long tick) {
        this.tick = tick;
    }
}
