package de.alphaomega.it.aocommands.commands;

import de.alphaomega.it.aocommands.AOCommands;
import de.alphaomega.it.aocommands.cmdhandler.Command;
import de.alphaomega.it.aocommands.cmdhandler.CommandArgs;
import de.alphaomega.it.aocommands.msghandler.Message;
import de.alphaomega.it.aocommands.utils.CheckPlayer;
import org.bukkit.entity.Player;

public class Vanish {

    private final AOCommands aoCommands;

    public Vanish(final AOCommands aoCommands) {
        this.aoCommands = aoCommands;
    }

    @Command(
            name = "vanish",
            aliases = {"v"},
            permission = "aocommands.vanish"
    )
    public void onCommand(final CommandArgs arg) {
        final Player player = arg.getPlayer();
        final Message msg = new Message(player);

        if (CheckPlayer.isVanished(this.aoCommands, player.getUniqueId())) {
            CheckPlayer.setVanished(this.aoCommands, player.getUniqueId(), false);
            msg.sendMessage("nonVanish", false, true);
        } else {
            CheckPlayer.setVanished(this.aoCommands, player.getUniqueId(), true);
            msg.sendMessage("setVanish", false, true);
        }
    }
}
