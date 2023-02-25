package de.snowwars.qualityoflife.keepinventory;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AfkSave implements Listener {
    Map<UUID, Integer> afkTime = new HashMap<>();
    BukkitRunnable runnable = new BukkitRunnable() {
        @Override
        public void run() {
            for(UUID uuid:afkTime.keySet()){
                afkTime.put(uuid, afkTime.get(uuid)+1);
            }
        }
    };
    public AfkSave(Plugin plugin) {
        if (plugin == null) {
            return;
        }
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        runnable.runTaskTimerAsynchronously(plugin, 0, 20);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        afkTime.put(event.getPlayer().getUniqueId(), 0);
    }
    @EventHandler
    public void onMove(PlayerMoveEvent event){
        afkTime.put(event.getPlayer().getUniqueId(), 0);
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        afkTime.put(event.getPlayer().getUniqueId(), 0);
    }
    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        if(afkTime.get(event.getEntity().getUniqueId())>60){
            event.setKeepInventory(true);
        }
    }
}
