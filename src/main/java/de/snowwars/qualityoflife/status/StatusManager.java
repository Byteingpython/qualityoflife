package de.snowwars.qualityoflife.status;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class StatusManager extends PlaceholderExpansion {
    Map<UUID, String> secondStatusPlayer = new HashMap<>();

    Map<UUID, String> firstStatusPlayer = new HashMap<>();
    FileConfiguration configuration;
    Plugin plugin;

    public StatusManager(FileConfiguration configuration, Plugin plugin) {
        this.configuration = configuration;
        this.plugin = plugin;
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            if (!this.register()) {
                plugin.getLogger().log(Level.SEVERE, "Could not register placeholder");
            }
        } else {
            plugin.getLogger().log(Level.SEVERE, "Could not find PlaceholderAPI");
        }

    }

    public Boolean setStatus(String first, String second, UUID uuid) {
        if (configuration.contains("stati.first." + first) && configuration.contains("stati.second." + second)) {
            firstStatusPlayer.put(uuid, first);
            secondStatusPlayer.put(uuid, second);
        } else {
            return false;
        }
        return true;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "status";
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
    public String onRequest(OfflinePlayer player, String identifier) {
        StringBuilder builder = new StringBuilder();
        if (!(firstStatusPlayer.containsKey(player.getUniqueId()))) {
            firstStatusPlayer.put(player.getUniqueId(), "default");
        }
        if (!(secondStatusPlayer.containsKey(player.getUniqueId()))) {
            secondStatusPlayer.put(player.getUniqueId(), "default");
        }
        if (configuration.contains("stati.first." + firstStatusPlayer.get(player.getUniqueId()))) {
            builder.append(configuration.get("stati.first." + firstStatusPlayer.get(player.getUniqueId())));
        }
        if (configuration.contains("stati.second." + secondStatusPlayer.get(player.getUniqueId()))) {
            builder.append(configuration.get("stati.second." + secondStatusPlayer.get(player.getUniqueId())));
        }
        builder.append("&r");
        return builder.toString();
    }
}
