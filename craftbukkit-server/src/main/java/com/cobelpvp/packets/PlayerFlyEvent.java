package com.cobelpvp.packets;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerFlyEvent extends Event implements Cancellable
{
    private static final HandlerList handlers;
    private final Player player;
    private boolean iscancel;

    public PlayerFlyEvent(final Player player) {
        this.player = player;
    }

    public static HandlerList getHandlerList() {
        return PlayerFlyEvent.handlers;
    }

    public Player getPlayer() {
        return this.player;
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerFlyEvent.handlers;
    }

    @Override
    public boolean isCancelled() {
        return this.iscancel;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.iscancel = cancel;
    }

    static {
        handlers = new HandlerList();
    }
}

