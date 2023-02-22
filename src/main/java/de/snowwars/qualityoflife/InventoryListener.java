package de.snowwars.qualityoflife;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
/*
Replace used Item with another Stack of the same Item
 */

public class InventoryListener implements Listener {
    List<String> toolTypes = List.of("AXE", "HOE", "PICKAXE", "SHOVEL", "SWORD", "STEEL", "SHEARS");
    private Plugin plugin;

    public InventoryListener(final Plugin plugin) {
        if (plugin == null) {
            return;
        }
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }

    /*
    If the tool is broken, replace it with another Tool of the same type
     */
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Material material = event.getPlayer().getInventory().getItemInMainHand().getType();
        String[] name = material.toString().split("_");
        String toolType = name[name.length - 1];
        Bukkit.getLogger().log(java.util.logging.Level.INFO, "\"" + toolType+ "\"" + " is the ToolType");
        if (!toolTypes.contains(toolType)) {
            Bukkit.getLogger().log(java.util.logging.Level.INFO, "ToolType not in Array");
            return;
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                if (event.getPlayer().getInventory().getItemInMainHand().getType() == Material.AIR) {


                    ItemStack item = searchForItem(event.getPlayer().getInventory(), toolType);
                    if (item == null) {
                        Bukkit.getLogger().log(java.util.logging.Level.INFO, "No Item found");
                        return;
                    }
                    event.getPlayer().getInventory().removeItem(item);
                    event.getPlayer().getInventory().setItemInMainHand(item);
                }
            }
        }.runTaskAsynchronously(this.plugin);
    }

    /*
    Search for other Stacks of the same Item, when item is used
    */
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getItem() == null || event.getHand() == null) {
            return;
        }
        Material material = event.getItem().getType();
        EquipmentSlot hand = event.getHand();

        BukkitRunnable inventoryRunnable = new BukkitRunnable() {

            @Override
            public void run() {

                if (event.getPlayer().getInventory().getItem(hand).getType().equals(Material.AIR)) {
                    ItemStack item = searchForItem(material, event.getPlayer().getInventory());
                    if (item == null) {

                        return;
                    }
                    event.getPlayer().getInventory().removeItem(item);
                    event.getPlayer().getInventory().setItem(hand, item);
                }
            }
        };
        inventoryRunnable.runTaskAsynchronously(this.plugin);
    }

    /*
    Search for another Stack in Inventory
     */
    private ItemStack searchForItem(Material material, PlayerInventory inventory) {
        if (material == null || inventory == null) {
            return null;
        }
        ItemStack biggestStack = null;
        if (inventory.contains(material)) {

            for (ItemStack stack : inventory.getContents()) {

                if (stack != null) {
                    if (stack.getType() == material) {
                        if (biggestStack == null) {
                            biggestStack = stack;
                        } else if (biggestStack.getAmount() <= stack.getAmount()) {
                            biggestStack = stack;
                        }
                    }
                }
            }
        }
        return biggestStack;
    }

    private ItemStack searchForItem(PlayerInventory inventory, String toolType) {
        if (toolType == null || inventory == null) {
            return null;
        }
        ItemStack biggestStack = null;


        for (ItemStack stack : inventory.getContents()) {
            if (stack == null) {
                continue;
            }
            if (stack.getType().toString().toLowerCase().contains(toolType.toLowerCase())) {
                if (biggestStack == null) {
                    biggestStack = stack;
                }
                else if (stack.getType().getMaxDurability() > biggestStack.getType().getMaxDurability()) {
                    biggestStack = stack;
                }

            }
        }

        return biggestStack;
    }
}
