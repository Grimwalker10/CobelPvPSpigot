package net.minecraft.server;

// CraftBukkit start
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.player.PlayerTeleportEvent;
// CraftBukkit end

public class EntityEnderPearl extends EntityProjectile {

    private Location lastValidTeleport = null;

    public EntityEnderPearl(World world) {
        super(world);
        this.loadChunks = world.paperSpigotConfig.loadUnloadedEnderPearls; // PaperSpigot
    }

    public EntityEnderPearl(World world, EntityLiving entityliving) {
        super(world, entityliving);
        this.loadChunks = world.paperSpigotConfig.loadUnloadedEnderPearls; // PaperSpigot
    }

    // MineHQ start
    public void h() {
        if (this.world.getCubes(this, this.boundingBox.grow(0.25D, 0.25D, 0.25D)).isEmpty()) {
            this.lastValidTeleport = getBukkitEntity().getLocation();
        }

        super.h();
    }
    // MineHQ start

    protected void a(MovingObjectPosition movingobjectposition) {
        if (movingobjectposition.entity != null) {
            movingobjectposition.entity.damageEntity(DamageSource.projectile(this, this.getShooter()), 0.0F);
        }

        // Poweruser start
        if(this.world.spigotConfig.enderPearlsCanPassNonSolidBlocks && movingobjectposition.type == EnumMovingObjectType.BLOCK) {
            double maxMotionVectorComponent = Math.max(Math.max(Math.abs(this.motX), Math.abs(this.motY)), Math.abs(this.motZ));
            if(maxMotionVectorComponent > 0.001D &&
               !this.world.getType(movingobjectposition.b, movingobjectposition.c, movingobjectposition.d).getMaterial().isSolid()) {
                double factor = 0.20D / maxMotionVectorComponent;
                double shortendMotionX = this.motX * factor;
                double shortendMotionY = this.motY * factor;
                double shortendMotionZ = this.motZ * factor;
                double tempPositionX = movingobjectposition.b + 0.5D;
                double tempPositionY = movingobjectposition.c + 0.5D;
                double tempPositionZ = movingobjectposition.d + 0.5D;
                int nextBlockPositionX;
                int nextBlockPositionY;
                int nextBlockPositionZ;
                do {
                    tempPositionX += shortendMotionX;
                    tempPositionY += shortendMotionY;
                    tempPositionZ += shortendMotionZ;
                    nextBlockPositionX = MathHelper.floor(tempPositionX);
                    nextBlockPositionY = (int)(tempPositionY);
                    nextBlockPositionZ = MathHelper.floor(tempPositionZ);
                } while (nextBlockPositionX == movingobjectposition.b &&
                         nextBlockPositionY == movingobjectposition.c &&
                         nextBlockPositionZ == movingobjectposition.d);
                Block nextBlock = this.world.getType(nextBlockPositionX, nextBlockPositionY, nextBlockPositionZ);
                if(!nextBlock.getMaterial().isSolid()) {
                    return;
                }
            }
        }
        // Poweruser end

        // PaperSpigot start - Remove entities in unloaded chunks
        if (inUnloadedChunk && world.paperSpigotConfig.removeUnloadedEnderPearls) {
            die();
        }
        // PaperSpigot end

        for (int i = 0; i < 32; ++i) {
            this.world.addParticle("portal", this.locX, this.locY + this.random.nextDouble() * 2.0D, this.locZ, this.random.nextGaussian(), 0.0D, this.random.nextGaussian());
        }

        if (!this.world.isStatic) {
            if (this.getShooter() != null && this.getShooter() instanceof EntityPlayer) {
                EntityPlayer entityplayer = (EntityPlayer) this.getShooter();

                if (entityplayer.playerConnection.b().isConnected() && entityplayer.world == this.world && this.lastValidTeleport != null) { // MineHQ
                    // CraftBukkit start - Fire PlayerTeleportEvent
                    org.bukkit.craftbukkit.entity.CraftPlayer player = entityplayer.getBukkitEntity();
                    org.bukkit.Location location = this.lastValidTeleport.clone(); // MineHQ
                    location.setPitch(player.getLocation().getPitch());
                    location.setYaw(player.getLocation().getYaw());

                    PlayerTeleportEvent teleEvent = new PlayerTeleportEvent(player, player.getLocation(), location, PlayerTeleportEvent.TeleportCause.ENDER_PEARL);
                    Bukkit.getPluginManager().callEvent(teleEvent);

                    if (!teleEvent.isCancelled() && !entityplayer.playerConnection.isDisconnected()) {
                        if (this.getShooter().am()) {
                            this.getShooter().mount((Entity) null);
                        }

                        entityplayer.playerConnection.teleport(teleEvent.getTo());
                        this.getShooter().fallDistance = 0.0F;
                        CraftEventFactory.entityDamage = this;
                        this.getShooter().damageEntity(DamageSource.FALL, 5.0F);
                        CraftEventFactory.entityDamage = null;
                    }
                    // CraftBukkit end
                }
            }

            this.die();
        }
    }
}
