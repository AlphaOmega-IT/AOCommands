package de.alphaomega.it.commands;

import de.alphaomega.it.cmdhandler.Command;
import de.alphaomega.it.cmdhandler.CommandArgs;
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
