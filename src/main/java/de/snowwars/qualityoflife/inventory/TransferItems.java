package de.snowwars.qualityoflife.inventory;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class TransferItems implements Listener {

    public TransferItems(Plugin plugin) {
        if (plugin == null) {
            return;
        }
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    private ItemStack[] searchForItems(Inventory inventory, Material material) {
        if (inventory == null || material == null) {
            return null;
        }
        List<ItemStack> stacks = new ArrayList<>();
        for (ItemStack stack : inventory.getContents()) {
            if (stack != null) {
                if (stack.getType() == material) {
                    stacks.add(stack);
                }
            }
        }
        return stacks.toArray(new ItemStack[0]);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;
        if (event.getClick() != ClickType.DROP) return;
        if (event.getCurrentItem() == null) return;

        if (event.getWhoClicked().getOpenInventory().getType() != InventoryType.CHEST && event.getWhoClicked().getOpenInventory().getType() != InventoryType.BARREL && event.getClickedInventory().getType() != InventoryType.SHULKER_BOX)
            return;
        event.setCancelled(true);
        Inventory inventory;
        Inventory oldInventory;
        if (event.getClickedInventory() instanceof PlayerInventory) {
            inventory = event.getWhoClicked().getOpenInventory().getTopInventory();
            oldInventory = event.getWhoClicked().getOpenInventory().getBottomInventory();
        } else {
            inventory = event.getWhoClicked().getOpenInventory().getBottomInventory();
            oldInventory = event.getWhoClicked().getOpenInventory().getTopInventory();
        }
        ItemStack[] stacks = searchForItems(event.getClickedInventory(), event.getCurrentItem().getType());
        if (stacks == null) return;
        for (ItemStack stack : stacks) {
            if (inventory.firstEmpty() != -1) {
                inventory.addItem(stack);
                oldInventory.removeItem(stack);
            }
        }
    }

}
