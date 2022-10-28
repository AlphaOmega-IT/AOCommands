package de.alphaomega.it.utils;

import de.alphaomega.it.AOCommands;
import de.alphaomega.it.msgHandler.Message;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class CheckPlayer {

    @Getter
    private static final Set<UUID> vanishedPlayers = new LinkedHashSet<>();

    public static boolean isOnline(final String targetName, final Player p) {
        final Player target = Bukkit.getPlayer(targetName);
        if (target != null) return true;

        final Message msg = new Message(p);
        msg.setArgs(List.of(targetName));
        msg.sendMessage("playerNotOnline", true, true);
        return false;
    }

    public static boolean isVanished(final UUID uniqueId) {
        return vanishedPlayers.contains(uniqueId);
    }

    public static void setVanished(final UUID uniqueId, final boolean vanish) {
        if (vanish) {
            if (!isVanished(uniqueId))
                vanishedPlayers.add(uniqueId);
        } else {
            if (isVanished(uniqueId))
                vanishedPlayers.remove(uniqueId);
        }
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getUniqueId().equals(uniqueId)) continue;
            if (vanish) p.hidePlayer(AOCommands.getInstance(), Objects.requireNonNull(Bukkit.getPlayer(uniqueId)));
            else p.showPlayer(AOCommands.getInstance(), Objects.requireNonNull(Bukkit.getPlayer(uniqueId)));
        }
    }

    public static void hideAllPlayers(final UUID uniqueId) {
        try {
            vanishedPlayers.forEach(uuid -> Objects.requireNonNull(Bukkit.getPlayer(uniqueId)).hidePlayer(AOCommands.getInstance(), Objects.requireNonNull(Bukkit.getPlayer(uuid))));
        } catch (final NullPointerException ignored) {}
    }
}
