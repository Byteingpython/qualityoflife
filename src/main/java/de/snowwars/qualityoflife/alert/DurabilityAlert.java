package de.snowwars.qualityoflife.alert;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DurabilityAlert implements Listener, Alert {

    protected final NamespacedKey key;
    JavaPlugin plugin;

    public DurabilityAlert(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
        key = new NamespacedKey(plugin, "durability");
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (event.getPlayer().getInventory().getItemInMainHand() == null) return;
        if(processItem(event.getPlayer().getInventory().getItemInMainHand(), event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event){
        if (!(event.getDamager() instanceof Player)) return;
        Player player = (Player) event.getDamager();
        if (player.getInventory().getItemInMainHand() == null) return;
        if(processItem(player.getInventory().getItemInMainHand(), player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event){
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        if (player.getInventory().getItemInMainHand() == null) return;
        for(ItemStack itemStack : player.getInventory().getArmorContents()) {
            if(itemStack == null) continue;
            processItem(itemStack, player);
        }
    }

    private Boolean processItem(@NotNull ItemStack itemStack, @NotNull Player player) {
        if (!itemStack.hasItemMeta()|| !(itemStack.getItemMeta() instanceof Damageable)) return false;
        if (getDurability(itemStack) != null) {
            if (getDurability(itemStack) <= getDurabilityAlertThreshold(itemStack)) {
                String name;
                if(itemStack.getItemMeta().hasDisplayName()) {
                    name = itemStack.getItemMeta().getDisplayName();
                } else {
                    name = itemStack.getType().name();
                }
                AlertUtil.alert(AlertType.DURABILITY, player, name);
                return true;
            }
        }
        return false;
    }


    @Override
    public void setAlert(@NotNull Player player, @NotNull String data) {
        if (player == null || data == null) return;
        setAlert(player, Integer.parseInt(data));

    }

    public void setAlert(@NotNull ItemStack stack, @NotNull Integer data) {
        if (stack == null || data == null) return;
        if (stack.hasItemMeta()) {
            if (stack.getItemMeta() instanceof Damageable) {
                ItemMeta meta = stack.getItemMeta();
                meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, data);
                stack.setItemMeta(meta);
            }
        }
    }

    public void setAlert(@NotNull Player player, @NotNull Integer data) {
        if (player == null || data == null) return;
        if (player.getInventory().getItemInMainHand() != null) {
            ItemStack item = player.getInventory().getItemInMainHand();
            setAlert(item, data);
            return;
        }
        if (player.getInventory().getItemInOffHand() != null) {
            ItemStack item = player.getInventory().getItemInOffHand();
            setAlert(item, data);
        }
    }

    @Override
    public List<String> getAlertCompletions() {
        return List.of();
    }

    @Override
    public AlertType getAlertType() {
        return AlertType.DURABILITY;
    }

    private static Integer getDurability(@NotNull ItemStack item) {
        if (item == null) return 0;
        if (item.hasItemMeta()) {
            if (item.getItemMeta() instanceof Damageable) {
                Damageable meta = (Damageable) item.getItemMeta();
                Integer durability = item.getType().getMaxDurability() - meta.getDamage();
                return durability;
            }
        }
        return 0;
    }

    @NotNull
    private Integer getDurabilityAlertThreshold(ItemStack item) {
        if (item == null) return 0;
        if (item.hasItemMeta()) {
            if (item.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.INTEGER)) {
                return item.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.INTEGER);
            }
        }
        return 0;
    }

    private void deleteDurabilityAlertThreshold(ItemStack item) {
        if (item == null) return;
        if (item.hasItemMeta()) {
            if (item.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.INTEGER)) {
                ItemMeta meta=item.getItemMeta();
                meta.getPersistentDataContainer().remove(key);
                item.setItemMeta(meta);
            }
        }
    }

}
