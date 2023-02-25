package de.snowwars.qualityoflife;

import de.snowwars.qualityoflife.keepinventory.AfkSave;
import de.snowwars.qualityoflife.keepinventory.KeepImportant;
import de.snowwars.qualityoflife.keepinventory.SaveInventory;
import de.snowwars.qualityoflife.shulker.ShulkerListener;
import de.snowwars.qualityoflife.status.StatusCommand;
import de.snowwars.qualityoflife.status.StatusManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class Qualityoflife extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        FileConfiguration configuration = getConfig();
        saveDefaultConfig();
        InventoryListener inventoryListener=new InventoryListener(this);
        ShulkerListener shulkerListener=new ShulkerListener(this);
        KeepImportant keepImportant=new KeepImportant(this);
        SaveInventory saveInventory=new SaveInventory(this);
        StatusCommand statusCommand = new StatusCommand(configuration, this);
        this.getCommand("status").setExecutor(statusCommand);
        this.getCommand("status").setTabCompleter(statusCommand);
        AfkSave afkSave=new AfkSave(this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
