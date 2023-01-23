package de.alphaomega.it.aocommands.commands;

import de.alphaomega.it.aocommands.cmdhandler.Command;
import de.alphaomega.it.aocommands.cmdhandler.CommandArgs;
import de.alphaomega.it.aocommands.msghandler.Message;
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
