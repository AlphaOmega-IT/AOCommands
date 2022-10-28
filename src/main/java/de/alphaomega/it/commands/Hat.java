package de.alphaomega.it.commands;

import de.alphaomega.it.cmdHandler.Command;
import de.alphaomega.it.cmdHandler.CommandArgs;
import de.alphaomega.it.msgHandler.Message;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Hat {

    @Command(
            name = "hat",
            aliases = {"hut"},
            permission = "aocommands.hat"
    )
    public void onCommand(final CommandArgs arg) {
        final Player p = arg.getPlayer();
        final Message msg = new Message(p);
        final ItemStack iSMainHand = p.getInventory().getItemInMainHand();
        final ItemStack iSHelmet = p.getInventory().getHelmet();

        if (!iSMainHand.getType().isItem() || iSMainHand.getType().isAir()) {
            msg.sendMessage("noValidItemInHand-hat", false, true);
            return;
        }

        p.getInventory().setItemInMainHand(iSHelmet);
        p.getInventory().setHelmet(iSMainHand);
        p.updateInventory();

        msg.sendMessage("hatCommandExecuted", false, true);
    }
}
