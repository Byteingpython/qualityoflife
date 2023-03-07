package de.snowwars.qualityoflife.block;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Vine implements Listener {
    // Place a vine under the vine you are clicking on
    public Vine(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getClickedBlock().getType() == Material.VINE) {
            if (event.getPlayer().getInventory().getItemInMainHand() == null) return;
            if (event.getPlayer().isSneaking()) return;
            if (event.getPlayer().getInventory().getItemInMainHand().getType() != Material.VINE) return;
            Integer i = 0;
            Block vine = event.getClickedBlock().getRelative(0, -1, 0);
            while (event.getClickedBlock().getRelative(0, i, 0).getType() == Material.VINE) {
                vine = event.getClickedBlock().getRelative(0, i - 1, 0);
                i--;
            }
            if (vine.getType() != Material.AIR) return;
            if (vine.getType() != Material.AIR) return;
            vine.setType(Material.VINE);
            BlockState state = vine.getState();
            state.setBlockData(event.getClickedBlock().getBlockData());
            state.update();
            event.getPlayer().getInventory().getItemInMainHand().setAmount(event.getPlayer().getInventory().getItemInMainHand().getAmount() - 1);
            // Play vine place sound
            event.getPlayer().playSound(event.getPlayer().getLocation(), "minecraft:block.vine.place", 1, 1);
        }
    }
}
