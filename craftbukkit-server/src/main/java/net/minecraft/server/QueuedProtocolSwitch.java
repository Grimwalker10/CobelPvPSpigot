package net.minecraft.server;

import net.minecraft.util.io.netty.channel.ChannelFuture;
import net.minecraft.util.io.netty.channel.ChannelFutureListener;
import net.minecraft.util.io.netty.channel.ChannelPromise; // CobelPvP
import net.minecraft.util.io.netty.util.concurrent.GenericFutureListener;

public class QueuedProtocolSwitch implements Runnable {
    private final EnumProtocol packetProtocol;
    private final Packet packet;
    private final GenericFutureListener[] genericFutureListeners;
    private final NetworkManager networkManager;

    QueuedProtocolSwitch(NetworkManager e, EnumProtocol a, Packet c, GenericFutureListener[] d) {
        this.networkManager = e;
        this.packetProtocol = a;
        this.packet = c;
        this.genericFutureListeners = d;
    }

    QueuedProtocolSwitch(NetworkManager e, EnumProtocol a, Packet c) {
        this.networkManager = e;
        this.packetProtocol = a;
        this.packet = c;
        this.genericFutureListeners = null;
    }

    public void run() {
        if (this.packetProtocol != this.networkManager.getProtocol()) {
            this.networkManager.setProtocol(this.packetProtocol);
        }

        ChannelFuture future = NetworkManager.getChannel(this.networkManager).writeAndFlush(this.packet);
        this.addGenericFutureListeners(future).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
    }

    private ChannelFuture addGenericFutureListeners(ChannelFuture cf) {
        return this.genericFutureListeners == null ? cf : cf.addListeners(this.genericFutureListeners);
    }
}
