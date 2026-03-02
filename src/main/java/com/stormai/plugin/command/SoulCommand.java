package com.stormai.plugin.command;

import com.stormai.plugin.SoulBoundSMP;
import com.stormai.plugin.util.SoulManager;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class SoulCommand implements CommandExecutor {

    private final SoulBoundSMP plugin;

    public SoulCommand(SoulBoundSMP plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("soulboundsmp.view")) {
            sender.sendMessage("§cYou don't have permission to view your souls.");
            return true;
        }

        if (sender instanceof Player p) {
            int count = plugin.getSoulManager().getSoulCount(p);
            p.sendMessage(ChatColor.GOLD + "You have " + count + " Soul" + (count == 1 ? "" : "s") + ".");
        } else {
            sender.sendMessage("§cOnly players can view soul count.");
        }
        return true;
    }
}