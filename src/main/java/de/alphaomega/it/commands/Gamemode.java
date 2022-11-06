package de.alphaomega.it.commands;

import de.alphaomega.it.AOCommands;
import de.alphaomega.it.cmdHandler.Command;
import de.alphaomega.it.cmdHandler.CommandArgs;
import de.alphaomega.it.msgHandler.Message;
import de.alphaomega.it.utils.CheckPlayer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public record Gamemode(AOCommands pl) {

    @Command(
            name = "gamemode",
            aliases = {"gm"},
            permission = "aocommands.gamemode",
            inGameOnly = false
    )
    public void onCommand(final CommandArgs arg) {
        final Player p = arg.getPlayer();
        final String[] args = arg.getArgs();
        final Message msg = new Message(p);

        if (!(arg.getSender() instanceof ConsoleCommandSender)) {
            if (args.length > 2 || args.length < 1) {
                msg.sendMessage("gm-syntax", false, true);
                return;
            }

            if (!pl.getBaseConfig().getBoolean("allowed_ingame_command")) {
                msg.sendMessage("gmNotAllowedIngame", false, true);
                return;
            }
        }

        if (args.length == 1) {
            setGamemode(p, args, null);
            return;
        }

        if (!CheckPlayer.isOnline(args[1], p)) return;
        final Player target = Bukkit.getPlayer(args[1]);
        setGamemode(p, args, target);
    }

    private boolean isSameGamemode(final Player p, final GameMode gm) {
        final Message msg = new Message(p);
        if (p.getGameMode().equals(gm)) {
            msg.setArgs(List.of(gm.translationKey()));
            msg.sendMessage("alreadyInGM", true, true);
            return true;
        }
        return false;
    }

    private void setGamemode(final Player p, final String[] args, final Player target) {
        final String arg0 = args[0].toLowerCase();
        GameMode oldGM = p.getGameMode();
        Message msg = new Message(p);
        Message msgTarget = null;
        if (target != null) {
            oldGM = target.getGameMode();
            if (p != target)
                msgTarget = new Message(target);
        }

        switch (arg0) {
            case "s", "survival", "0" -> p.setGameMode(GameMode.SURVIVAL);
            case "c", "creative", "1" -> p.setGameMode(GameMode.CREATIVE);
            case "a", "adventure", "2" -> p.setGameMode(GameMode.ADVENTURE);
            case "spec", "spectator", "3" -> p.setGameMode(GameMode.SPECTATOR);
            default -> {
                msg.setArgs(List.of(arg0));
                msg.sendMessage("gmNotExists", true, true);
                return;
            }
        }
        if (target == null || msgTarget == null) {
            if (isSameGamemode(p, oldGM))
                return;
            msg.setArgs(List.of(p.getGameMode().translationKey()));
            msg.sendMessage("gmChanged", true, true);
            return;
        }

        msgTarget.setArgs(List.of(p.getName(), p.getGameMode().translationKey()));
        msgTarget.sendMessage("gmChangedFrom", true, true);
    }
}
