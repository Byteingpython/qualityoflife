package de.snowwars.qualityoflife;

import org.bukkit.plugin.java.JavaPlugin;

public final class Qualityoflife extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        InventoryListener inventoryListener=new InventoryListener(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
