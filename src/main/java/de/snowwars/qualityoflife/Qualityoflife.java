package de.snowwars.qualityoflife;

import de.snowwars.qualityoflife.arena.ArenaCommand;
import de.snowwars.qualityoflife.arena.ArenaManager;
import de.snowwars.qualityoflife.block.Vine;
import de.snowwars.qualityoflife.inventory.InventoryListener;
import de.snowwars.qualityoflife.inventory.backpack.BackpackCommand;
import de.snowwars.qualityoflife.inventory.backpack.BackpackManager;
import de.snowwars.qualityoflife.inventory.keepinventory.AfkSave;
import de.snowwars.qualityoflife.inventory.keepinventory.KeepImportant;
import de.snowwars.qualityoflife.inventory.keepinventory.SaveInventory;
import de.snowwars.qualityoflife.inventory.shulker.ShulkerListener;
import de.snowwars.qualityoflife.mob.creeper.CreeperListener;
import de.snowwars.qualityoflife.status.StatusCommand;
import de.snowwars.qualityoflife.utils.ConfigurationElement;
import de.snowwars.qualityoflife.utils.DisabledCommand;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class Qualityoflife extends JavaPlugin {
    BackpackManager manager;
    ArenaManager arenaManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        FileConfiguration configuration = getConfig();
        saveDefaultConfig();
        if (!configuration.contains(ConfigurationElement.AUTOREFILL.getPath()) && configuration.getBoolean(ConfigurationElement.AUTOREFILL.getPath())) {
            InventoryListener inventoryListener = new InventoryListener(this);
        }
        if (configuration.get(ConfigurationElement.SHULKER.getPath()) != null && configuration.getBoolean(ConfigurationElement.SHULKER.getPath())) {
            ShulkerListener shulkerListener = new ShulkerListener(this);
        }
        if (configuration.get(ConfigurationElement.KEEPIMPORTANT.getPath()) != null && configuration.getBoolean(ConfigurationElement.KEEPIMPORTANT.getPath())) {
            KeepImportant keepImportant = new KeepImportant(this);
        }
        if (configuration.get(ConfigurationElement.DUMPINVENTORY.getPath()) != null && configuration.getBoolean(ConfigurationElement.DUMPINVENTORY.getPath())) {
            SaveInventory saveInventory = new SaveInventory(this);
        }
        if (configuration.get(ConfigurationElement.BACKPACK.getPath()) != null && configuration.getBoolean(ConfigurationElement.BACKPACK.getPath())) {
            manager = new BackpackManager(this);
            new BackpackCommand(manager, this);
        } else {
            Bukkit.getPluginCommand("backpack").setExecutor(new DisabledCommand());
        }
        if (configuration.get(ConfigurationElement.STATUS.getPath()) != null && configuration.getBoolean("features.status")) {
            StatusCommand statusCommand = new StatusCommand(configuration, this);
            this.getCommand("status").setExecutor(statusCommand);
            this.getCommand("status").setTabCompleter(statusCommand);
        } else {
            Bukkit.getPluginCommand("status").setExecutor(new DisabledCommand());
        }
        if (configuration.get(ConfigurationElement.AFKSAVE.getPath()) != null && configuration.getBoolean(ConfigurationElement.AFKSAVE.getPath())) {
            AfkSave afkSave = new AfkSave(this);
        }
        if (configuration.get(ConfigurationElement.VINE.getPath()) != null && configuration.getBoolean(ConfigurationElement.VINE.getPath()))
            new Vine(this);
        if (configuration.get(ConfigurationElement.ARENA.getPath()) != null && configuration.getBoolean(ConfigurationElement.ARENA.getPath())) {
            arenaManager = new ArenaManager(this);
            ArenaCommand arenaCommand = new ArenaCommand(arenaManager);
            this.getCommand("arena").setExecutor(arenaCommand);
        } else {
            Bukkit.getPluginCommand("arena").setExecutor(new DisabledCommand());
        }
        if (configuration.get(ConfigurationElement.CREEPER.getPath()) != null && configuration.getBoolean(ConfigurationElement.CREEPER.getPath())) {
            new CreeperListener(this);
        }
    }


    @Override
    public void onDisable() {
        manager.saveBackpacks();
        if (arenaManager != null) {
            arenaManager.saveArenas();
        }
    }
}
