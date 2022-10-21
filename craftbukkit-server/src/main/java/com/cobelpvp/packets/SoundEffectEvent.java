package com.cobelpvp.packets;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SoundEffectEvent extends Event implements Cancellable
{
    private static final HandlerList handlers;
    private final Entity entity;
    private String sound;
    private boolean cancelled;

    public SoundEffectEvent(final String sound, final Entity entity) {
        this.cancelled = false;
        this.sound = sound;
        this.entity = entity;
    }

    public static HandlerList getHandlerList() {
        return SoundEffectEvent.handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return SoundEffectEvent.handlers;
    }

    public Entity getEntity() {
        return this.entity;
    }

    public String getSound() {
        return this.sound;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setSound(final String sound) {
        this.sound = sound;
    }

    @Override
    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SoundEffectEvent)) {
            return false;
        }
        final SoundEffectEvent other = (SoundEffectEvent)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$entity = this.getEntity();
        final Object other$entity = other.getEntity();
        Label_0065: {
            if (this$entity == null) {
                if (other$entity == null) {
                    break Label_0065;
                }
            }
            else if (this$entity.equals(other$entity)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$sound = this.getSound();
        final Object other$sound = other.getSound();
        if (this$sound == null) {
            if (other$sound == null) {
                return this.isCancelled() == other.isCancelled();
            }
        }
        else if (this$sound.equals(other$sound)) {
            return this.isCancelled() == other.isCancelled();
        }
        return false;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof SoundEffectEvent;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $entity = this.getEntity();
        result = result * 59 + (($entity == null) ? 43 : $entity.hashCode());
        final Object $sound = this.getSound();
        result = result * 59 + (($sound == null) ? 43 : $sound.hashCode());
        result = result * 59 + (this.isCancelled() ? 79 : 97);
        return result;
    }

    @Override
    public String toString() {
        return "SoundEffectEvent(entity=" + this.getEntity() + ", sound=" + this.getSound() + ", cancelled=" + this.isCancelled() + ")";
    }

    static {
        handlers = new HandlerList();
    }
}
