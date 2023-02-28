package de.snowwars.qualityoflife;

import de.snowwars.qualityoflife.inventory.InventoryListener;
import de.snowwars.qualityoflife.inventory.backpack.BackpackCommand;
import de.snowwars.qualityoflife.inventory.backpack.BackpackManager;
import de.snowwars.qualityoflife.inventory.keepinventory.AfkSave;
import de.snowwars.qualityoflife.inventory.keepinventory.KeepImportant;
import de.snowwars.qualityoflife.inventory.keepinventory.SaveInventory;
import de.snowwars.qualityoflife.inventory.shulker.ShulkerListener;
import de.snowwars.qualityoflife.status.StatusCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class Qualityoflife extends JavaPlugin {
    BackpackManager manager;
    @Override
    public void onEnable() {
        // Plugin startup logic
        FileConfiguration configuration = getConfig();
        saveDefaultConfig();
        InventoryListener inventoryListener = new InventoryListener(this);
        ShulkerListener shulkerListener = new ShulkerListener(this);
        KeepImportant keepImportant = new KeepImportant(this);
        SaveInventory saveInventory = new SaveInventory(this);
        StatusCommand statusCommand = new StatusCommand(configuration, this);
        manager= new BackpackManager(this);
        this.getCommand("status").setExecutor(statusCommand);
        this.getCommand("status").setTabCompleter(statusCommand);
        AfkSave afkSave = new AfkSave(this);
        new BackpackCommand(manager, this);

    }

    @Override
    public void onDisable() {
        manager.saveBackpacks();
    }
}
