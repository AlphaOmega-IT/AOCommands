package de.alphaomega.it.commands;

import de.alphaomega.it.cmdHandler.Command;
import de.alphaomega.it.cmdHandler.CommandArgs;
import de.alphaomega.it.msgHandler.Message;
import de.alphaomega.it.utils.CheckPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Utility;
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
        final Player p = arg.getPlayer();
        final String[] args = arg.getArgs();
        final Message msg = new Message(p);

        if (args.length > 1) {
            msg.sendMessage("heal-syntax", false, true);
            return;
        }

        if (args.length == 0) {
            p.setHealth(Objects.requireNonNull(p.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getDefaultValue());
            msg.sendMessage("playerGotHealed", false, true);
            return;
        }

        if (!CheckPlayer.isOnline(args[0], p)) return;
        final Player target = Bukkit.getPlayer(args[0]);

        if (target != p) {
            msg.setArgs(List.of(target.getName()));
            msg.sendMessage("playerHealedTarget", true, true);
        }

        final Message msgTarget = new Message(target);
        msgTarget.sendMessage("targetGotHealed", false, true);
        target.setHealth(Objects.requireNonNull(target.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getDefaultValue());
    }
}
