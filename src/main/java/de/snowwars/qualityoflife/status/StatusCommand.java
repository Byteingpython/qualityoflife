package de.snowwars.qualityoflife.status;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.util.StringUtil.copyPartialMatches;

public class StatusCommand implements TabExecutor {
    FileConfiguration configuration;
    Plugin plugin;
    StatusManager statusManager;

    public StatusCommand(FileConfiguration configuration, Plugin plugin) {
        this.configuration = configuration;
        this.plugin = plugin;
        this.statusManager = new StatusManager(configuration, plugin);
        plugin.reloadConfig();
        this.configuration = plugin.getConfig();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length <= 1) {
            return false;
        }
        if (!(sender instanceof Player)) {
            return false;
        }
        if (statusManager.setStatus(args[0], args[1], ((Player) sender).getUniqueId())) {
            sender.sendMessage("Status set");
        } else {
            sender.sendMessage("Invalid status");
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.addAll(configuration.getConfigurationSection("stati.first").getKeys(false));
        }
        if (args.length == 2) {
            completions.addAll(configuration.getConfigurationSection("stati.second").getKeys(false));
        }
        List<String> matches = new ArrayList<>();
        copyPartialMatches(args[args.length - 1], completions, matches);
        return matches;
    }
}
