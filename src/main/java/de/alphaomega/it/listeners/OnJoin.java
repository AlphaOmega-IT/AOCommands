package de.alphaomega.it.listeners;

import de.alphaomega.it.utils.CheckPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class OnJoin implements Listener {

    @EventHandler
    public void onJoin(final PlayerJoinEvent e) {
        final Player p = e.getPlayer();
        CheckPlayer.hideAllPlayers(p.getUniqueId());
    }
}
