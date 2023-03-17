package de.snowwars.qualityoflife.alert;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public class AlertUtil {
    public static void alert(@NotNull AlertType type, @NotNull Player player) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.RED + type.getMessage()));
    }
    public static void alert(@NotNull AlertType type, @NotNull Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.RED + type.getMessage()+": "+message));
    }
}
