package com.cobelpvp.packets;

import net.minecraft.server.PacketPlayInFlying;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerPositionEvent extends PacketReceiveEvent
{
    private static HandlerList handlerList;
    private final Phase phase;
    private Location from;
    private Location to;
    private boolean fromGround;
    private boolean toGround;

    static {
        PlayerPositionEvent.handlerList = new HandlerList();
    }

    public PlayerPositionEvent(final Player player, final Location from, final Location to, final boolean fromGround, final boolean toGround, final PacketPlayInFlying packet, final Phase phase) {
        super(player, packet);
        this.phase = phase;
        this.from = from;
        this.to = to;
        this.fromGround = fromGround;
        this.toGround = toGround;
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerPositionEvent.handlerList;
    }

    public static HandlerList getHandlerList() {
        return PlayerPositionEvent.handlerList;
    }

    public Phase getPhase() {
        return this.phase;
    }

    public Location getFrom() {
        return this.from;
    }

    public Location getTo() {
        return this.to;
    }

    public boolean isFromGround() {
        return this.fromGround;
    }

    public boolean isToGround() {
        return this.toGround;
    }

    public enum Phase
    {
        PRE("PRE", 0),
        POST("POST", 1);

        private Phase(final String s, final int n) {
        }
    }
}
