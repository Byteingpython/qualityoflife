package de.snowwars.qualityoflife;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
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


    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event){
        if(event.getDamager() instanceof Player){
            Player player = (Player) event.getDamager();
            if(player.getInventory().getItemInMainHand().getType()!=Material.AIR){
                Material material = player.getInventory().getItemInMainHand().getType();
                String name = material.toString().split("_")[material.toString().split("_").length-1];
                BukkitRunnable runnable = new BukkitRunnable() {
                    @Override
                    public void run() {
                        se
                    }
                }
            }
        }
    }
    /*
    Search for other Stacks of the same Item, when item is used
    */
    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        if(event.getItem()==null||event.getHand()==null){
            return;
        }
        Material material=event.getItem().getType();
        EquipmentSlot hand = event.getHand();
        BukkitRunnable inventoryRunnable = new BukkitRunnable() {

            @Override
            public void run() {

                if(event.getPlayer().getInventory().getItem(hand).getType()==Material.AIR){

                    ItemStack item = searchForItem(material, event.getPlayer().getInventory());
                    if(item==null){

                        return;
                    }
                    event.getPlayer().getInventory().removeItem(item);
                    event.getPlayer().getInventory().setItem(hand, item);
                }
            }
        };
        if(material.isEdible()){
            inventoryRunnable=new BukkitRunnable() {
                @Override
                public void run() {

                    if(event.getPlayer().getInventory().getItem(hand).getType()==Material.AIR){

                        ItemStack item = searchForFood(event.getPlayer().getInventory());
                        if(item==null){

                            return;
                        }
                        event.getPlayer().getInventory().removeItem(item);
                        event.getPlayer().getInventory().setItem(hand, item);
                    }
                }
            };
            inventoryRunnable.runTaskLaterAsynchronously(this.plugin, 33);
            return;
        }
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

                if(stack != null) {
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
    /*
    Search for Food in Inventory
     */
    private ItemStack searchForFood(PlayerInventory inventory){
        if(inventory==null){
            return null;
        }
        ItemStack biggestStack=null;
        for(ItemStack stack:inventory.getContents()){
            if(stack != null) {
                if (stack.getType().isEdible()) {
                    if (biggestStack == null) {
                        biggestStack = stack;
                    } else if (biggestStack.getAmount() <= stack.getAmount()) {
                        biggestStack = stack;
                    }
                }
            }
        }
        return biggestStack;
    }
}
