package de.alphaomega.it.commands;

import de.alphaomega.it.cmdHandler.Command;
import de.alphaomega.it.cmdHandler.CommandArgs;
import de.alphaomega.it.inventories.ArmorstandInv;
import org.bukkit.entity.Player;

public class Armorstand {

    @Command(
            name = "armorstand",
            aliases = {"ac"},
            permission = "aocommands.armorstand"
    )
    public void onCommand(final CommandArgs arg) {
        final Player p = arg.getPlayer();
        ArmorstandInv.getInv().open(p);
    }
}
