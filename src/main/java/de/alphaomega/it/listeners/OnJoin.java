package de.alphaomega.it.listeners;

import de.alphaomega.it.AOCommands;
import de.alphaomega.it.msghandler.Message;
import de.alphaomega.it.utils.CheckPlayer;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;


public class OnJoin implements Listener {

    private final AOCommands aoCommands;

    public OnJoin(final AOCommands aoCommands) {
        this.aoCommands = aoCommands;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final Message msg = new Message(player);

        msg.setArgs(List.of(player.getName()));
        event.joinMessage(MiniMessage.miniMessage().deserialize(msg.showMessage("joinMessage", true, false)));
        CheckPlayer.hideAllPlayers(this.aoCommands, player.getUniqueId());
    }
}
