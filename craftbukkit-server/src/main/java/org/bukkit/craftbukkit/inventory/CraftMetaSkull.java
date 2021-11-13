package org.bukkit.craftbukkit.inventory;

import java.util.Map;

import net.minecraft.server.GameProfileSerializer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.util.com.mojang.authlib.GameProfile;

import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.inventory.CraftMetaItem.SerializableMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.google.common.collect.ImmutableMap.Builder;

import com.cobelpvp.util.ThreadingManager; // Poweruser

@DelegateDeserialization(SerializableMeta.class)
class CraftMetaSkull extends CraftMetaItem implements SkullMeta {
    static final ItemMetaKey SKULL_OWNER = new ItemMetaKey("SkullOwner", "skull-owner");
    static final int MAX_OWNER_LENGTH = 16;

    private GameProfile profile;

    CraftMetaSkull(CraftMetaItem meta) {
        super(meta);
        if (!(meta instanceof CraftMetaSkull)) {
            return;
        }
        CraftMetaSkull skullMeta = (CraftMetaSkull) meta;
        this.profile = skullMeta.profile;
    }

    CraftMetaSkull(NBTTagCompound tag) {
        super(tag);

        if (tag.hasKeyOfType(SKULL_OWNER.NBT, 10)) {
            profile = GameProfileSerializer.deserialize(tag.getCompound(SKULL_OWNER.NBT));
        } else if (tag.hasKeyOfType(SKULL_OWNER.NBT, 8)) {
            profile = new GameProfile(null, tag.getString(SKULL_OWNER.NBT));
        }
    }

    CraftMetaSkull(Map<String, Object> map) {
        super(map);
        setOwner(SerializableMeta.getString(map, SKULL_OWNER.BUKKIT, true));
    }

    @Override
    void applyToItem(final NBTTagCompound tag) { // Spigot - make final
        super.applyToItem(tag);

        if (hasOwner()) {
            NBTTagCompound owner = new NBTTagCompound();
            GameProfileSerializer.serialize(owner, profile);
            tag.set( SKULL_OWNER.NBT, owner );
            // Spigot start - do an async lookup of the profile. 
            // Unfortunately there is not way to refresh the holding
            // inventory, so that responsibility is left to the user.
            ThreadingManager.queueHeadConversion(new Runnable() // Poweruser
            {
                @Override
                public void run()
                {

                    final GameProfile profile = net.minecraft.server.TileEntitySkull.skinCache.getUnchecked( CraftMetaSkull.this.profile.getName().toLowerCase() );
                    if ( profile != null )
                    {
                        MinecraftServer.getServer().processQueue.add( new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                NBTTagCompound owner = new NBTTagCompound();
                                GameProfileSerializer.serialize( owner, profile );
                                tag.set( SKULL_OWNER.NBT, owner );
                            }
                        } );
                    }
                }
            } );
            // Spigot end
        }
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && isSkullEmpty();
    }

    boolean isSkullEmpty() {
        return !(hasOwner());
    }

    @Override
    boolean applicableTo(Material type) {
        switch(type) {
            case SKULL_ITEM:
                return true;
            default:
                return false;
        }
    }

    @Override
    public CraftMetaSkull clone() {
        return (CraftMetaSkull) super.clone();
    }

    public boolean hasOwner() {
        return profile != null;
    }

    public String getOwner() {
        return hasOwner() ? profile.getName() : null;
    }

    public boolean setOwner(String name) {
        if (name != null && name.length() > MAX_OWNER_LENGTH) {
            return false;
        }

        if (name == null) {
            profile = null;
        } else {
            profile = new GameProfile(null, name);
        }

        return true;
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();
        if (hasOwner()) {
            hash = 61 * hash + profile.hashCode();
        }
        return original != hash ? CraftMetaSkull.class.hashCode() ^ hash : hash;
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaSkull) {
            CraftMetaSkull that = (CraftMetaSkull) meta;

            return (this.hasOwner() ? that.hasOwner() && this.profile.equals(that.profile) : !that.hasOwner());
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaSkull || isSkullEmpty());
    }

    @Override
    Builder<String, Object> serialize(Builder<String, Object> builder) {
        super.serialize(builder);
        if (hasOwner()) {
            return builder.put(SKULL_OWNER.BUKKIT, this.profile.getName());
        }
        return builder;
    }
}
