package org.spigotmc;

import net.minecraft.server.MinecraftServer;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.text.DecimalFormat;

public class TicksPerSecondCommand extends Command
{
    long startTime = System.currentTimeMillis();
    public static final DecimalFormat df = new DecimalFormat("#.#");

    public TicksPerSecondCommand(String name)
    {
        super( name );
        this.description = "Gets the current ticks per second for the server";
        this.usageMessage = "/tps";
        this.setPermission( "bukkit.command.tps" );
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args)
    {
        if (!sender.isOp()) {
            sender.sendMessage(ChatColor.RED + "No permission.");
            return true;
        }

        if (sender.hasPermission("bukkit.command.tps.advanced")) {
            double[] tps = Bukkit.spigot().getTPS();
            Runtime runtime = Runtime.getRuntime();
            double usedMemory = (double)(runtime.totalMemory() - runtime.freeMemory());
            double freeMemory = (double)runtime.maxMemory() - usedMemory;
            String[] tpsAvg = new String[tps.length];

            for (int i = 0; i < tps.length; i++) {
                tpsAvg[i] = formatAdvancedTps(tps[i]);
            }

            int entities = MinecraftServer.getServer().entities;
            int activeEntities = MinecraftServer.getServer().activeEntities;
            double activePercent = Math.round(10000.0 * activeEntities / entities) / 100.0;

            sender.sendMessage(ChatColor.GOLD + "TPS from last 1m, 5m, 15m: " + StringUtils.join(tpsAvg, ", "));
            sender.sendMessage(ChatColor.GOLD + "Full tick: " + formatTickTime(MinecraftServer.getServer().lastTickTime) + " ms");
            sender.sendMessage(ChatColor.GOLD + "Memory: " + formatMem(usedMemory) + "§7/" + formatMem((double)runtime.maxMemory()) + " §8(§c" + formatMem(freeMemory) + " free§8)");
            sender.sendMessage(ChatColor.GOLD + "Active entities: " + ChatColor.GREEN + activeEntities + "/" + entities + " (" + activePercent + "%)");
            sender.sendMessage(ChatColor.GOLD + "Online players: " + ChatColor.GREEN + Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers());
            sender.sendMessage(ChatColor.GOLD + "Uptime: §a" + formatFullMilis(System.currentTimeMillis() - this.startTime));
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

    private static String formatMem(double mem) {
        return "§a" + Math.round(mem / 1024.0D / 1024.0D) + "MB";
    }

    private static String formatAdvancedTps(double tps) {
        return (tps > 18.0 ? ChatColor.GREEN : tps > 16.0 ? ChatColor.YELLOW : ChatColor.RED).toString() + Math.min(Math.round(tps * 100.0D) / 100.0, 20.0);
    }

    private String formatBasicTps(double tps) {
        return (tps > 18.0 ? ChatColor.GREEN : tps > 16.0 ? ChatColor.YELLOW : ChatColor.RED).toString() + Math.min(Math.round(tps * 10.0D) / 10.0D, 20.0D);
    }

    public static String formatFullMilis(Long milis) {
        double seconds = (double)Math.max(0L, milis) / 1000.0D;
        double minutes = seconds / 60.0D;
        double hours = minutes / 60.0D;
        double days = hours / 24.0D;
        double weeks = days / 7.0D;
        double months = days / 31.0D;
        double years = months / 12.0D;
        if (years >= 1.0D) {
            return df.format(years) + " year" + (years != 1.0D ? "s" : "");
        } else if (months >= 1.0D) {
            return df.format(months) + " month" + (months != 1.0D ? "s" : "");
        } else if (weeks >= 1.0D) {
            return df.format(weeks) + " week" + (weeks != 1.0D ? "s" : "");
        } else if (days >= 1.0D) {
            return df.format(days) + " day" + (days != 1.0D ? "s" : "");
        } else if (hours >= 1.0D) {
            return df.format(hours) + " hour" + (hours != 1.0D ? "s" : "");
        } else {
            return minutes >= 1.0D ? df.format(minutes) + " minute" + (minutes != 1.0D ? "s" : "") : df.format(seconds) + " second" + (seconds != 1.0D ? "s" : "");
        }
    }
}
