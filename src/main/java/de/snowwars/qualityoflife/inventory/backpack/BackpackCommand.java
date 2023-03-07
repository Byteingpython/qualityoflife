package de.snowwars.qualityoflife.inventory.backpack;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class BackpackCommand implements CommandExecutor {
    BackpackManager manager;

    public BackpackCommand(BackpackManager manager, JavaPlugin plugin) {
        if (plugin == null) throw new IllegalArgumentException("plugin cannot be null");
        plugin.getCommand("backpack").setExecutor(this);
        this.manager = manager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            manager.openBackpack((Player) sender);
            return true;
        } else {
            sender.sendMessage("You have to be a player to use this command");
            return false;
        }
    }
}
