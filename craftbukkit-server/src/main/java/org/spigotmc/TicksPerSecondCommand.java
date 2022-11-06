package org.spigotmc;

import net.minecraft.server.MinecraftServer;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import java.lang.management.ManagementFactory;

public class TicksPerSecondCommand extends Command
{
    long startTime = System.currentTimeMillis();

    public TicksPerSecondCommand(String name)
    {
        super( name );
        this.description = "Gets the current ticks per second for the server";
        this.usageMessage = "/tps";
        this.setPermission( "" );
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args)
    {
        if (!testPermission(sender)) {
            return true;
        }

        if (sender.hasPermission("spigot.tps")) {
            double[] tps = Bukkit.spigot().getTPS();
            String[] tpsAvg = new String[tps.length];

            for (int i = 0; i < tps.length; i++) {
                tpsAvg[i] = formatAdvancedTps(tps[i]);
            }

            long usedMemory = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 2L / 1048576L;
            long allocatedMemory = Runtime.getRuntime().totalMemory() / 1048576L;

            int entities = MinecraftServer.getServer().entities;
            int activeEntities = MinecraftServer.getServer().activeEntities;
            double activePercent = Math.round(10000.0 * activeEntities / entities) / 100.0;

            sender.sendMessage(ChatColor.GOLD + "TPS from last 1m, 5m, 15m: " + StringUtils.join(tpsAvg, ", "));
            sender.sendMessage(ChatColor.GOLD + "Full tick: " + formatTickTime(MinecraftServer.getServer().lastTickTime) + " ms");
            sender.sendMessage(ChatColor.GOLD + "Memory: " + ChatColor.GREEN + usedMemory + ChatColor.GOLD + "/" + ChatColor.GREEN +allocatedMemory + "MB");
            sender.sendMessage(ChatColor.GOLD + "Active entities: " + ChatColor.GREEN + activeEntities + "/" + entities + " (" + activePercent + "%)");
            sender.sendMessage(ChatColor.GOLD + "Online players: " + ChatColor.GREEN + Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers());
            sender.sendMessage(ChatColor.GOLD + "Alive Threads: " + ChatColor.GREEN + ManagementFactory.getThreadMXBean().getThreadCount() + ChatColor.RED + " - " + ChatColor.GOLD + "Daemon Threads: " + ChatColor.GREEN + ManagementFactory.getThreadMXBean().getDaemonThreadCount());
            sender.sendMessage(ChatColor.GOLD + "Active Workers: " + ChatColor.GREEN + Bukkit.getScheduler().getActiveWorkers().size() + ChatColor.RED + " - " + ChatColor.GOLD + "Pending Tasks: " + ChatColor.GREEN + Bukkit.getScheduler().getPendingTasks().size());
            sender.sendMessage(ChatColor.GOLD + "Threads Interrupted: " + ChatColor.GREEN + Thread.getAllStackTraces().keySet().parallelStream().filter(Thread::isInterrupted).count());
            sender.sendMessage(ChatColor.GOLD + "Uptime: " + ChatColor.YELLOW + formatFullMilis(System.currentTimeMillis() - this.startTime));
        } else {
            double tps = Bukkit.spigot().getTPS()[1];
            StringBuilder tpsBuilder = new StringBuilder();

            tpsBuilder.append(ChatColor.GOLD).append("Server performance: ");
            tpsBuilder.append(formatBasicTps(tps)).append(ChatColor.GOLD).append("/20.0");
            tpsBuilder.append(" [").append(tps > 18.0 ? ChatColor.GREEN : tps > 16.0 ? ChatColor.YELLOW : ChatColor.RED);

            int i = 0;

            for (; i < Math.round(tps); i++) {
                tpsBuilder.append("|");
            }

            tpsBuilder.append(ChatColor.DARK_GRAY);

            for (; i < 20; i++) {
                tpsBuilder.append("|");
            }

            tpsBuilder.append(ChatColor.GOLD).append("]");
            sender.sendMessage(tpsBuilder.toString());
        }

        return true;
    }

    private static String formatTickTime(double time) {
        return (time < 40.0D ? ChatColor.GREEN : time < 60.0D ? ChatColor.YELLOW : ChatColor.RED).toString() + Math.round(time * 10.0D) / 10.0D;
    }

    public static String formatFullMilis(Long millisTime) {

        long seconds = (millisTime / 1000) % 60;
        long minutes = (millisTime / (1000 * 60)) % 60;
        long hours = (millisTime / (1000 * 60 * 60)) % 24;
        long days = (millisTime / (1000 * 60 * 60 * 24)) % 24;
        long weeks = (millisTime / (1000 * 60 * 60 * 24 * 7)) % 7;
        long months = (millisTime / (1000 * 60 * 60 * 24 * 7 * 31)) % 31;
        long years = (millisTime / (1000 * 60 * 60 * 24 * 7 * 31 * 12)) % 12;

        String format = "";
        if (years >= 1.0D) {
            format += years + "y ";
        }
        if (months >= 1.0D) {
            format += months + "mon ";
        }
        if (weeks >= 1.0D) {
            format += weeks + "w ";
        }
        if (days >= 1.0D) {
            format += days + "d ";
        }
        if (hours >= 1.0D) {
            format += hours + "h ";
        }
        if (minutes >= 1.0D) {
            format += minutes + "min ";
        }
        return format + seconds + "s";
    }

    private static String formatAdvancedTps(double tps) {
        return (tps > 18.0 ? ChatColor.GREEN : tps > 16.0 ? ChatColor.YELLOW : ChatColor.RED).toString() + Math.min(Math.round(tps * 100.0D) / 100.0, 20.0);
    }

    private String formatBasicTps(double tps) {
        return (tps > 18.0 ? ChatColor.GREEN : tps > 16.0 ? ChatColor.YELLOW : ChatColor.RED).toString() + Math.min(Math.round(tps * 10.0D) / 10.0D, 20.0D);
    }

}
