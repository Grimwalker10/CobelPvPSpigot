package net.minecraft.server;

import java.util.HashSet;
import java.util.stream.Stream;

import com.cobelpvp.CobelSpigot;
import net.minecraft.optimizations.util.Options;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.entity.CraftEnderPearl;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EnderpearlLandEvent;
import org.bukkit.event.entity.EnderpearlLandEvent.Reason;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Gate;
import org.bukkit.material.Stairs;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

public class EntityEnderPearl extends EntityProjectile {
    private static final BlockFace[] faces;
    private static final ItemStack enderpearl;
    private Location valid;

    public EntityEnderPearl(World world) {
        super(world);
        this.loadChunks = world.paperSpigotConfig.loadUnloadedEnderPearls;
    }

    public EntityEnderPearl(World world, EntityLiving entityliving) {
        super(world, entityliving);
        this.loadChunks = world.paperSpigotConfig.loadUnloadedEnderPearls;
    }

    protected void a(MovingObjectPosition movingobjectposition) {
        Block block = this.world.getType(movingobjectposition.b, movingobjectposition.c, movingobjectposition.d);
        if (!Options.PEARL_STRING.getBooleanValue() || block != Blocks.TRIPWIRE) {
            if (Options.PEARL_GATE.getBooleanValue() && block == Blocks.FENCE_GATE) {
                BlockIterator bi = null;

                try {
                    Vector l = new Vector(this.locX, this.locY, this.locZ);
                    Vector l2 = new Vector(this.locX + this.motX, this.locY + this.motY, this.locZ + this.motZ);
                    Vector dir = (new Vector(l2.getX() - l.getX(), l2.getY() - l.getY(), l2.getZ() - l.getZ())).normalize();
                    bi = new BlockIterator(this.world.getWorld(), l, dir, 0.0D, 1);
                } catch (IllegalStateException var11) {
                }

                if (bi != null) {
                    boolean open = true;
                    boolean hasSolidBlock = false;

                    while(bi.hasNext()) {
                        org.bukkit.block.Block b = bi.next();
                        if (b.getType().isSolid() && b.getType().isOccluding()) {
                            hasSolidBlock = true;
                        }

                        if (b.getState().getData() instanceof Gate && !((Gate)b.getState().getData()).isOpen()) {
                            open = false;
                            break;
                        }
                    }

                    if (open && !hasSolidBlock) {
                        return;
                    }
                }
            } else if (Options.PEARL_COBWEB.getBooleanValue() && block == Blocks.WEB) {
                return;
            }

            if (Options.DAMAGE_PEARLS.getBooleanValue() && movingobjectposition.entity != null) {
                movingobjectposition.entity.damageEntity(DamageSource.projectile(this, this.getShooter()), 0.0F);
            }

            if (this.inUnloadedChunk && this.world.paperSpigotConfig.removeUnloadedEnderPearls) {
                this.die();
            }

            if (Options.PEARL_PARTICLES.getBooleanValue()) {
                for(int i = 0; i < 32; ++i) {
                    this.world.addParticle("portal", this.locX, this.locY + this.random.nextDouble() * 2.0D, this.locZ, this.random.nextGaussian(), 0.0D, this.random.nextGaussian());
                }
            }

            if (!this.world.isStatic) {
                if (this.getShooter() != null && this.getShooter() instanceof EntityPlayer) {
                    EntityPlayer entityplayer = (EntityPlayer)this.getShooter();
                    if (entityplayer.playerConnection.b().isConnected() && entityplayer.world == this.world) {
                        if (this.valid != null) {
                            CraftPlayer player = entityplayer.getBukkitEntity();
                            Reason reason = movingobjectposition.entity != null ? Reason.ENTITY : Reason.BLOCK;
                            CraftEntity bukkitHitEntity = movingobjectposition.entity != null ? movingobjectposition.entity.getBukkitEntity() : null;
                            EnderpearlLandEvent landEvent = new EnderpearlLandEvent((CraftEnderPearl)this.getBukkitEntity(), reason, bukkitHitEntity);
                            Bukkit.getPluginManager().callEvent(landEvent);
                            if (landEvent.isCancelled()) {
                                this.die();
                                return;
                            }

                            Location location = this.getBukkitEntity().getLocation();
                            location.setPitch(player.getLocation().getPitch());
                            location.setYaw(player.getLocation().getYaw());
                            if (!this.tali(location, player)) {
                                this.die();
                                this.refund(player);
                                return;
                            }

                            if (!this.taliNotPassable(location, player)) {
                                this.die();
                                this.refund(player);
                                return;
                            }

                            if (Options.PEARL_ANTI_GLITCH.getBooleanValue()) {
                                label211: {
                                    if (location.getBlock().getType() != Material.WEB && location.getBlock().getRelative(BlockFace.UP).getType() != Material.WEB) {
                                        if (location.getBlock().getType() != Material.TRIPWIRE && location.getBlock().getRelative(BlockFace.UP).getType() != Material.TRIPWIRE) {
                                            this.badTeleport(location);
                                            break label211;
                                        }

                                        this.die();
                                        this.refund(player);
                                        return;
                                    }

                                    this.die();
                                    this.refund(player);
                                    return;
                                }
                            }

                            PlayerTeleportEvent teleEvent = new PlayerTeleportEvent(player, player.getLocation(), location, PlayerTeleportEvent.TeleportCause.ENDER_PEARL);
                            Bukkit.getPluginManager().callEvent(teleEvent);
                            if (!teleEvent.isCancelled() && !entityplayer.playerConnection.isDisconnected()) {
                                if (this.getShooter().am()) {
                                    this.getShooter().mount((Entity)null);
                                }

                                entityplayer.playerConnection.teleport(teleEvent.getTo());
                                this.getShooter().fallDistance = 0.0F;
                                if (Options.DAMAGE_PEARLS.getBooleanValue()) {
                                    CraftEventFactory.entityDamage = this;
                                    this.getShooter().damageEntity(DamageSource.FALL, 5.0F);
                                    CraftEventFactory.entityDamage = null;
                                }
                            }
                        } else if (Options.PEARL_ANTI_GLITCH.getBooleanValue()) {
                            this.die();

                            entityplayer.getBukkitEntity().getInventory().addItem(new ItemStack[]{enderpearl});
                            entityplayer.getBukkitEntity().updateInventory();
                            return;
                        }
                    }
                }

                this.die();
            }

        }
    }

    private void refund(Player player) {
        player.getInventory().addItem(new ItemStack[]{enderpearl});
        player.updateInventory();
    }

    private Location badTeleport(final Location location) {
        final org.bukkit.block.Block block = location.getBlock();
        if (block.getType() == Material.AIR) {
            final org.bukkit.block.Block up = block.getRelative(BlockFace.UP);
            final org.bukkit.block.Block down = block.getRelative(BlockFace.DOWN);
            if (down.getType() == Material.AIR) {
                if (up.getType() != Material.AIR) {
                    location.setY(location.getBlockY() - 1.0);
                }
                else if (up.getRelative(BlockFace.UP).getType() != Material.AIR) {
                    if (location.getBlockY() < 5) {
                        return location;
                    }
                    location.setY(location.getBlockY() - 2.0);
                }
            }
            else if (up.getRelative(BlockFace.UP).getType() != Material.AIR) {
                location.setY(location.getBlockY());
            }
        }
        final boolean check = Stream.of(EntityEnderPearl.faces).filter(face -> block.getRelative(face).getType() != Material.AIR).findAny().orElse(null) != null;
        if (check) {
            location.setX(location.getBlockX() + 0.5);
            location.setZ(location.getBlockZ() + 0.5);
        }
        return location;
    }

    private boolean tali(Location location, Player player) {
        org.bukkit.block.Block current = location.getBlock();
        org.bukkit.block.Block above = current.getRelative(BlockFace.UP);
        org.bukkit.block.Block down = current.getRelative(BlockFace.DOWN);
        org.bukkit.block.Block west = current.getRelative(BlockFace.WEST);
        org.bukkit.block.Block east = current.getRelative(BlockFace.EAST);
        org.bukkit.block.Block north = current.getRelative(BlockFace.NORTH);
        org.bukkit.block.Block south = current.getRelative(BlockFace.SOUTH);
        Material westType = west.getRelative(BlockFace.UP).getType();
        Material eastType = east.getRelative(BlockFace.UP).getType();
        Material northType = north.getRelative(BlockFace.UP).getType();
        Material southType = south.getRelative(BlockFace.UP).getType();
        boolean stairCurrent = false;
        boolean stairUp = false;
        boolean crittable = Options.PEARL_CRITICAL_BLOCK.getBooleanValue();
        if (!(current.getLocation().distance(player.getLocation()) > 4.0D) && (this.isSlab(current.getType()) && Options.PEARL_SLABS.getBooleanValue() || (stairCurrent = this.isStair(current.getType())) && Options.PEARL_STAIRS.getBooleanValue() || this.isSlab(current.getType()) && Options.PEARL_SLABS.getBooleanValue() || (stairUp = this.isStair(current.getType())) && Options.PEARL_STAIRS.getBooleanValue() || current.getType() == Material.COBBLE_WALL && Options.PEARL_COBBLEWALL.getBooleanValue() || current.getType() == Material.BED_BLOCK && Options.PEARL_BED.getBooleanValue() || (current.getType() == Material.PISTON_EXTENSION || current.getType() == Material.PISTON_BASE) && Options.PEARL_PISTON.getBooleanValue())) {
            BlockFace blockFace = null;
            if (stairCurrent || stairUp) {
                Stairs stairs = (Stairs)(stairCurrent ? current : current).getState().getData();
                blockFace = stairs.getFacing();
            }

            if (this.getDirectionName(player.getLocation()).contains("E")) {
                if ((stairCurrent || stairCurrent) && blockFace != null && blockFace != BlockFace.NORTH && blockFace != BlockFace.SOUTH) {
                    this.die();
                    return false;
                }

                if (eastType.isSolid()) {
                    if (!crittable) {
                        return false;
                    }

                    location.setY(east.getLocation().getY() - 1.0D);
                }

                location.setX(location.getX() + 1.0D);
            } else if (this.getDirectionName(player.getLocation()).contains("W")) {
                if ((stairCurrent || stairCurrent) && blockFace != null && blockFace != BlockFace.NORTH && blockFace != BlockFace.SOUTH) {
                    this.die();
                    return false;
                }

                if (westType.isSolid()) {
                    if (!crittable) {
                        return false;
                    }

                    location.setY(west.getLocation().getY() - 1.0D);
                }

                location.setX(location.getX() - 1.0D);
            } else if (this.getDirectionName(player.getLocation()).contains("N")) {
                if ((stairCurrent || stairCurrent) && blockFace != null && blockFace != BlockFace.EAST && blockFace != BlockFace.WEST) {
                    this.die();
                    return false;
                }

                if (northType.isSolid()) {
                    if (!crittable) {
                        return false;
                    }

                    location.setY(north.getLocation().getY() - 1.0D);
                }

                location.setZ(location.getZ() - 1.0D);
            } else if (this.getDirectionName(player.getLocation()).contains("S")) {
                if ((stairCurrent || stairCurrent) && blockFace != null && blockFace != BlockFace.EAST && blockFace != BlockFace.WEST) {
                    this.die();
                    return false;
                }

                if (southType.isSolid()) {
                    if (!crittable) {
                        return false;
                    }

                    location.setY(south.getLocation().getY() - 1.0D);
                }

                location.setZ(location.getZ() + 1.0D);
            }
        }

        return true;
    }

    private boolean taliNotPassable(Location location, Player player) {
        org.bukkit.block.Block current = location.getBlock();
        org.bukkit.block.Block west = current.getRelative(BlockFace.WEST);
        org.bukkit.block.Block east = current.getRelative(BlockFace.EAST);
        org.bukkit.block.Block north = current.getRelative(BlockFace.NORTH);
        org.bukkit.block.Block south = current.getRelative(BlockFace.SOUTH);
        Material westType = west.getRelative(BlockFace.UP).getType();
        Material eastType = east.getRelative(BlockFace.UP).getType();
        Material northType = north.getRelative(BlockFace.UP).getType();
        Material southType = south.getRelative(BlockFace.UP).getType();
        boolean crittable = Options.PEARL_CRITICAL_BLOCK.getBooleanValue();
        if (!(current.getLocation().distance(player.getLocation()) > 4.0D) && Options.PEARL_ENDPORTAL.getBooleanValue()) {
            org.bukkit.block.Block targetBlock = player.getTargetBlock((HashSet)null, 2);
            if (this.getDirectionName(player.getLocation()).contains("E") && targetBlock.getType() == Material.ENDER_PORTAL_FRAME) {
                if (eastType.isSolid()) {
                    if (!crittable) {
                        return false;
                    }

                    location.setY(east.getLocation().getY() - 1.0D);
                }

                location.setX((double)(targetBlock.getX() + 1));
            } else if (this.getDirectionName(player.getLocation()).contains("W") && targetBlock.getType() == Material.ENDER_PORTAL_FRAME) {
                if (westType.isSolid()) {
                    if (!crittable) {
                        return false;
                    }

                    location.setY(west.getLocation().getY() - 1.0D);
                }

                location.setX((double)(targetBlock.getX() - 1));
            } else if (this.getDirectionName(player.getLocation()).contains("N") && targetBlock.getType() == Material.ENDER_PORTAL_FRAME) {
                if (northType.isSolid()) {
                    if (!crittable) {
                        return false;
                    }

                    location.setY(north.getLocation().getY() - 1.0D);
                }

                location.setZ((double)(targetBlock.getZ() - 1));
            } else if (this.getDirectionName(player.getLocation()).contains("S") && targetBlock.getType() == Material.ENDER_PORTAL_FRAME) {
                if (southType.isSolid()) {
                    if (!crittable) {
                        return false;
                    }

                    location.setY(south.getLocation().getY() - 1.0D);
                }

                location.setZ((double)(targetBlock.getZ() + 1));
            }
        }

        return true;
    }

    private boolean isSlab(Material material) {
        return material.toString().contains("STEP");
    }

    private boolean isStair(Material material) {
        return material.toString().contains("STAIRS");
    }

    private String getDirectionName(Location location) {
        double rotation = (double)((location.getYaw() - 90.0F) % 360.0F);
        if (rotation < 0.0D) {
            rotation += 360.0D;
        }

        if (0.0D <= rotation && rotation < 22.5D) {
            return "W";
        } else if (22.5D <= rotation && rotation < 67.5D) {
            return "NW";
        } else if (67.5D <= rotation && rotation < 112.5D) {
            return "N";
        } else if (112.5D <= rotation && rotation < 157.5D) {
            return "NE";
        } else if (157.5D <= rotation && rotation < 202.5D) {
            return "E";
        } else if (202.5D <= rotation && rotation < 247.5D) {
            return "SE";
        } else if (247.5D <= rotation && rotation < 292.5D) {
            return "S";
        } else if (292.5D <= rotation && rotation < 337.5D) {
            return "SW";
        } else {
            return 337.5D <= rotation && rotation < 360.0D ? "W" : null;
        }
    }

    public void h() {
        EntityLiving shooter = this.shooter;
        if (shooter != null && !shooter.isAlive()) {
            this.die();
        } else {
            Location location = this.getBukkitEntity().getLocation();
            org.bukkit.block.Block block = location.getBlock();
            if (block.isEmpty()) {
                this.valid = location;
            }

            if (block.getType().toString().contains("STAIRS") || block.getType().toString().contains("STEP")) {
                this.valid = location;
            }

            if (block.getType() == Material.COBBLE_WALL) {
                this.valid = location;
            }

            if (block.getType() == Material.BED_BLOCK) {
                this.valid = location;
            }

            if (block.getType() == Material.FENCE_GATE && ((Gate)block.getState().getData()).isOpen()) {
                this.valid = location;
            }

            super.h();
        }
    }

    static {
        faces = new BlockFace[]{BlockFace.SOUTH, BlockFace.NORTH, BlockFace.EAST, BlockFace.WEST, BlockFace.SELF};
        enderpearl = new ItemStack(Material.ENDER_PEARL);
    }
}
