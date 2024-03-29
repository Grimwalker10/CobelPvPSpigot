package net.minecraft.server;

public class PacketPlayInUseEntity extends Packet {
    public int a;
    private EnumEntityUseAction action;

    public PacketPlayInUseEntity() {
    }

    public void a(PacketDataSerializer packetdataserializer) {
        if (packetdataserializer.version < 16) {
            this.a = packetdataserializer.readInt();
            this.action = EnumEntityUseAction.values()[packetdataserializer.readByte() % EnumEntityUseAction.values().length];
        } else {
            this.a = packetdataserializer.a();
            int val = packetdataserializer.a();
            if (val == 2) {
                packetdataserializer.readFloat();
                packetdataserializer.readFloat();
                packetdataserializer.readFloat();
            } else {
                this.action = EnumEntityUseAction.values()[val % EnumEntityUseAction.values().length];
            }
        }

    }

    public void b(PacketDataSerializer packetdataserializer) {
        packetdataserializer.writeInt(this.a);
        packetdataserializer.writeByte(this.action.ordinal());
    }

    public void a(PacketPlayInListener packetplayinlistener) {
        packetplayinlistener.a(this);
    }

    public Entity a(World world) {
        return world.getEntity(this.a);
    }

    public EnumEntityUseAction c() {
        return this.action;
    }

    public void handle(PacketListener packetlistener) {
        this.a((PacketPlayInListener)packetlistener);
    }
}
