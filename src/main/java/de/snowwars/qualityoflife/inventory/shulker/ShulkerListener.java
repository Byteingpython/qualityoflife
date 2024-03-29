package de.snowwars.qualityoflife.inventory.shulker;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class ShulkerListener implements Listener {
    Map<Inventory, ItemStack> openInventories = new HashMap<>();
    Map<Inventory, Integer> openInventoriesSlot = new HashMap<>();

    public ShulkerListener(Plugin plugin) {
        if (plugin == null) {
            return;
        }
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    private void closeShulkerBox(Inventory inventory, Player player) {
        ItemStack itemStack = openInventories.get(inventory);
        BlockStateMeta blockStateMeta = (BlockStateMeta) itemStack.getItemMeta();
        ShulkerBox shulkerBox = (ShulkerBox) blockStateMeta.getBlockState();
        shulkerBox.getInventory().setContents(inventory.getContents());
        shulkerBox.update(true);
        blockStateMeta.setBlockState(shulkerBox);
        itemStack.setItemMeta(blockStateMeta);
        openInventories.remove(inventory);
        openInventoriesSlot.remove(inventory);
        player.closeInventory();
        player.setItemOnCursor(null);
        //Play shulkerbox close sound
        if (player instanceof Player) {
            player.playSound(player.getLocation(), "minecraft:block.shulker_box.close", 1, 1);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (openInventories.containsKey(event.getInventory())) {
            closeShulkerBox(event.getInventory(), (Player) event.getPlayer());
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getItem() == null) {
            return;
        }
        if (event.getItem().getType() == Material.SHULKER_BOX) {

            ItemStack item = event.getItem();
            if (event.getPlayer().getOpenInventory().getType() != InventoryType.CRAFTING) {
                return;
            }
            if (item.getItemMeta() == null) {
                return;
            }
            if (event.getAction() != Action.LEFT_CLICK_AIR && event.getAction() != Action.LEFT_CLICK_BLOCK) {
                return;
            }
            event.setCancelled(true);
            ShulkerBox shulkerBox = (ShulkerBox) ((BlockStateMeta) item.getItemMeta()).getBlockState();
            Inventory inventory = Bukkit.createInventory(null, 27, item.getItemMeta().getDisplayName());
            inventory.setContents(shulkerBox.getInventory().getContents());
            event.getPlayer().closeInventory();
            event.getPlayer().openInventory(inventory);
            openInventories.put(inventory, item);
            openInventoriesSlot.put(inventory, event.getPlayer().getInventory().getHeldItemSlot());
            //Play shulkerbox open sound
            if (event.getPlayer() instanceof Player) {
                event.getPlayer().playSound(event.getPlayer().getLocation(), "minecraft:block.shulker_box.open", 1, 1);
            }
        }
    }

    @EventHandler
    public void onInventory(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) {
            return;
        }
        if (openInventories.containsKey(event.getWhoClicked().getOpenInventory().getTopInventory()) && event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.SHULKER_BOX) {
            event.setCancelled(true);
            return;
        }
        if (!(event.getClickedInventory() instanceof PlayerInventory)) {
            return;
        }
        if (event.getClick() == ClickType.LEFT) {
            if (event.getCurrentItem() != null) {
                if (event.getCurrentItem().getType() == Material.SHULKER_BOX) {
                    event.setCancelled(true);
                    ItemStack item = event.getCurrentItem();
                    if (item.getItemMeta() == null) {
                        return;
                    }
                    ShulkerBox shulkerBox = (ShulkerBox) ((BlockStateMeta) item.getItemMeta()).getBlockState();
                    Inventory inventory = Bukkit.createInventory(null, 27, item.getItemMeta().getDisplayName());
                    inventory.setContents(shulkerBox.getInventory().getContents());
                    event.getWhoClicked().closeInventory();
                    event.getWhoClicked().openInventory(inventory);
                    openInventories.put(inventory, item);
                    openInventoriesSlot.put(inventory, event.getSlot());

                    if (event.getWhoClicked() instanceof Player) {
                        //Play shulkerbox open sound

                        ((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), "minecraft:block.shulker_box.open", 1, 1);
                    }
                }
            }
        }

    }
}


