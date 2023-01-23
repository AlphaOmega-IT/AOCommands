package de.alphaomega.it.aocommands.commands;

import de.alphaomega.it.aocommands.cmdhandler.Command;
import de.alphaomega.it.aocommands.cmdhandler.CommandArgs;
import de.alphaomega.it.aocommands.msghandler.Message;
import de.alphaomega.it.aocommands.utils.CheckPlayer;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;

public class Heal {

    @Command(
            name = "heal",
            aliases = {"heilen"},
            permission = "aocommands.heal"
    )
    public void onCommand(final CommandArgs arg) {
        final Player player = arg.getPlayer();
        final String[] args = arg.getArgs();
        final Message msg = new Message(player);

        if (args.length > 1) {
            msg.sendMessage("heal-syntax", false, true);
            return;
        }

        if (args.length == 0) {
            player.setHealth(Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getDefaultValue());
            msg.sendMessage("playerGotHealed", false, true);
            return;
        }

        if (!CheckPlayer.isOnline(args[0], player)) return;
        final Player target = Bukkit.getPlayer(args[0]);

        if (target != player) {
            msg.setArgs(List.of(target.getName()));
            msg.sendMessage("playerHealedTarget", true, true);
        }

        final Message msgTarget = new Message(target);
        msgTarget.sendMessage("targetGotHealed", false, true);
        target.setHealth(Objects.requireNonNull(target.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getDefaultValue());
    }
}
