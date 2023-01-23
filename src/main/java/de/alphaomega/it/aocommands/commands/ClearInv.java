package de.alphaomega.it.aocommands.commands;

import de.alphaomega.it.aocommands.cmdhandler.Command;
import de.alphaomega.it.aocommands.cmdhandler.CommandArgs;
import de.alphaomega.it.aocommands.msghandler.Message;
import de.alphaomega.it.aocommands.utils.CheckPlayer;
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
        final Player player = arg.getPlayer();
        final String[] args = arg.getArgs();
        final Message msg = new Message(player);

        if (args.length > 1) {
            msg.sendMessage("clearinv-syntax", false, true);
            return;
        }

        if (args.length == 1) {
            if (!CheckPlayer.isOnline(args[0], player)) return;
            final Player target = Bukkit.getPlayer(args[0]);

            target.getInventory().clear();
            target.getEquipment().clear();

            if (player != target) {
                msg.setArgs(List.of(target.getName()));
                msg.sendMessage("targetInvCleared", true, false);

                final Message msgTarget = new Message(target);
                msgTarget.setArgs(List.of(player.getName()));
                msg.sendMessage("InvClearedBy", true, true);
                return;
            }
        }

        player.getInventory().clear();
        player.getEquipment().clear();
        msg.sendMessage("invCleared", false, true);
    }
}
