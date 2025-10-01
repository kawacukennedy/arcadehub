
package com.arcadehub.client.network.packet;

public class ChatPacket extends BasePacket {

    private String username;
    private String message;

    public ChatPacket() {
        super(PacketType.CHAT);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
