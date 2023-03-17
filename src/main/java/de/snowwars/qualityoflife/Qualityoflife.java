package de.snowwars.qualityoflife;

import de.snowwars.qualityoflife.alert.AlertCommand;
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
        if (configuration.contains(ConfigurationElement.AUTOREFILL.getPath()) && configuration.getBoolean(ConfigurationElement.AUTOREFILL.getPath())) {
            InventoryListener inventoryListener = new InventoryListener(this);
            Bukkit.getLogger().info("[QualityOfLife] Autorefill enabled");
        }
        if (configuration.contains(ConfigurationElement.SHULKER.getPath()) && configuration.getBoolean(ConfigurationElement.SHULKER.getPath())) {
            ShulkerListener shulkerListener = new ShulkerListener(this);
            Bukkit.getLogger().info("[QualityOfLife] Shulker enabled");
        }
        if (configuration.contains(ConfigurationElement.KEEPIMPORTANT.getPath()) && configuration.getBoolean(ConfigurationElement.KEEPIMPORTANT.getPath())) {
            KeepImportant keepImportant = new KeepImportant(this);
            Bukkit.getLogger().info("[QualityOfLife] KeepImportant enabled");
        }
        if (configuration.contains(ConfigurationElement.DUMPINVENTORY.getPath()) && configuration.getBoolean(ConfigurationElement.DUMPINVENTORY.getPath())) {
            SaveInventory saveInventory = new SaveInventory(this);
            Bukkit.getLogger().info("[QualityOfLife] DumpInventory enabled");
        }
        if (configuration.contains(ConfigurationElement.BACKPACK.getPath()) && configuration.getBoolean(ConfigurationElement.BACKPACK.getPath())) {
            manager = new BackpackManager(this);
            new BackpackCommand(manager, this);
            Bukkit.getLogger().info("[QualityOfLife] Backpack enabled");
        } else {
            Bukkit.getPluginCommand("backpack").setExecutor(new DisabledCommand());
        }
        if (configuration.contains(ConfigurationElement.STATUS.getPath()) && configuration.getBoolean(ConfigurationElement.STATUS.getPath())) {
            StatusCommand statusCommand = new StatusCommand(configuration, this);
            this.getCommand("status").setExecutor(statusCommand);
            this.getCommand("status").setTabCompleter(statusCommand);
            Bukkit.getLogger().info("[QualityOfLife] Status enabled");
        } else {
            Bukkit.getPluginCommand("status").setExecutor(new DisabledCommand());
        }
        if (configuration.contains(ConfigurationElement.AFKSAVE.getPath()) && configuration.getBoolean(ConfigurationElement.AFKSAVE.getPath())) {
            AfkSave afkSave = new AfkSave(this);
            Bukkit.getLogger().info("[QualityOfLife] AfkSave enabled");
        }
        if (configuration.contains(ConfigurationElement.VINE.getPath()) && configuration.getBoolean(ConfigurationElement.VINE.getPath())){
            new Vine(this);
            Bukkit.getLogger().info("[QualityOfLife] Vine enabled");
        }
        if (configuration.contains(ConfigurationElement.ARENA.getPath()) && configuration.getBoolean(ConfigurationElement.ARENA.getPath())) {
            arenaManager = new ArenaManager(this);
            ArenaCommand arenaCommand = new ArenaCommand(arenaManager);
            this.getCommand("arena").setExecutor(arenaCommand);
            Bukkit.getLogger().info("[QualityOfLife] Arena enabled");
        } else {
            Bukkit.getPluginCommand("arena").setExecutor(new DisabledCommand());
        }
        if (configuration.contains(ConfigurationElement.CREEPER.getPath()) && configuration.getBoolean(ConfigurationElement.CREEPER.getPath())) {
            new CreeperListener(this);
            Bukkit.getLogger().info("[QualityOfLife] Creeper enabled");
        }
        if (configuration.contains(ConfigurationElement.ALERT.getPath()) && configuration.getBoolean(ConfigurationElement.ALERT.getPath())) {
            new AlertCommand(this);
            Bukkit.getLogger().info("[QualityOfLife] Alert enabled");
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
