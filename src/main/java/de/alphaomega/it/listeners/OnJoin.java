package de.alphaomega.it.listeners;

import de.alphaomega.it.msgHandler.Message;
import de.alphaomega.it.utils.CheckPlayer;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;


public class OnJoin implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(final PlayerJoinEvent e) {
        final Player p = e.getPlayer();
        final Message msg = new Message(p);

        msg.setArgs(List.of(p.getName()));
        e.joinMessage(MiniMessage.miniMessage().deserialize(msg.showMessage("joinMessage", true, false)));
        CheckPlayer.hideAllPlayers(p.getUniqueId());
    }
}
