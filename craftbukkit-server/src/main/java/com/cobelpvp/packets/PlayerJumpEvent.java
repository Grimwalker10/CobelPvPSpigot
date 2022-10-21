package com.cobelpvp.packets;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerJumpEvent extends PlayerMoveEvent implements Cancellable {
    public PlayerJumpEvent(Player player, Location from, Location to) {
        super(player, from, to);
    }
}
