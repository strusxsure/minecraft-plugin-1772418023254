package com.stormai.plugin.util;

import com.stormai.plugin.SoulBoundSMP;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SoulManager {

    private final Plugin plugin;
    private final File dataFolder;
    private final Map<UUID, Integer> soulCounts = new HashMap<>();

    public SoulManager(SoulBoundSMP plugin) {
        this.plugin = plugin;
        this.dataFolder = new File(plugin.getDataFolder(), "souls.yml");
        if (!dataFolder.exists()) {
            try { dataFolder.createNewFile(); } catch (Exception e) { e.printStackTrace(); }
        }
        loadAllData();
    }

    private File getPlayerFile(UUID uuid) {
        return new File(dataFolder, uuid.toString() + ".yml");
    }

    public void loadAllData() {
        File[] files = dataFolder.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.getName().endsWith(".yml")) {
                    try {
                        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(f);
                        String uuidStr = f.getName().replace(".yml", "");
                        UUID uuid = UUID.fromString(uuidStr);
                        int count = cfg.getInt("souls", 0);
                        soulCounts.put(uuid, count);
                    } catch (Exception e) { e.printStackTrace(); }
                }
            }
        }
    }

    public void saveAllData() {
        for (Map.Entry<UUID, Integer> entry : soulCounts.entrySet()) {
            saveSoulCount(entry.getKey(), entry.getValue());
        }
    }

    public int getSoulCount(Player p) {
        return soulCounts.getOrDefault(p.getUniqueId(), 0);
    }

    public void setSoulCount(Player p, int count) {
        soulCounts.put(p.getUniqueId(), count);
        saveSoulCount(p.getUniqueId(), count);
        adjustHealth(p, count);
    }

    public void addSoul(Player p, int amount) {
        int current = getSoulCount(p);
        setSoulCount(p, current + amount);
    }

    public void removeSoul(Player p, int amount) {
        int current = getSoulCount(p);
        if (current - amount <= 0) {
            setSoulCount(p, 0);
        } else {
            setSoulCount(p, current - amount);
        }
    }

    private void saveSoulCount(UUID uuid, int count) {
        File f = getPlayerFile(uuid);
        YamlConfiguration cfg = new YamlConfiguration();
        cfg.set("souls", count);
        try { cfg.save(f); } catch (Exception e) { e.printStackTrace(); }
    }

    private void loadSoulCount(UUID uuid) {
        File f = getPlayerFile(uuid);
        if (!f.exists()) return;
        try {
            YamlConfiguration cfg = YamlConfiguration.loadConfiguration(f);
            int count = cfg.getInt("souls", 0);
            soulCounts.put(uuid, count);
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void adjustHealth(Player p, int count) {
        AttributeInstance health = p.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (health == null) return;

        // Default health from configuration (or 20)
        double defaultHealth = 20.0;
        double weakenedHealth = 5.0;

        if (count > 0) {
            // Restore to default if it was weakened
            health.setBaseValue(defaultHealth);
        } else {
            // Set weakened health
            health.setBaseValue(weakenedHealth);
        }
    }
}