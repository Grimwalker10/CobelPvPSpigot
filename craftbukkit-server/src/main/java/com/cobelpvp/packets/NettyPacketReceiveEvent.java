package com.cobelpvp.packets;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class NettyPacketReceiveEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Player player;
    private Object packet;

    public NettyPacketReceiveEvent(Player player, Object packet) {
        this.player = player;
        this.packet = packet;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Object getPacket() {
        return this.packet;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
