package com.cobelpvp.packets;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerAttackEvent extends PlayerEvent
{
    private static final HandlerList handlers;

    static {
        handlers = new HandlerList();
    }

    public PlayerAttackEvent(final Player player) {
        super(player);
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerAttackEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return PlayerAttackEvent.handlers;
    }
}
