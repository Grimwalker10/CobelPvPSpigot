package net.minecraft.server;

import net.minecraft.util.io.netty.util.concurrent.GenericFutureListener;

class QueuedPacket {
    private final Packet packet;
    private final GenericFutureListener[] listeners;
    private final boolean hasListeners;

    public QueuedPacket(Packet packet, GenericFutureListener... agenericfuturelistener) {
        this.packet = packet;
        this.listeners = agenericfuturelistener;
        this.hasListeners = true;
    }

    public QueuedPacket(Packet packet) {
        this.packet = packet;
        this.listeners = null;
        this.hasListeners = false;
    }

    public Packet getPacket() {
        return this.packet;
    }

    public GenericFutureListener[] getListeners() {
        return this.listeners;
    }
}
