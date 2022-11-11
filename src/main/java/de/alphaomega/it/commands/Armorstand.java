package de.alphaomega.it.commands;


import de.alphaomega.it.AOCommands;
import de.alphaomega.it.cmdHandler.Command;
import de.alphaomega.it.cmdHandler.CommandArgs;
import de.alphaomega.it.inventories.ArmorstandInv;
import org.bukkit.entity.Player;

public record Armorstand(AOCommands pl) {

    @Command(
            name = "armorstand",
            aliases = {"ac"},
            permission = "aocommands.armorstand"
    )
    public void onCommand(final CommandArgs arg) {
        final Player p = arg.getPlayer();
        new ArmorstandInv().getInv(pl).open(p);
    }
}
