package de.alphaomega.it.aocommands.commands;

import de.alphaomega.it.aocommands.cmdhandler.Command;
import de.alphaomega.it.aocommands.cmdhandler.CommandArgs;
import org.bukkit.entity.Player;

public class Workbench {

    @Command(
            name = "workbench",
            aliases = {"wb"},
            permission = "aocommands.workbench"
    )
    public void onCommand(final CommandArgs arg) {
        final Player player = arg.getPlayer();
        player.openWorkbench(player.getLocation(), true);
    }
}
