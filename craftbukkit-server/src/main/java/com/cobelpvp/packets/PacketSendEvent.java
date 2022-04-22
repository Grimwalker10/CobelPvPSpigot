package com.cobelpvp.packets;

import net.minecraft.server.Packet;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PacketSendEvent extends PlayerEvent implements Cancellable
{
    private static final HandlerList handlers;
    private Packet packet;
    private boolean cancelled;

    static {
        handlers = new HandlerList();
    }

    public PacketSendEvent(final Player cheater, final Packet packet) {
        super(cheater);
        this.packet = packet;
        this.cancelled = false;
    }

    @Override
    public HandlerList getHandlers() {
        return PacketSendEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return PacketSendEvent.handlers;
    }

    public Packet getPacket() {
        return this.packet;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }
}
