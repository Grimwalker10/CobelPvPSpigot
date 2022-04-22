package com.cobelpvp.events;

import net.minecraft.server.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.List;

public class BlockDropItemsEvent extends Event implements Cancellable
{
    private static final HandlerList handlers;
    private Block block;
    private Player player;
    private List toDrop;
    private boolean cancelled;

    public BlockDropItemsEvent(final Block block, final Player player, final List toDrop) {
        this.cancelled = false;
        this.block = block;
        this.player = player;
        this.toDrop = toDrop;
    }

    public Block getBlock() {
        return this.block;
    }

    public Player getPlayer() {
        return this.player;
    }

    public List getToDrop() {
        return this.toDrop;
    }

    @Override
    public HandlerList getHandlers() {
        return BlockDropItemsEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return BlockDropItemsEvent.handlers;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    static {
        handlers = new HandlerList();
    }
}
