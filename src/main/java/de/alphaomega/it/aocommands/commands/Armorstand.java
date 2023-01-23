package de.alphaomega.it.aocommands.commands;


import de.alphaomega.it.aocommands.AOCommands;
import de.alphaomega.it.aocommands.cmdhandler.Command;
import de.alphaomega.it.aocommands.cmdhandler.CommandArgs;
import de.alphaomega.it.aocommands.inventories.ArmorstandInv;
import org.bukkit.entity.Player;

public class Armorstand {

    private final AOCommands aoCommands;

    public Armorstand(final AOCommands aoCommands) {
        this.aoCommands = aoCommands;
    }

    @Command(
            name = "armorstand",
            aliases = {"ac"},
            permission = "aocommands.armorstand"
    )
    public void onCommand(final CommandArgs arg) {
        final Player player = arg.getPlayer();
        ArmorstandInv.getInv(aoCommands).open(player);
    }
}
