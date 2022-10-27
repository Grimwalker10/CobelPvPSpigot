package com.cobelpvp;

import com.cobelpvp.handler.MovementHandler;
import com.cobelpvp.handler.PacketHandler;
import org.bukkit.Bukkit;

import java.util.HashSet;
import java.util.Set;

public enum CobelSpigot {
    INSTANCE("INSTANCE", 0);

    private Set<PacketHandler> packetHandlers;
    private Set<MovementHandler> movementHandlers;

    private CobelSpigot(final String s, final int n) {
        this.packetHandlers = new HashSet<PacketHandler>();
        this.movementHandlers = new HashSet<MovementHandler>();
    }

    public Set<PacketHandler> getPacketHandlers() {
        return this.packetHandlers;
    }

    public Set<MovementHandler> getMovementHandlers() {
        return this.movementHandlers;
    }

    public void addPacketHandler(final PacketHandler handler) {
        Bukkit.getLogger().info("Adding packet handler: " + handler.getClass().getPackage().getName() + "." + handler.getClass().getName());
        this.packetHandlers.add(handler);
    }

    public void addMovementHandler(final MovementHandler handler) {
        Bukkit.getLogger().info("Adding movement handler: " + handler.getClass().getPackage().getName() + "." + handler.getClass().getName());
        this.movementHandlers.add(handler);
    }
}
