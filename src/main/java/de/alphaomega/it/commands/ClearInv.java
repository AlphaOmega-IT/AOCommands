package de.alphaomega.it.commands;

import de.alphaomega.it.cmdHandler.Command;
import de.alphaomega.it.cmdHandler.CommandArgs;
import de.alphaomega.it.msgHandler.Message;
import de.alphaomega.it.utils.CheckPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class ClearInv {

    @Command(
            name = "clearinv",
            aliases = {"ci", "clearinventory"},
            permission = "aocommands.clearinv"
    )
    public void onCommand(final CommandArgs arg) {
        final Player p = arg.getPlayer();
        final String[] args = arg.getArgs();
        final Message msg = new Message(p);

        if (args.length > 1) {
            msg.sendMessage("clearinv-syntax", false, true);
            return;
        }

        if (args.length == 1) {
            if (!CheckPlayer.isOnline(args[0], p)) return;
            final Player target = Bukkit.getPlayer(args[0]);

            target.getInventory().clear();
            target.getEquipment().clear();

            if (p != target) {
                msg.setArgs(List.of(target.getName()));
                msg.sendMessage("targetInvCleared", true, false);

            final Message msgTarget = new Message(target);
            msgTarget.setArgs(List.of(p.getName()));
            msg.sendMessage("InvClearedBy", true, true);
            return;
            }
        }

        p.getInventory().clear();
        p.getEquipment().clear();
        msg.sendMessage("invCleared", false, true);
    }
}
