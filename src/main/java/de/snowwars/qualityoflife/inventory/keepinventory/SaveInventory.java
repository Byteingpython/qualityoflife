package de.snowwars.qualityoflife.inventory.keepinventory;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/*
  Save Inventory to File on Death
 */
public class SaveInventory implements Listener {
    Plugin plugin;

    public SaveInventory(Plugin plugin) {
        if (plugin == null) {
            return;
        }

        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
        new File(plugin.getDataFolder() + "/inventories").mkdirs();

    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        String timeStamp = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date());
        File file = new File(plugin.getDataFolder() + "/inventories/" + event.getEntity().getName() + " " + timeStamp + ".txt");
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            StringBuilder content = new StringBuilder();
            for (ItemStack stack : event.getEntity().getInventory().getContents()) {
                if (stack != null) {
                    StringBuilder line = new StringBuilder(stack.getType().name());
                    line.append(" x");
                    line.append(Integer.toString(stack.getAmount()));
                    if (stack.hasItemMeta()) {
                        if (stack.getItemMeta().hasEnchants()) {
                            line.append(" Enchants: ");
                            for (Map.Entry<org.bukkit.enchantments.Enchantment, Integer> entry : stack.getItemMeta().getEnchants().entrySet()) {
                                line.append(entry.getKey().toString());
                                line.append(" ");
                                line.append(entry.getValue().toString());
                                line.append(", ");
                            }
                        }
                    }
                    content.append(line.toString());
                    content.append("\n");
                }
                writer.write(content.toString());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
