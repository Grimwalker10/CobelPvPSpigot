package net.minecraft.server;

public class PacketPlayInKeepAlive extends Packet {
    private int a;

    public PacketPlayInKeepAlive() {
    }

    public void a(PacketPlayInListener packetplayinlistener) {
        packetplayinlistener.a(this);
    }

    public void a(PacketDataSerializer packetdataserializer) {
        if (packetdataserializer.version < 16) {
            this.a = packetdataserializer.readInt();
        } else {
            this.a = packetdataserializer.a();
        }

    }

    public void b(PacketDataSerializer packetdataserializer) {
        packetdataserializer.writeInt(this.a);
    }

    public boolean a() {
        return true;
    }

    public int c() {
        return this.a;
    }

    public void handle(PacketListener packetlistener) {
        this.a((PacketPlayInListener)packetlistener);
    }
}
