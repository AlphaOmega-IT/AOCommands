package de.alphaomega.it.utils;


import de.alphaomega.it.AOCommands;
import de.alphaomega.it.msghandler.Message;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class CheckPlayer {

    public static boolean isOnline(final String targetName, final Player p) {
        final Player target = Bukkit.getPlayer(targetName);
        if (target != null) return true;

        final Message msg = new Message(p);
        msg.setArgs(List.of(targetName));
        msg.sendMessage("playerNotOnline", true, true);
        return false;
    }

    public static boolean isVanished(final AOCommands aoCommands, final UUID uniqueId) {
        return aoCommands.getVanishedPlayers().contains(uniqueId);
    }

    public static void setVanished(final AOCommands aoCommands, final UUID uniqueId, final boolean vanish) {
        if (vanish) {
            if (!isVanished(aoCommands, uniqueId))
                aoCommands.getVanishedPlayers().add(uniqueId);
        } else {
            if (isVanished(aoCommands, uniqueId))
                aoCommands.getVanishedPlayers().remove(uniqueId);
        }
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getUniqueId().equals(uniqueId)) continue;
            if (vanish) p.hidePlayer(aoCommands, Objects.requireNonNull(Bukkit.getPlayer(uniqueId)));
            else p.showPlayer(aoCommands, Objects.requireNonNull(Bukkit.getPlayer(uniqueId)));
        }
    }

    public static void hideAllPlayers(final AOCommands aoCommands, final UUID uniqueId) {
        try {
            aoCommands.getVanishedPlayers().forEach(uuid -> Objects.requireNonNull(Bukkit.getPlayer(uniqueId)).hidePlayer(aoCommands, Objects.requireNonNull(Bukkit.getPlayer(uuid))));
        } catch (final NullPointerException ignored) {}
    }
}
