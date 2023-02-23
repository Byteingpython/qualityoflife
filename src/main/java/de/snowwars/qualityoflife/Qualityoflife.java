package de.snowwars.qualityoflife;

import de.snowwars.qualityoflife.keepinventory.KeepImportant;
import de.snowwars.qualityoflife.keepinventory.SaveInventory;
import de.snowwars.qualityoflife.shulker.ShulkerListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class Qualityoflife extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        InventoryListener inventoryListener=new InventoryListener(this);
        ShulkerListener shulkerListener=new ShulkerListener(this);
        KeepImportant keepImportant=new KeepImportant(this);
        SaveInventory saveInventory=new SaveInventory(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
