package org.bukkit.command.defaults;

import java.util.List;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.google.common.collect.ImmutableList;

public class SayCommand extends VanillaCommand {
    public SayCommand() {
        super("say");
        this.description = "Broadcasts the given message as the sender";
        this.usageMessage = "/say <message ...>";
        this.setPermission("bukkit.command.say");
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!sender.isOp())
        {
            sender.sendMessage(ChatColor.RED + "No permission.");
            return false;
        }
        if (args.length == 0)  {
            sender.sendMessage(ChatColor.RED + "Usage: " + usageMessage);
            return false;
        }

        StringBuilder message = new StringBuilder();
        message.append(ChatColor.YELLOW).append("[");
        if (sender instanceof ConsoleCommandSender) {
            message.append(ChatColor.DARK_RED + "CONSOLE");
        } else {
            message.append((sender.isOp() ? ChatColor.RED : ChatColor.GREEN) + sender.getName());
        }
        message.append(ChatColor.YELLOW).append("] ");

        if (args.length > 0) {
            message.append(args[0]);
            for (int i = 1; i < args.length; i++) {
                message.append(" ").append(args[i]);
            }
        }

        Bukkit.broadcastMessage(message.toString());
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        Validate.notNull(sender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");

        if (args.length >= 1) {
            return super.tabComplete(sender, alias, args);
        }
        return ImmutableList.of();
    }
}
