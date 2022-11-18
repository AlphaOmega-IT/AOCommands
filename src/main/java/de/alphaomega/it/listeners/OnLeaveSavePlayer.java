package de.alphaomega.it.listeners;

import de.alphaomega.it.api.AOCommandsAPI;
import de.alphaomega.it.database.entities.AOPlayer;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.concurrent.CompletableFuture;

public record OnLeaveSavePlayer(AOCommandsAPI api) implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerQuit(final PlayerQuitEvent e) {
        final Player p = e.getPlayer();
        e.quitMessage(Component.empty()); //will be handled in OnLeave.class

        CompletableFuture.supplyAsync(() -> {
            AOPlayer aoP = AOCommandsAPI.players.get(p.getUniqueId());
            aoP.setPlaytime(p.getPlayerTime());
            return aoP;
        }).thenApplyAsync(aoPlayer -> {
            api.getAoPlayers().dbUserUpdate(aoPlayer);
            return aoPlayer;
        }).thenAccept(aoPlayer -> AOCommandsAPI.players.remove(p.getUniqueId())).join();
    }
}
