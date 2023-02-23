package de.snowwars.qualityoflife;

import de.snowwars.qualityoflife.keepinventory.KeepImportant;
import de.snowwars.qualityoflife.shulker.ShulkerListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Qualityoflife extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        InventoryListener inventoryListener=new InventoryListener(this);
        ShulkerListener shulkerListener=new ShulkerListener(this);
        KeepImportant keepImportant=new KeepImportant(this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
