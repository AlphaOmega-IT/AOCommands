package de.alphaomega.it.commands;

import de.alphaomega.it.AOCommands;
import de.alphaomega.it.cmdhandler.Command;
import de.alphaomega.it.cmdhandler.CommandArgs;
import de.alphaomega.it.inventories.InvseeInv;
import de.alphaomega.it.msghandler.Message;
import de.alphaomega.it.utils.CheckPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Invsee {

    private final AOCommands aoCommands;

    public Invsee(final AOCommands aoCommands) {
        this.aoCommands = aoCommands;
    }

    @Command(
            name = "invsee",
            permission = "aocommands.invsee"
    )
    public void onCommand(final CommandArgs arg) {
        final Player player = arg.getPlayer();
        final String[] args = arg.getArgs();
        final Message msg = new Message(player);

        if (args.length != 1) {
            msg.sendMessage("invsee-syntax", false, true);
            return;
        }

        if (!CheckPlayer.isOnline(args[0], player)) return;
        final Player target = Bukkit.getPlayer(args[0]);
        new InvseeInv(this.aoCommands, target).getInv(player).open(player);
    }
}
