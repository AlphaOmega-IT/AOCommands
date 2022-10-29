package de.alphaomega.it.commands;

import de.alphaomega.it.cmdHandler.Command;
import de.alphaomega.it.cmdHandler.CommandArgs;
import org.bukkit.entity.Player;

public class Workbench {

    @Command(
            name = "workbench",
            aliases = {"wb"},
            permission = "aocommands.workbench"
    )
    public void onCommand(final CommandArgs arg) {
        final Player p = arg.getPlayer();
        p.openWorkbench(p.getLocation(), true);
    }
}
