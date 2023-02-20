package de.snowwars.qualityoflife;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;


public class InventoryListener implements Listener {
    private Plugin plugin;
    public InventoryListener(final Plugin plugin ){
        if(plugin == null){
            return;
        }
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.plugin=plugin;
    }

    /*
    Search for other Stacks of the same Item, when item is used
    */
    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        if(event.getItem()==null){
            return;
        }

        BukkitRunnable inventoryRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                if(event.getPlayer().getInventory().getItemInMainHand()==null){
                    ItemStack item = searchForItem(event.getItem().getType(), event.getPlayer().getInventory());
                    if(item==null){
                        return;
                    }
                    event.getPlayer().getInventory().remove(item);
                    event.getPlayer().getInventory().setItemInMainHand(item);
                }
                else if(event.getPlayer().getInventory().getItemInOffHand()==null){
                    ItemStack item = searchForItem(event.getItem().getType(), event.getPlayer().getInventory());
                    if(item==null){
                        return;
                    }
                    event.getPlayer().getInventory().remove(item);
                    event.getPlayer().getInventory().setItemInOffHand(item);
                }
            }
        };
        inventoryRunnable.runTaskAsynchronously(this.plugin);
    }

    /*
    Search for another Stack in Inventory
     */
    private ItemStack searchForItem(Material material, PlayerInventory inventory){
        if(material==null||inventory==null){
            return null;
        }
        ItemStack biggestStack=null;
        if(inventory.contains(material)){

            for(ItemStack stack:inventory.getContents()){
                if(stack.getType()==material){
                    if(biggestStack==null){
                        biggestStack=stack;
                    }
                    else if(biggestStack.getAmount()<=stack.getAmount()){
                        biggestStack=stack;
                    }
                }
            }
        }
        return biggestStack;
    }
}
