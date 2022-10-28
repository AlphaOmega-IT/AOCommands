package de.alphaomega.it.utils;

import de.alphaomega.it.msgHandler.Message;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class CheckPlayer {

    public static boolean isOnline(final String targetName, final Player p) {
        final Player target = Bukkit.getPlayer(targetName);
        if (target != null) return true;

        final Message msg = new Message(p);
        msg.setArgs(List.of(targetName));
        msg.sendMessage("playerNotOnline", true, true);
        return false;
    }
}
