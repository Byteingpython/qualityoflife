package de.snowwars.qualityoflife.arena;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ArenaCommand implements CommandExecutor {
    Map<UUID, Location> arenas;
    ArenaManager arenaManager;
    public ArenaCommand(ArenaManager arenaManager) {
        this.arenaManager = arenaManager;
        arenas = new HashMap<>();
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
       if(!(sender instanceof Player)) {
           return false;
       }
       Player player = (Player) sender;
       if(!arenas.containsKey(player.getUniqueId())) {
           arenas.put(player.getUniqueId(), player.getLocation());
           player.sendMessage("Arena center set");
       }
       else {
           player.sendMessage("Arena created");
           arenaManager.addArena(arenas.get(player.getUniqueId()), (int) Math.ceil(player.getLocation().distance(arenas.get(player.getUniqueId()))));
              arenas.remove(player.getUniqueId());
       }

        return true;
    }
}
