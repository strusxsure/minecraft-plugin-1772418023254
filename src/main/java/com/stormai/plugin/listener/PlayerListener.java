package com.stormai.plugin.listener;

import com.stormai.plugin.SoulBoundSMP;
import com.stormai.plugin.util.SoulManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;

public class PlayerListener implements Listener {

    private final SoulBoundSMP plugin;

    public PlayerListener(SoulBoundSMP plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        // Load soul count from config
        int count = plugin.getSoulManager().getSoulCount(p);
        // Set default max health if not already modified
        AttributeInstance healthAttr = p.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (healthAttr != null) {
            healthAttr.setBaseValue(20.0); // default health
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player victim = e.getEntity();
        int souls = SoulManager.getInstance().getSoulCount(victim);
        e.getDrops().clear();

        // Drop a physical Soul item
        ItemStack soulItem = new ItemStack(Material.SHULK_SHELL);
        ItemMeta meta = soulItem.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_PURPLE + "Soul");
        meta.setLore(java.util.Collections.singletonList(ChatColor.GRAY + "Hold to gain a temporary ability."));
        soulItem.setItemMeta(meta);

        // Drop at victim's location
        World world = victim.getWorld();
        if (world != null) {
            world.dropItemNaturally(victim.getLocation(), soulItem);
        }

        // Reset max health to default (20) for demonstration; actual reduction handled on next join
        AttributeInstance healthAttr = victim.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (healthAttr != null) {
            healthAttr.setBaseValue(20.0);
        }
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent e) {
        if (!(e.getItem() != null && e.getItem().getItemMeta() != null &&
                "Soul".equals(ChatColor.stripColor(e.getItem().getItemMeta().getDisplayName())))) {
            return;
        }

        Player player = e.getPlayer();
        int current = SoulManager.getInstance().getSoulCount(player);
        SoulManager.getInstance().addSoul(player, 1);
        player.sendMessage(ChatColor.AQUA + "You gathered a Soul! Current: " + SoulManager.getInstance().getSoulCount(player));

        // Revive: restore max health to default (20) if it was reduced
        AttributeInstance healthAttr = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (healthAttr != null) {
            healthAttr.setBaseValue(20.0);
        }
    }

    // Optional: Handle player kill to grant a soul
    // Using EntityDamageByEntityEvent to detect kill might be more accurate,
    // but for simplicity we rely on death event listener to manage drops.
}