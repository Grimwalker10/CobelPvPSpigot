package com.cobelpvp.packets;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.server.PacketPlayInCustomPayload;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@RequiredArgsConstructor
@Getter
public class PacketCustomPayloadEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    @Getter
    private final CraftPlayer player;

    private final String name;
    private final PacketPlayInCustomPayload payload;

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
