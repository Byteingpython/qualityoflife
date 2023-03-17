package de.snowwars.qualityoflife.alert;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.bukkit.util.StringUtil.copyPartialMatches;

public class AlertCommand implements TabExecutor {
    DurabilityAlert durabilityAlert;

    public AlertCommand(JavaPlugin plugin) {
        durabilityAlert= new DurabilityAlert(plugin);
        plugin.getCommand("alert").setExecutor(this);
        plugin.getCommand("alert").setTabCompleter(this);
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player)) {
            return false;
        }
        if(args.length <= 1) {
            return false;
        }
        if(args[0].equalsIgnoreCase("durability")) {
            durabilityAlert.setAlert((Player) sender, args[1]);
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> completions= new ArrayList<>();
        if(args.length == 1) {
            completions=List.of("durability");
        }
        List<String> matches = new ArrayList<>();
        copyPartialMatches(args[args.length - 1], completions, matches);
        return matches;
    }
}
