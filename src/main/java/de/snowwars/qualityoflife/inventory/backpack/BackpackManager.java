package de.snowwars.qualityoflife.inventory.backpack;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

import static de.snowwars.qualityoflife.inventory.keepinventory.KeepImportant.parseDrops;

public class BackpackManager implements Listener {
    Map<UUID, ItemStack[]> backpacks;
    Plugin plugin;

    List<Inventory> openInventories = new ArrayList<>();
    protected NamespacedKey key;

    public BackpackManager(Plugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        backpacks = loadBackpacks();
        key = new NamespacedKey(plugin, "backpack");
    }

    private Integer getNumberOfAdvancements(Player player) {
        Iterator<Advancement> advancementIterator = Bukkit.advancementIterator();
        Integer numberOfAdvancements = 0;
        while (advancementIterator.hasNext()) {
            Advancement advancement = advancementIterator.next();
            if (advancement.getKey().getKey().contains("recipes")) {
                continue;
            }
            if(player.getAdvancementProgress(advancement).isDone()) {
                numberOfAdvancements++;
            }


        }
        return numberOfAdvancements;
    }


    public void openBackpack(Player player) {

        Integer advancements = (int) Math.floor(getNumberOfAdvancements(player) / 2);
        Integer inventorySize = advancements - (advancements % 9) + 9;
        Inventory inventory = Bukkit.createInventory(null, inventorySize, "§6Backpack");
        if(backpacks.containsKey(player.getUniqueId())){
            try {
                inventory.setContents(backpacks.get(player.getUniqueId()));
            } catch (IllegalArgumentException e){
                Bukkit.getLogger().log(Level.WARNING, "Could not load backpack");
                Bukkit.getLogger().log(Level.WARNING, "Too little Inventory Size");
                player.sendMessage("§cCould not load backpack. Contact an Admin");
            }

        }
        for(int i = 0; i < 9-advancements%9; i++){
            ItemStack stack = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
            ItemMeta meta = stack.getItemMeta();
            meta.setDisplayName("§7 Not Available");
            meta.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte) 1);
            stack.setItemMeta(meta);
            inventory.setItem(inventorySize - i -1, stack);
        }
        openInventories.add(inventory);
        player.openInventory(inventory);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (openInventories.contains(event.getInventory())) {
            ItemStack[] contents = event.getInventory().getContents();
            for(ItemStack stack : event.getInventory().getContents()){
                if(stack != null && stack.getType() != Material.AIR){
                    if(stack.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.BYTE)){
                        contents[Arrays.asList(contents).indexOf(stack)] = null;
                    }
                }
            }
            backpacks.put(event.getPlayer().getUniqueId(), contents);
            openInventories.remove(event.getInventory());
        }
    }

    public void saveBackpacks(){
        try {
            FileOutputStream stream = new FileOutputStream(plugin.getDataFolder() + "/backpacks.dat");
            BukkitObjectOutputStream outputStream = new BukkitObjectOutputStream(stream);
            outputStream.writeObject(backpacks);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private Map<UUID, ItemStack[]> loadBackpacks(){
        Map<UUID, ItemStack[]> backpacks;
        try {
            FileInputStream stream = new FileInputStream(plugin.getDataFolder() + "/backpacks.dat");
            BukkitObjectInputStream inputStream = new BukkitObjectInputStream(stream);
            backpacks = (Map<UUID, ItemStack[]>) inputStream.readObject();
        } catch (FileNotFoundException e) {
            Bukkit.getLogger().log(Level.WARNING, "No backpacks found");
            backpacks = new HashMap<>();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return backpacks;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event){
        if(event.getCurrentItem()==null){
            return;
        }
        if(event.getCurrentItem().getType() == Material.AIR){
            return;
        }
        if(!event.getCurrentItem().hasItemMeta()){
            return;
        }
        if(event.getCurrentItem().getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.BYTE)){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        if(backpacks.containsKey(event.getEntity().getUniqueId())){
            ItemStack[] drops = parseDrops(backpacks.get(event.getEntity().getUniqueId()));
            List<ItemStack> backpack = new LinkedList<>(Arrays.asList(backpacks.get(event.getEntity().getUniqueId())));
            backpack.removeAll(Arrays.asList(drops));
            event.getDrops().addAll(Arrays.asList(drops));
            backpacks.put(event.getEntity().getUniqueId(), backpack.toArray(new ItemStack[0]));
        }
    }




}
