package de.alphaomega.it.aocommands.commands;

import de.alphaomega.it.aocommands.AOCommands;
import de.alphaomega.it.aocommands.cmdhandler.Command;
import de.alphaomega.it.aocommands.cmdhandler.CommandArgs;
import de.alphaomega.it.aocommands.database.entities.AOSpawn;
import de.alphaomega.it.aocommands.msghandler.Message;
import de.alphaomega.it.aocommands.utils.CheckPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.concurrent.CompletableFuture;

public class Spawn {

    private final AOCommands aoCommands;

    public Spawn(final AOCommands aoCommands) {
        this.aoCommands = aoCommands;
    }

    @Command(
            name = "setspawn",
            permission = "aocommands.setspawn"
    )
    public void onCommandSetSpawn(final CommandArgs arg) {
        final Player player = arg.getPlayer();
        final Location loc = player.getLocation();
        final String serverName = player.getServer().getName();
        final Message msg = new Message(player);

        CompletableFuture.supplyAsync(() -> {
            AOSpawn aoSpawn = this.aoCommands.getAoCommandsAPI().getAoSpawnDao().findByServer(serverName);
            if (aoSpawn == null)
                return new AOSpawn(loc, serverName);
            aoSpawn.setLocation(loc);
            return aoSpawn;
        }).thenApplyAsync(aaoSpawn -> {
            this.aoCommands.getAoCommandsAPI().getAoSpawnDao().update(aaoSpawn);
            return aaoSpawn;
        }).thenAccept(aa -> msg.sendMessage("spawnSet", false, true));
    }

    @Command(
            name = "spawn",
            permission = "aocommands.spawn"
    )
    public void onCommandSpawn(final CommandArgs arg) {
        final Player player = arg.getPlayer();
        final Message msg = new Message(player);
        final String[] args = arg.getArgs();
        final String serverName = player.getServer().getName();

        if (!player.hasPermission("aocommands.spawn.*") && !player.isOp()) {
            if (args.length != 0) {
                msg.sendMessage("spawn-syntax", false, true);
                return;
            }

            CompletableFuture.supplyAsync(() -> this.aoCommands.getAoCommandsAPI().getAoSpawnDao().findByServer(serverName)).thenAccept(aoSpawn -> {
                if (aoSpawn == null) {
                    msg.sendMessage("spawnNotSet", false, true);
                    return;
                }
                teleport(player, aoSpawn);
            });
        }

        if (args.length > 1) {
            msg.sendMessage("spawn-admin-syntax", false, true);
            return;
        }

        CompletableFuture.supplyAsync(() -> this.aoCommands.getAoCommandsAPI().getAoSpawnDao().findByServer(serverName)).thenAccept(aoSpawn -> {
            if (aoSpawn == null) {
                msg.sendMessage("spawnNotSet", false, true);
                return;
            }

            if (args.length == 0) {
                teleport(player, aoSpawn);
                return;
            }

            if (args[0].matches("(all|\\*|@a)")) {
                Bukkit.getOnlinePlayers().forEach(p -> teleport(p, aoSpawn));
                return;
            }

            if (!CheckPlayer.isOnline(args[0], player)) return;
            final Player target = Bukkit.getPlayer(args[0]);
            teleport(target, aoSpawn);
        });
    }

    private void teleport(final Player player, final AOSpawn spawn) {
        final Message msg = new Message(player);
        player.teleportAsync(spawn.getLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
        msg.sendMessage("teleportToSpawn", false, true);
    }
}
