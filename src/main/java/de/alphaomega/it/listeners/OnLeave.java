package de.alphaomega.it.listeners;

import de.alphaomega.it.msghandler.Message;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;

public class OnLeave implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLeave(final PlayerQuitEvent e) {
        final Player p = e.getPlayer();
        final Message msg = new Message(p);

        msg.setArgs(List.of(p.getName()));
        e.quitMessage(MiniMessage.miniMessage().deserialize(msg.showMessage("leaveMessage", true, false)));
    }
}
