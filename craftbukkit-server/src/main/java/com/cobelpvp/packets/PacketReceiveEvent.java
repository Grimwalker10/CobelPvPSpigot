package com.cobelpvp.packets;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PacketReceiveEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private Player player;
    private Object packet;
    private boolean canceled;

    public PacketReceiveEvent(Player player, Object packet) {
        this.player = player;
        this.packet = packet;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Object getPacket() {
        return this.packet;
    }

    public boolean isCancelled() {
        return this.canceled;
    }

    public void setCancelled(boolean cancel) {
        this.canceled = cancel;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
