package de.alphaomega.it.commands;

import de.alphaomega.it.cmdhandler.Command;
import de.alphaomega.it.cmdhandler.CommandArgs;
import de.alphaomega.it.msghandler.Message;
import org.bukkit.entity.Player;

public class Vote {

    @Command(
            name = "vote",
            aliases = {"votes", "votelink"},
            permission = "aocommands.vote"
    )
    public void onCommand(final CommandArgs arg) {
        final Player player = arg.getPlayer();
        final Message msg = new Message(player);
        msg.sendMessage("vote", false, true);
    }
}
