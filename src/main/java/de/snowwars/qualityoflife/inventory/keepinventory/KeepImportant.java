package de.snowwars.qualityoflife.inventory.keepinventory;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.*;

/*
 Keep important Items on Death
 */
public class KeepImportant implements Listener {
    private static List<String> important_items = List.of("DIAMOND", "NETHERITE", "ELYTRA", "SHULKER");
    Map<UUID, List<ItemStack>> keepDrops = new HashMap<>();

    public KeepImportant(Plugin plugin) {
        if (plugin == null) {
            return;
        }
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (event.getKeepInventory()) return;
        keepDrops.put(event.getEntity().getUniqueId(), new ArrayList<>());
        List<ItemStack> drops = new ArrayList<>(event.getDrops());
        for (ItemStack stack : drops) {
            String[] name = stack.getType().toString().split("_");
            String toolType = name[0];
            if (important_items.contains(toolType)) {
                event.getEntity().getInventory().removeItem(stack);
                event.getDrops().remove(stack);
                keepDrops.get(event.getEntity().getUniqueId()).add(stack);
            }
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        if (keepDrops.containsKey(event.getPlayer().getUniqueId())) {
            for (ItemStack stack : keepDrops.get(event.getPlayer().getUniqueId())) {
                if (event.getPlayer().getInventory().firstEmpty() != -1) {
                    event.getPlayer().getInventory().addItem(stack);
                } else {
                    event.getPlayer().getWorld().dropItem(event.getRespawnLocation(), stack);
                }
            }
        }
    }

    public static ItemStack[] parseDrops(ItemStack[] drops) {
        List<ItemStack> newDrops = new ArrayList<>();
        for (ItemStack stack : drops) {
            if (stack == null) continue;
            String[] name = stack.getType().toString().split("_");
            String toolType = name[0];
            if (!important_items.contains(toolType)) {
                newDrops.add(stack);
            }
        }
        return newDrops.toArray(new ItemStack[0]);
    }
}
