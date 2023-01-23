package de.alphaomega.it.aocommands.listeners;

import de.alphaomega.it.aocommands.AOCommands;
import de.alphaomega.it.aocommands.database.entities.AOSpawn;
import de.alphaomega.it.aocommands.msghandler.Message;
import de.alphaomega.it.aocommands.utils.CheckPlayer;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.List;
import java.util.concurrent.CompletableFuture;


public class OnJoin implements Listener {

    private final AOCommands aoCommands;
    private AOSpawn spawn;

    public OnJoin(final AOCommands aoCommands) {
        this.aoCommands = aoCommands;
    }

    @EventHandler
    public void onLogin(final PlayerLoginEvent event) {
        final Player player = event.getPlayer();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final Message msg = new Message(player);

        msg.setArgs(List.of(player.getName()));
        event.joinMessage(MiniMessage.miniMessage().deserialize(msg.showMessage("joinMessage", true, false)));
        CheckPlayer.hideAllPlayers(this.aoCommands, player.getUniqueId());

        if (this.aoCommands.getBaseConfig().getBoolean("teleport_player_to_spawn")) {
            CompletableFuture.supplyAsync(() -> this.aoCommands.getAoCommandsAPI().getAoSpawnDao().findByServer(player.getServer().getName())).thenAcceptAsync(aoSpawn -> {
                spawn = aoSpawn;
            }).thenAccept(aoSpawn -> {
                if (this.spawn == null)
                    msg.sendMessage("spawnTeleportNotAllowed", false, true);
                else
                    player.teleportAsync(spawn.getLocation());
            });
        }
    }
}
