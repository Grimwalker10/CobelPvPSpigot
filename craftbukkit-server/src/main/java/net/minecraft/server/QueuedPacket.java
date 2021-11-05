package net.minecraft.server;

import net.minecraft.util.io.netty.util.concurrent.GenericFutureListener;

// CraftBukkit - imported class because the methods are package private
class QueuedPacket {

    private final Packet a;
    private final GenericFutureListener[] b;

    // Poweruser start
    private final NetworkManager manager;

    static NetworkManager getNetworkManager(QueuedPacket queuedpacket) {
        return queuedpacket.manager;
    }

    public QueuedPacket(NetworkManager manager, Packet packet, GenericFutureListener... agenericfuturelistener) {
        this.manager = manager;
    // Poweruser end
        this.a = packet;
        this.b = agenericfuturelistener;
    }

    static Packet a(QueuedPacket queuedpacket) {
        return queuedpacket.a;
    }

    static GenericFutureListener[] b(QueuedPacket queuedpacket) {
        return queuedpacket.b;
    }
}
