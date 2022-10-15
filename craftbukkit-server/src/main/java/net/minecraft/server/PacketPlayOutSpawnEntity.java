package net.minecraft.server;

public class PacketPlayOutSpawnEntity extends Packet {
    public int a;
    public int b;
    public int c;
    public int d;
    public int e;
    public int f;
    public int g;
    public int h;
    public int i;
    public int j;
    public int k;

    public PacketPlayOutSpawnEntity() {
    }

    public PacketPlayOutSpawnEntity(Entity entity, int i) {
        this(entity, i, 0);
    }

    public PacketPlayOutSpawnEntity(Entity entity, int i, int j) {
        this.a = entity.getId();
        this.b = MathHelper.floor(entity.locX * 32.0D);
        this.c = MathHelper.floor(entity.locY * 32.0D);
        this.d = MathHelper.floor(entity.locZ * 32.0D);
        this.h = MathHelper.d(entity.pitch * 256.0F / 360.0F);
        this.i = MathHelper.d(entity.yaw * 256.0F / 360.0F);
        this.j = i;
        this.k = j;
        if (j > 0) {
            double d0 = entity.motX;
            double d1 = entity.motY;
            double d2 = entity.motZ;
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

            this.e = (int)(d0 * 8000.0D);
            this.f = (int)(d1 * 8000.0D);
            this.g = (int)(d2 * 8000.0D);
        }

    }

    public void a(PacketDataSerializer packetdataserializer) {
        this.a = packetdataserializer.a();
        this.j = packetdataserializer.readByte();
        this.b = packetdataserializer.readInt();
        this.c = packetdataserializer.readInt();
        this.d = packetdataserializer.readInt();
        this.h = packetdataserializer.readByte();
        this.i = packetdataserializer.readByte();
        this.k = packetdataserializer.readInt();
        if (this.k > 0) {
            this.e = packetdataserializer.readShort();
            this.f = packetdataserializer.readShort();
            this.g = packetdataserializer.readShort();
        }

    }

    public void b(PacketDataSerializer packetdataserializer) {
        packetdataserializer.b(this.a);
        packetdataserializer.writeByte(this.j);
        if (this.j == 71 && packetdataserializer.version >= 28) {
            switch(this.k) {
                case 0:
                    this.d += 32;
                    this.i = 0;
                    break;
                case 1:
                    this.b -= 32;
                    this.i = 64;
                    break;
                case 2:
                    this.d -= 32;
                    this.i = 128;
                    break;
                case 3:
                    this.b += 32;
                    this.i = 192;
            }
        }

        if (this.j == 70 && packetdataserializer.version >= 36) {
            int id = this.k & '\uffff';
            int data = this.k >> 16;
            this.k = id | data << 12;
        }

        if ((this.j == 50 || this.j == 70 || this.j == 74) && packetdataserializer.version >= 16) {
            this.c -= 16;
        }

        packetdataserializer.writeInt(this.b);
        packetdataserializer.writeInt(this.c);
        packetdataserializer.writeInt(this.d);
        packetdataserializer.writeByte(this.h);
        packetdataserializer.writeByte(this.i);
        packetdataserializer.writeInt(this.k);
        if (this.k > 0) {
            packetdataserializer.writeShort(this.e);
            packetdataserializer.writeShort(this.f);
            packetdataserializer.writeShort(this.g);
        }

    }

    public void a(PacketPlayOutListener packetplayoutlistener) {
        packetplayoutlistener.a(this);
    }

    public String b() {
        return String.format("id=%d, type=%d, x=%.2f, y=%.2f, z=%.2f", this.a, this.j, (float)this.b / 32.0F, (float)this.c / 32.0F, (float)this.d / 32.0F);
    }

    public void a(int i) {
        this.b = i;
    }

    public void b(int i) {
        this.c = i;
    }

    public void c(int i) {
        this.d = i;
    }

    public void d(int i) {
        this.e = i;
    }

    public void e(int i) {
        this.f = i;
    }

    public void f(int i) {
        this.g = i;
    }

    public void handle(PacketListener packetlistener) {
        this.a((PacketPlayOutListener)packetlistener);
    }
}
