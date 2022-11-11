package de.alphaomega.it.commands;

import de.alphaomega.it.AOCommands;
import de.alphaomega.it.cmdHandler.Command;
import de.alphaomega.it.cmdHandler.CommandArgs;
import de.alphaomega.it.inventories.InvseeInv;
import de.alphaomega.it.msgHandler.Message;
import de.alphaomega.it.utils.CheckPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public record Invsee(AOCommands pl) {

    @Command(
            name = "invsee",
            permission = "aocommands.invsee"
    )
    public void onCommand(final CommandArgs arg) {
        final Player p = arg.getPlayer();
        final String[] args = arg.getArgs();
        final Message msg = new Message(p);

        if (args.length != 1) {
            msg.sendMessage("invsee-syntax", false, true);
            return;
        }

        if (!CheckPlayer.isOnline(args[0], p)) return;
        final Player target = Bukkit.getPlayer(args[0]);
        new InvseeInv(pl, target).getInv(p).open(p);
    }
}
