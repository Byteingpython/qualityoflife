package de.snowwars.qualityoflife.arena;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ArenaManager implements Listener {
    Map<Location, Integer> arenas = new HashMap<>();
    JavaPlugin plugin;
    public ArenaManager(JavaPlugin plugin) {
        if(new File(plugin.getDataFolder(), "arenas.yml").exists()) {
            try {
                FileInputStream stream = new FileInputStream(new File(plugin.getDataFolder(), "arenas.yml"));
                BukkitObjectInputStream objectInputStream = new BukkitObjectInputStream(stream);
                arenas = (Map<Location, Integer>) objectInputStream.readObject();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        }
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    public void saveArenas() {
        try {
            FileOutputStream stream = new FileOutputStream(new File(plugin.getDataFolder(), "arenas.yml"));
            BukkitObjectOutputStream objectOutputStream = new BukkitObjectOutputStream(stream);
            objectOutputStream.writeObject(arenas);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @EventHandler
    public void onMove(PlayerMoveEvent event) {
       if(!isArena(event.getTo())) return;
         if(!isArena(event.getFrom())) {
              event.getPlayer().sendMessage("You entered an arena");
         }
    }
    @EventHandler(priority = org.bukkit.event.EventPriority.HIGHEST)
    public void onDeath(PlayerDeathEvent event) {
        if(!isArena(event.getEntity().getLocation())) return;
        event.setNewTotalExp(event.getEntity().getTotalExperience());
        event.setKeepInventory(true);
        event.getEntity().sendMessage("You died in an arena");
    }
    public void addArena(Location location, int radius) {
        arenas.put(location, radius);
    }
    public void removeArena(Location location) {
        arenas.remove(location);
    }
    public boolean isArena(Location location) {
        for(Map.Entry<Location, Integer> entry: arenas.entrySet()) {
            if(entry.getKey().getWorld().equals(location.getWorld())) {
                if(entry.getKey().distance(location) <= entry.getValue()) {
                    return true;
                }
            }
        }
        return false;
    }
}
