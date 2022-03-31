package com.cobelpvp.packets;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerPositionUpdateEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancel = false;
    private Location from;
    private Location to;
    private boolean onGround;

    public PlayerPositionUpdateEvent(Player player, Location from, Location to, boolean on) {
        super(player);
        this.from = from;
        this.to = to;
        this.onGround = on;
    }

    public boolean isCancelled() {
        return this.cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public Location getFrom() {
        return this.from;
    }

    public void setFrom(Location from) {
        this.from = from;
    }

    public Location getTo() {
        return this.to;
    }

    public void setTo(Location to) {
        this.to = to;
    }

    public boolean isFromGround() {
        return this.onGround;
    }

    public boolean isToGround() {
        return !this.onGround;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
