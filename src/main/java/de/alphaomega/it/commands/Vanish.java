package de.alphaomega.it.commands;

import de.alphaomega.it.cmdHandler.Command;
import de.alphaomega.it.cmdHandler.CommandArgs;
import de.alphaomega.it.msgHandler.Message;
import de.alphaomega.it.utils.CheckPlayer;
import org.bukkit.entity.Player;

public class Vanish {

    @Command(
            name = "vanish",
            aliases = {"v"},
            permission = "aocommands.vanish"
    )
    public void onCommand(final CommandArgs arg) {
        final Player p = arg.getPlayer();
        final Message msg = new Message(p);

        if (CheckPlayer.isVanished(p.getUniqueId())) {
            CheckPlayer.setVanished(p.getUniqueId(), false);
            msg.sendMessage("nonVanish", false, true);
        } else {
            CheckPlayer.setVanished(p.getUniqueId(), true);
            msg.sendMessage("setVanish", false, true);
        }
    }
}
