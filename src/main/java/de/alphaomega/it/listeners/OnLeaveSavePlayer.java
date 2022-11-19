package de.alphaomega.it.listeners;

import de.alphaomega.it.AOCommands;
import de.alphaomega.it.database.entities.AOPlayer;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.concurrent.CompletableFuture;

public class OnLeaveSavePlayer implements Listener {

    private final AOCommands aoCommands;

    public OnLeaveSavePlayer(final AOCommands aoCommands) {
        this.aoCommands = aoCommands;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerQuit(final PlayerQuitEvent e) {
        final Player p = e.getPlayer();
        e.quitMessage(Component.empty()); //will be handled in OnLeave.class

        CompletableFuture.supplyAsync(() -> {
            AOPlayer aoP = this.aoCommands.getAoCommandsAPI().getPlayers().get(p.getUniqueId());
            aoP.setPlaytime(p.getPlayerTime());
            return aoP;
        }).thenApplyAsync(aoPlayer -> {
            this.aoCommands.getAoCommandsAPI().getAoPlayers().dbUserUpdate(aoPlayer);
            return aoPlayer;
        }).thenAccept(aoPlayer -> this.aoCommands.getAoCommandsAPI().getPlayers().remove(p.getUniqueId())).join();
    }
}
