package net.minecraft.server;

public class PacketPlayOutEntityVelocity extends Packet {
    public int a;
    public int b;
    public int c;
    public int d;

    public PacketPlayOutEntityVelocity() {
    }

    public PacketPlayOutEntityVelocity(Entity entity) {
        this(entity.getId(), entity.motX, entity.motY, entity.motZ);
    }

    public PacketPlayOutEntityVelocity(int i, double d0, double d1, double d2) {
        this.a = i;
        double d3 = 3.9D;
        if (d0 < -d3) {
            d0 = -d3;
        }

        if (d1 < -d3) {
            d1 = -d3;
        }

        if (d2 < -d3) {
            d2 = -d3;
        }

        if (d0 > d3) {
            d0 = d3;
        }

        if (d1 > d3) {
            d1 = d3;
        }

        if (d2 > d3) {
            d2 = d3;
        }

        this.b = (int)(d0 * 8000.0D);
        this.c = (int)(d1 * 8000.0D);
        this.d = (int)(d2 * 8000.0D);
    }

    public void a(PacketDataSerializer packetdataserializer) {
        this.a = packetdataserializer.readInt();
        this.b = packetdataserializer.readShort();
        this.c = packetdataserializer.readShort();
        this.d = packetdataserializer.readShort();
    }

    public void b(PacketDataSerializer packetdataserializer) {
        if (packetdataserializer.version < 16) {
            packetdataserializer.writeInt(this.a);
        } else {
            packetdataserializer.b(this.a);
        }

        packetdataserializer.writeShort(this.b);
        packetdataserializer.writeShort(this.c);
        packetdataserializer.writeShort(this.d);
    }

    public void a(PacketPlayOutListener packetplayoutlistener) {
        packetplayoutlistener.a(this);
    }

    public String b() {
        return String.format("id=%d, x=%.2f, y=%.2f, z=%.2f", this.a, (float)this.b / 8000.0F, (float)this.c / 8000.0F, (float)this.d / 8000.0F);
    }

    public void handle(PacketListener packetlistener) {
        this.a((PacketPlayOutListener)packetlistener);
    }
}
