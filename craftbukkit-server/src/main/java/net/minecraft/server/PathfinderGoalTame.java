package net.minecraft.server;

import net.minecraft.optimizations.pathsearch.PositionPathSearchType;

public class PathfinderGoalTame extends PathfinderGoal {

    private EntityHorse entity;
    private double b;
    private double c;
    private double d;
    private double e;

    public PathfinderGoalTame(EntityHorse entityhorse, double d0) {
        this.entity = entityhorse;
        this.b = d0;
        this.a(1);
    }

    public boolean a() {
        if (!this.entity.isTame() && this.entity.passenger != null) {
            Vec3D vec3d = RandomPositionGenerator.a(this.entity, 5, 4);

            if (vec3d == null) {
                return false;
            } else {
                this.c = vec3d.a;
                this.d = vec3d.b;
                this.e = vec3d.c;
                return true;
            }
        } else {
            return false;
        }
    }

    public void c() {
        this.entity.getNavigation().a(PositionPathSearchType.TAME, this.c, this.d, this.e, this.b); // CobelPvP
    }

    public boolean b() {
        return !this.entity.getNavigation().g() && this.entity.passenger != null;
    }

    public void e() {
        if (this.entity.aI().nextInt(50) == 0) {
            if (this.entity.passenger instanceof EntityHuman) {
                int i = this.entity.getTemper();
                int j = this.entity.getMaxDomestication();

                // CraftBukkit - fire EntityTameEvent
                if (j > 0 && this.entity.aI().nextInt(j) < i && !org.bukkit.craftbukkit.event.CraftEventFactory.callEntityTameEvent(this.entity, (EntityHuman) this.entity.passenger).isCancelled() && this.entity.passenger instanceof EntityHuman) {
                    this.entity.h((EntityHuman) this.entity.passenger);
                    this.entity.world.broadcastEntityEffect(this.entity, (byte) 7);
                    return;
                }

                this.entity.v(5);
            }

            // CraftBukkit start - Handle dismounting to account for VehicleExitEvent being fired.
            if (this.entity.passenger != null) {
                this.entity.passenger.mount((Entity) null);
                // If the entity still has a passenger, then a plugin cancelled the event.
                if (this.entity.passenger != null) {
                    return;
                }
            }
            // this.entity.passenger = null;
            // CraftBukkit end
            this.entity.cJ();
            this.entity.world.broadcastEntityEffect(this.entity, (byte) 6);
        }
    }
}
