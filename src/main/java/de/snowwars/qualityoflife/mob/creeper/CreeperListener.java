package de.snowwars.qualityoflife.mob.creeper;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Creeper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class CreeperListener implements Listener {
    JavaPlugin plugin;
    private static final List<Material> materials = List.of(Material.DIRT, Material.GRASS_BLOCK);

    public CreeperListener(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent event) {
        if (event.getEntity() instanceof Creeper) {
            Integer count = 0;
            for (Block block : List.copyOf(event.blockList())) {

                if (materials.contains(block.getType())) {
                    block.setType(Material.AIR);
                    count++;
                    event.blockList().remove(block);
                }
            }
            Integer finalCount = count;
            new BukkitRunnable() {
                Integer innerCount = finalCount;

                @Override
                public void run() {
                    for (int i = 0; i < Math.ceil(finalCount / 64.0); i++) {
                        event.getLocation().getWorld().dropItem(event.getLocation(), new org.bukkit.inventory.ItemStack(Material.DIRT, innerCount % 64));
                        innerCount -= innerCount % 64;
                    }
                }
            }.runTaskLater(plugin, 1);
        }
    }
}
