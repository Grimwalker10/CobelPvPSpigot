package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;
// CraftBukkit end

public class EntityWolf extends EntityTameableAnimal {

    private float bq;
    private float br;
    private boolean bs;
    private boolean bt;
    private float bu;
    private float bv;

    public EntityWolf(World world) {
        super(world);
        this.a(0.6F, 0.8F);
        this.getNavigation().a(true);
        this.goalSelector.a(1, new PathfinderGoalFloat(this));
        this.goalSelector.a(2, this.bp);
        this.goalSelector.a(3, new PathfinderGoalLeapAtTarget(this, 0.4F));
        this.goalSelector.a(4, new PathfinderGoalMeleeAttack(this, 1.0D, true));
        this.goalSelector.a(5, new PathfinderGoalFollowOwner(this, 1.0D, 10.0F, 2.0F));
        this.goalSelector.a(6, new PathfinderGoalBreed(this, 1.0D));
        this.goalSelector.a(7, new PathfinderGoalRandomStroll(this, 1.0D));
        this.goalSelector.a(8, new PathfinderGoalBeg(this, 8.0F));
        this.goalSelector.a(9, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
        this.goalSelector.a(9, new PathfinderGoalRandomLookaround(this));
        this.targetSelector.a(1, new PathfinderGoalOwnerHurtByTarget(this));
        this.targetSelector.a(2, new PathfinderGoalOwnerHurtTarget(this));
        this.targetSelector.a(3, new PathfinderGoalHurtByTarget(this, true));
        this.targetSelector.a(4, new PathfinderGoalRandomTargetNonTamed(this, EntitySheep.class, 200, false));
        this.setTamed(false);
    }

    protected void aD() {
        super.aD();
        this.getAttributeInstance(GenericAttributes.d).setValue(0.30000001192092896D);
        if (this.isTamed()) {
            this.getAttributeInstance(GenericAttributes.maxHealth).setValue(20.0D);
        } else {
            this.getAttributeInstance(GenericAttributes.maxHealth).setValue(8.0D);
        }
    }

    public boolean bk() {
        return true;
    }

    public void setGoalTarget(EntityLiving entityliving) {
        super.setGoalTarget(entityliving);
        if (entityliving == null) {
            this.setAngry(false);
        } else if (!this.isTamed()) {
            this.setAngry(true);
        }
    }

    protected void bp() {
        this.datawatcher.watch(18, Float.valueOf(this.getHealth()));
    }

    protected void c() {
        super.c();
        this.datawatcher.a(18, new Float(this.getHealth()));
        this.datawatcher.a(19, new Byte((byte) 0));
        this.datawatcher.a(20, new Byte((byte) BlockCloth.b(1)));
    }

    protected void a(int i, int j, int k, Block block) {
        this.makeSound("mob.wolf.step", 0.15F, 1.0F);
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setBoolean("Angry", this.isAngry());
        nbttagcompound.setByte("CollarColor", (byte) this.getCollarColor());
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        this.setAngry(nbttagcompound.getBoolean("Angry"));
        if (nbttagcompound.hasKeyOfType("CollarColor", 99)) {
            this.setCollarColor(nbttagcompound.getByte("CollarColor"));
        }
    }

    protected String t() {
        // CraftBukkit - (getFloat(18) < 10) -> (getFloat(18) < this.getMaxHealth() / 2)
        return this.isAngry() ? "mob.wolf.growl" : (this.random.nextInt(3) == 0 ? (this.isTamed() && this.datawatcher.getFloat(18) < (this.getMaxHealth() / 2) ? "mob.wolf.whine" : "mob.wolf.panting") : "mob.wolf.bark");
    }

    protected String aT() {
        return "mob.wolf.hurt";
    }

    protected String aU() {
        return "mob.wolf.death";
    }

    protected float bf() {
        return 0.4F;
    }

    protected Item getLoot() {
        return Item.getById(-1);
    }

    public void e() {
        super.e();
        if (!this.world.isStatic && this.bs && !this.bt && !this.bS() && this.onGround && !this.isSearchingForAPath()) { // CobelPvP
            this.bt = true;
            this.bu = 0.0F;
            this.bv = 0.0F;
            this.world.broadcastEntityEffect(this, (byte) 8);
        }
    }

    public void h() {
        super.h();
        this.br = this.bq;
        if (this.ck()) {
            this.bq += (1.0F - this.bq) * 0.4F;
        } else {
            this.bq += (0.0F - this.bq) * 0.4F;
        }

        if (this.ck()) {
            this.g = 10;
        }

        if (this.L()) {
            this.bs = true;
            this.bt = false;
            this.bu = 0.0F;
            this.bv = 0.0F;
        } else if ((this.bs || this.bt) && this.bt) {
            if (this.bu == 0.0F) {
                this.makeSound("mob.wolf.shake", this.bf(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            }

            this.bv = this.bu;
            this.bu += 0.05F;
            if (this.bv >= 2.0F) {
                this.bs = false;
                this.bt = false;
                this.bv = 0.0F;
                this.bu = 0.0F;
            }

            if (this.bu > 0.4F) {
                float f = (float) this.boundingBox.b;
                int i = (int) (MathHelper.sin((this.bu - 0.4F) * 3.1415927F) * 7.0F);

                for (int j = 0; j < i; ++j) {
                    float f1 = (this.random.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;
                    float f2 = (this.random.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;

                    this.world.addParticle("splash", this.locX + (double) f1, (double) (f + 0.8F), this.locZ + (double) f2, this.motX, this.motY, this.motZ);
                }
            }
        }
    }

    public float getHeadHeight() {
        return this.length * 0.8F;
    }

    public int x() {
        return this.isSitting() ? 20 : super.x();
    }

    public boolean damageEntity(DamageSource damagesource, float f) {
        if (this.isInvulnerable()) {
            return false;
        } else {
            Entity entity = damagesource.getEntity();

            this.bp.setSitting(false);
            if (entity != null && !(entity instanceof EntityHuman) && !(entity instanceof EntityArrow)) {
                f = (f + 1.0F) / 2.0F;
            }

            return super.damageEntity(damagesource, f);
        }
    }

    public boolean n(Entity entity) {
        int i = this.isTamed() ? 4 : 2;

        return entity.damageEntity(DamageSource.mobAttack(this), (float) i);
    }

    public void setTamed(boolean flag) {
        super.setTamed(flag);
        if (flag) {
            this.getAttributeInstance(GenericAttributes.maxHealth).setValue(20.0D);
        } else {
            this.getAttributeInstance(GenericAttributes.maxHealth).setValue(8.0D);
        }
    }

    public boolean a(EntityHuman entityhuman) {
        ItemStack itemstack = entityhuman.inventory.getItemInHand();

        if (this.isTamed()) {
            if (itemstack != null) {
                if (itemstack.getItem() instanceof ItemFood) {
                    ItemFood itemfood = (ItemFood) itemstack.getItem();

                    if (itemfood.i() && this.datawatcher.getFloat(18) < 20.0F) {
                        if (!entityhuman.abilities.canInstantlyBuild) {
                            --itemstack.count;
                        }

                        this.heal((float) itemfood.getNutrition(itemstack), org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason.EATING); // CraftBukkit
                        if (itemstack.count <= 0) {
                            entityhuman.inventory.setItem(entityhuman.inventory.itemInHandIndex, (ItemStack) null);
                        }

                        return true;
                    }
                } else if (itemstack.getItem() == Items.INK_SACK) {
                    int i = BlockCloth.b(itemstack.getData());

                    if (i != this.getCollarColor()) {
                        this.setCollarColor(i);
                        if (!entityhuman.abilities.canInstantlyBuild && --itemstack.count <= 0) {
                            entityhuman.inventory.setItem(entityhuman.inventory.itemInHandIndex, (ItemStack) null);
                        }

                        return true;
                    }
                }
            }

            if (this.e(entityhuman) && !this.world.isStatic && !this.c(itemstack)) {
                this.bp.setSitting(!this.isSitting());
                this.bc = false;
                this.setPathEntity((PathEntity) null);
                this.setTarget((Entity) null);
                // CraftBukkit start
                if (this.getGoalTarget() != null) {
                    CraftEventFactory.callEntityTargetEvent(this, null, TargetReason.FORGOT_TARGET);
                }
                // CraftBukkit end
                this.setGoalTarget((EntityLiving) null);
            }
        } else if (itemstack != null && itemstack.getItem() == Items.BONE && !this.isAngry()) {
            if (!entityhuman.abilities.canInstantlyBuild) {
                --itemstack.count;
            }

            if (itemstack.count <= 0) {
                entityhuman.inventory.setItem(entityhuman.inventory.itemInHandIndex, (ItemStack) null);
            }

            if (!this.world.isStatic) {
                // CraftBukkit - added event call and isCancelled check.
                if (this.random.nextInt(3) == 0 && !CraftEventFactory.callEntityTameEvent(this, entityhuman).isCancelled()) {
                    this.setTamed(true);
                    this.setPathEntity((PathEntity) null);
                    // CraftBukkit start
                    if (this.getGoalTarget() != null) {
                        CraftEventFactory.callEntityTargetEvent(this, null, TargetReason.FORGOT_TARGET);
                    }
                    // CraftBukkit end
                    this.setGoalTarget((EntityLiving) null);
                    this.bp.setSitting(true);
                    this.setHealth(this.getMaxHealth()); // CraftBukkit - 20.0 -> getMaxHealth()
                    this.setOwnerUUID(entityhuman.getUniqueID().toString());
                    this.i(true);
                    this.world.broadcastEntityEffect(this, (byte) 7);
                } else {
                    this.i(false);
                    this.world.broadcastEntityEffect(this, (byte) 6);
                }
            }

            return true;
        }

        return super.a(entityhuman);
    }

    public boolean c(ItemStack itemstack) {
        return itemstack == null ? false : (!(itemstack.getItem() instanceof ItemFood) ? false : ((ItemFood) itemstack.getItem()).i());
    }

    public int bB() {
        return 8;
    }

    public boolean isAngry() {
        return (this.datawatcher.getByte(16) & 2) != 0;
    }

    public void setAngry(boolean flag) {
        byte b0 = this.datawatcher.getByte(16);

        if (flag) {
            this.datawatcher.watch(16, Byte.valueOf((byte) (b0 | 2)));
        } else {
            this.datawatcher.watch(16, Byte.valueOf((byte) (b0 & -3)));
        }
    }

    public int getCollarColor() {
        return this.datawatcher.getByte(20) & 15;
    }

    public void setCollarColor(int i) {
        this.datawatcher.watch(20, Byte.valueOf((byte) (i & 15)));
    }

    public EntityWolf b(EntityAgeable entityageable) {
        EntityWolf entitywolf = new EntityWolf(this.world);
        String s = this.getOwnerUUID();

        if (s != null && s.trim().length() > 0) {
            entitywolf.setOwnerUUID(s);
            entitywolf.setTamed(true);
        }

        return entitywolf;
    }

    public void m(boolean flag) {
        if (flag) {
            this.datawatcher.watch(19, Byte.valueOf((byte) 1));
        } else {
            this.datawatcher.watch(19, Byte.valueOf((byte) 0));
        }
    }

    public boolean mate(EntityAnimal entityanimal) {
        if (entityanimal == this) {
            return false;
        } else if (!this.isTamed()) {
            return false;
        } else if (!(entityanimal instanceof EntityWolf)) {
            return false;
        } else {
            EntityWolf entitywolf = (EntityWolf) entityanimal;

            return !entitywolf.isTamed() ? false : (entitywolf.isSitting() ? false : this.ce() && entitywolf.ce());
        }
    }

    public boolean ck() {
        return this.datawatcher.getByte(19) == 1;
    }

    protected boolean isTypeNotPersistent() {
        return !this.isTamed() /*&& this.ticksLived > 2400*/; // CraftBukkit
    }

    public boolean a(EntityLiving entityliving, EntityLiving entityliving1) {
        if (!(entityliving instanceof EntityCreeper) && !(entityliving instanceof EntityGhast)) {
            if (entityliving instanceof EntityWolf) {
                EntityWolf entitywolf = (EntityWolf) entityliving;

                if (entitywolf.isTamed() && entitywolf.getOwner() == entityliving1) {
                    return false;
                }
            }

            return entityliving instanceof EntityHuman && entityliving1 instanceof EntityHuman && !((EntityHuman) entityliving1).a((EntityHuman) entityliving) ? false : !(entityliving instanceof EntityHorse) || !((EntityHorse) entityliving).isTame();
        } else {
            return false;
        }
    }

    public EntityAgeable createChild(EntityAgeable entityageable) {
        return this.b(entityageable);
    }
}
