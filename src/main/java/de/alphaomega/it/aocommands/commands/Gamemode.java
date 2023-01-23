package de.alphaomega.it.aocommands.commands;

import de.alphaomega.it.aocommands.AOCommands;
import de.alphaomega.it.aocommands.cmdhandler.Command;
import de.alphaomega.it.aocommands.cmdhandler.CommandArgs;
import de.alphaomega.it.aocommands.msghandler.Message;
import de.alphaomega.it.aocommands.utils.CheckPlayer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class Gamemode {

    private final AOCommands aoCommands;

    public Gamemode(final AOCommands aoCommands) {
        this.aoCommands = aoCommands;
    }

    @Command(
            name = "gamemode",
            aliases = {"gm"},
            permission = "aocommands.gamemode",
            inGameOnly = false
    )
    public void onCommand(final CommandArgs arg) {
        final Player player = arg.getPlayer();
        final String[] args = arg.getArgs();
        final Message msg = new Message(player);

        if (!(arg.getSender() instanceof ConsoleCommandSender)) {
            if (args.length > 2 || args.length < 1) {
                msg.sendMessage("gm-syntax", false, true);
                return;
            }

            if (!this.aoCommands.getBaseConfig().getBoolean("allowed_ingame_command")) {
                msg.sendMessage("gmNotAllowedIngame", false, true);
                return;
            }
        }

        if (args.length == 1) {
            setGamemode(player, args, player);
            return;
        }

        if (!CheckPlayer.isOnline(args[1], player)) return;
        final Player target = Bukkit.getPlayer(args[1]);
        setGamemode(player, args, target);
    }

    private boolean isSameGamemode(final Player player, final GameMode gm) {
        final Message msg = new Message(player);
        if (player.getGameMode().equals(gm)) {
            msg.setArgs(List.of(gm.translationKey()));
            msg.sendMessage("alreadyInGM", true, true);
            return true;
        }
        return false;
    }

    private void setGamemode(Player player, final String[] args, final Player target) {
        final String arg0 = args[0].toLowerCase();

        GameMode oldGM = GameMode.SURVIVAL;
        if (player != null)
            oldGM = player.getGameMode();
        Message msg = new Message(player);
        Message msgTarget = null;
        if (target != null) {
            oldGM = target.getGameMode();
            if (player != target)
                msgTarget = new Message(target);
            player = target;
        }

        if (player == null) return;

        switch (arg0) {
            case "s", "survival", "0" -> player.setGameMode(GameMode.SURVIVAL);
            case "c", "creative", "1" -> player.setGameMode(GameMode.CREATIVE);
            case "a", "adventure", "2" -> player.setGameMode(GameMode.ADVENTURE);
            case "spec", "spectator", "3" -> player.setGameMode(GameMode.SPECTATOR);
            default -> {
                msg.setArgs(List.of(arg0));
                msg.sendMessage("gmNotExists", true, true);
                return;
            }
        }
        if (target == null || msgTarget == null) {
            if (isSameGamemode(player, oldGM))
                return;
            msg.setArgs(List.of(player.getGameMode().translationKey()));
            msg.sendMessage("gmChanged", true, true);
            return;
        }

        msgTarget.setArgs(List.of(player.getName(), player.getGameMode().translationKey()));
        msgTarget.sendMessage("gmChangedFrom", true, true);
    }
}
