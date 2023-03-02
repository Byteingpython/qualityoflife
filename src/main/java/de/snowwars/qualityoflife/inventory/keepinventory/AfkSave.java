package de.snowwars.qualityoflife.inventory.keepinventory;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class AfkSave extends PlaceholderExpansion implements Listener {
    Map<UUID, Integer> afkTime = new HashMap<>();
    BukkitRunnable runnable = new BukkitRunnable() {
        @Override
        public void run() {
            for (Map.Entry<UUID, Integer> entry : afkTime.entrySet()) {
                afkTime.put(entry.getKey(), afkTime.get(entry.getKey()) + 1);
            }
        }
    };

    public AfkSave(Plugin plugin) {
        if (plugin == null) {
            return;
        }
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            if (!this.register()) {
                plugin.getLogger().log(Level.SEVERE, "Could not register placeholder");
            }
        }
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        runnable.runTaskTimerAsynchronously(plugin, 0, 20);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        afkTime.put(event.getPlayer().getUniqueId(), 0);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        afkTime.put(event.getPlayer().getUniqueId(), 0);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        afkTime.put(event.getPlayer().getUniqueId(), 0);
    }

    @EventHandler(priority = org.bukkit.event.EventPriority.HIGHEST)
    public void onDeath(PlayerDeathEvent event) {
        if (afkTime.get(event.getEntity().getUniqueId()) > 60) {
            event.setKeepInventory(true);
            event.getDrops().clear();
        }
    }

    @Override
    public @NotNull String getIdentifier() {
        return "afk";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Snowwars";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if(afkTime.containsKey(player.getUniqueId())) {
            if(afkTime.get(player.getUniqueId()) > 60) {
                return "&7";
            }
        }
        return "";
    }
}
