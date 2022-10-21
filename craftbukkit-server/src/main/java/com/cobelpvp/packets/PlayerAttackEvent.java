package com.cobelpvp.packets;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerAttackEvent extends PlayerEvent implements Cancellable
{
    private static final HandlerList handlers;
    private boolean cancelled;
    private Player attacker;
    private Player victim;

    public PlayerAttackEvent(final Player attacker, final Player victim) {
        super(attacker);
        this.victim = victim;
    }

    public static HandlerList getHandlerList() {
        return PlayerAttackEvent.handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerAttackEvent.handlers;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    public Player getAttacker() {
        return this.attacker;
    }

    public Player getVictim() {
        return this.victim;
    }

    @Override
    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }

    public void setAttacker(final Player attacker) {
        this.attacker = attacker;
    }

    public void setVictim(final Player victim) {
        this.victim = victim;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PlayerAttackEvent)) {
            return false;
        }
        final PlayerAttackEvent other = (PlayerAttackEvent)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.isCancelled() != other.isCancelled()) {
            return false;
        }
        final Object this$attacker = this.getAttacker();
        final Object other$attacker = other.getAttacker();
        Label_0078: {
            if (this$attacker == null) {
                if (other$attacker == null) {
                    break Label_0078;
                }
            }
            else if (this$attacker.equals(other$attacker)) {
                break Label_0078;
            }
            return false;
        }
        final Object this$victim = this.getVictim();
        final Object other$victim = other.getVictim();
        if (this$victim == null) {
            if (other$victim == null) {
                return true;
            }
        }
        else if (this$victim.equals(other$victim)) {
            return true;
        }
        return false;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof PlayerAttackEvent;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + (this.isCancelled() ? 79 : 97);
        final Object $attacker = this.getAttacker();
        result = result * 59 + (($attacker == null) ? 43 : $attacker.hashCode());
        final Object $victim = this.getVictim();
        result = result * 59 + (($victim == null) ? 43 : $victim.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "PlayerAttackEvent(cancelled=" + this.isCancelled() + ", attacker=" + this.getAttacker() + ", victim=" + this.getVictim() + ")";
    }

    static {
        handlers = new HandlerList();
    }
}
