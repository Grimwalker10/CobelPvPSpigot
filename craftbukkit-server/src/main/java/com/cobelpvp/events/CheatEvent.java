package com.cobelpvp.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CheatEvent extends Event
{
    private static final HandlerList handlers;
    private Player player;
    private Type cheatType;
    private Level alertLevel;
    private String alertMessage;

    public CheatEvent(final Player player, final Type type, final Level alert, final String message) {
        this.player = player;
        this.cheatType = type;
        this.alertLevel = alert;
        this.alertMessage = message;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Type getType() {
        return this.cheatType;
    }

    public Level getLevel() {
        return this.alertLevel;
    }

    public String getMessage() {
        return this.alertMessage;
    }

    @Override
    public HandlerList getHandlers() {
        return CheatEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return CheatEvent.handlers;
    }

    static {
        handlers = new HandlerList();
    }

    public enum Type
    {
        PHASE,
        VCLIP,
        KILL_AURA,
        REGEN,
        CRIT,
        FAST_EAT_MACHINE_GUN,
        FLY,
        TIMER,
        ANTI_KB,
        HOVER,
        SPEED,
        INVENTORY,
        UNKNOWN,
        REACH,
        INVALID_PACKET;
    }

    public enum Level
    {
        MODERATOR,
        ADMIN,
        OWNER;
    }
}
