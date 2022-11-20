package de.alphaomega.it.listeners;

import de.alphaomega.it.AOCommands;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

public class OnJoinInitPlayer implements Listener {

    private final AOCommands aoCommands;

    public OnJoinInitPlayer(final AOCommands aoCommands) {
        this.aoCommands = aoCommands;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerLogin(final PlayerLoginEvent e) {
        final Player p = e.getPlayer();
        this.aoCommands.getAoCommandsAPI().getAoPlayers().createPlayer(p);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(final PlayerJoinEvent e) {
        e.joinMessage(Component.empty()); //will be handled in OnJoin.class
    }
}
