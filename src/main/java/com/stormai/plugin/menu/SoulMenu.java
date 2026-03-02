package com.stormai.plugin.menu;

import com.stormai.plugin.SoulBoundSMP;
import com.stormai.plugin.util.Ability;
import com.stormai.plugin.util.SoulManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class SoulMenu {

    private final SoulBoundSMP plugin;
    private final HashMap<String, Ability> abilities; // command->ability

    public SoulMenu(SoulBoundSMP plugin) {
        this.plugin = plugin;
        this.abilities = new HashMap<>();
        // Register abilities (hardcoded for demo)
        abilities.put("strength", new Ability("Strength", Material.IRON_SWORD, 1));
        abilities.put("speed", new Ability("Speed", Material.ARROW, 2));
        abilities.put("extra_hearts", new Ability("Extra Hearts", Material.HEART_OF_THE_SEA, 5));
    }

    public void open(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, ChatColor.DARK_PURPLE + "Soul Abilities");

        int slot = 0;
        for (Map.Entry<String, Ability> entry : abilities.entrySet()) {
            Ability ability = entry.getValue();

            ItemStack display = new ItemStack(ability.getMaterial());
            ItemMeta meta = display.getItemMeta();
            meta.setDisplayName(ChatColor.AQUA + ability.getName());
            meta.setLore(Arrays.asList(
                    ChatColor.YELLOW + "Cost: " + ability.getCost() + " Souls",
                    ChatColor.GRAY + "Click to toggle."
            ));
            display.setItemMeta(meta);
            inv.setItem(slot, display);
            slot++;
        }

        player.openInventory(inv);
    }

    // This method would be called from an inventory click listener.
    // For brevity, it is omitted here; in a full implementation,
    // handle InventoryClickEvent to apply/remove abilities.
}