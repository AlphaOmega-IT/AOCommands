package de.alphaomega.it.commands;



import de.alphaomega.it.AOCommands;
import de.alphaomega.it.cmdHandler.Command;
import de.alphaomega.it.cmdHandler.CommandArgs;

import de.alphaomega.it.inventories.EnderchestInv;
import de.alphaomega.it.msgHandler.Message;
import de.alphaomega.it.utils.CheckPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Enderchest {


    private AOCommands pl;

    @Command(
            name = "enderchest",
            aliases = "ec",
            permission = "aocommands.enderchest"
    )
    public void onCommand(final CommandArgs arg) {
        final Player p = arg.getPlayer();
        final String[] args = arg.getArgs();
        final Message msg = new Message(p);

        if (args.length > 1) {
            msg.sendMessage("enderchest-syntax", false, true);
            return;
        }

        if (args.length == 1) {
            if (!CheckPlayer.isOnline(args[0], p)) return;
            final Player target = Bukkit.getPlayer(args[0]);
            if (p.isOp() || p.hasPermission("aocommands.enderchest.*"))
                p.openInventory(target.getEnderChest());
            else
                new EnderchestInv(target.getEnderChest()).getInv(target.getEnderChest(), target);
            return;
        }

        p.openInventory(p.getEnderChest());
    }
}
