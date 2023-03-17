package de.snowwars.qualityoflife.alert;

import org.bukkit.entity.Player;

import java.util.List;

public interface Alert {
    void setAlert(Player player, String data);
    List<String> getAlertCompletions();
    AlertType getAlertType();
}
