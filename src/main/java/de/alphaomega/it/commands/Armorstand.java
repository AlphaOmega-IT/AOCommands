package de.alphaomega.it.commands;


import de.alphaomega.it.AOCommands;
import de.alphaomega.it.cmdhandler.Command;
import de.alphaomega.it.cmdhandler.CommandArgs;
import de.alphaomega.it.inventories.ArmorstandInv;
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
