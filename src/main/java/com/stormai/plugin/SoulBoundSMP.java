package com.stormai.plugin;

import com.stormai.plugin.command.SoulCommand;
import com.stormai.plugin.listener.PlayerListener;
import com.stormai.plugin.menu.SoulMenu;
import com.stormai.plugin.util.SoulManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class SoulBoundSMP extends JavaPlugin {

    private static SoulBoundSMP instance;
    private SoulManager soulManager;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        instance = this;

        // Create or load soul data
        saveDefaultConfig();
        soulManager = new SoulManager(this);

        // Register listeners
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

        // Register commands
        getCommand("souls").setExecutor(new SoulCommand(this));

        // Register ability menu command (/ability)
        getCommand("ability").setExecutor((sender, command, label, args) -> {
            if (sender.hasPermission("soulboundsmp.use")) {
                new SoulMenu(this).open((org.bukkit.entity.Player) sender);
                return true;
            }
            sender.sendMessage("§cYou don't have permission to use this command.");
            return true;
        });

        getLogger().info("SoulBoundSMP enabled!");
    }

    @Override
    public void onDisable() {
        // Save all player soul data
        soulManager.saveAllData();
        getLogger().info("SoulBoundSMP disabled and data saved.");
    }

    public static SoulBoundSMP getInstance() {
        return instance;
    }

    public SoulManager getSoulManager() {
        return soulManager;
    }
}