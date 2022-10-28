package de.alphaomega.it.commands;

import de.alphaomega.it.cmdHandler.Command;
import de.alphaomega.it.cmdHandler.CommandArgs;
import de.alphaomega.it.msgHandler.Message;
import org.bukkit.entity.Player;

public class Vote {

    @Command(
            name = "vote",
            aliases = {"votes", "votelink"},
            permission = "aocommands.vote"
    )
    public void onCommand(final CommandArgs arg) {
        final Player p = arg.getPlayer();
        final Message msg = new Message(p);
        msg.sendMessage("vote", false, true);
    }
}
