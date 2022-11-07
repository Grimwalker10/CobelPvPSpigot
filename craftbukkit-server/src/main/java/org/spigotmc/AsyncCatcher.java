package org.spigotmc;

import net.minecraft.server.MinecraftServer;

public class AsyncCatcher
{

    public static boolean enabled = org.github.paperspigot.PaperSpigotConfig.asyncCatcherFeature;

    public static boolean shuttingDown = false;

    public static void catchOp(String reason)
    {
        if ( enabled && Thread.currentThread() != MinecraftServer.getServer().primaryThread )
        {
            throw new IllegalStateException( "Asynchronous " + reason + "!" );
        }
    }
}
