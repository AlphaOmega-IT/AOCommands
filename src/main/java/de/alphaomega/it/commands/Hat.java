package de.alphaomega.it.commands;

import de.alphaomega.it.cmdhandler.Command;
import de.alphaomega.it.cmdhandler.CommandArgs;
import de.alphaomega.it.msghandler.Message;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Hat {

    @Command(
            name = "hat",
            aliases = {"hut"},
            permission = "aocommands.hat"
    )
    public void onCommand(final CommandArgs arg) {
        final Player player = arg.getPlayer();
        final Message msg = new Message(player);
        final ItemStack itemMainHand = player.getInventory().getItemInMainHand();
        final ItemStack itemHelmet = player.getInventory().getHelmet();

        if (!itemMainHand.getType().isItem() || itemMainHand.getType().isAir()) {
            msg.sendMessage("noValidItemInHand-hat", false, true);
            return;
        }

        player.getInventory().setItemInMainHand(itemHelmet);
        player.getInventory().setHelmet(itemMainHand);
        player.updateInventory();

        msg.sendMessage("hatCommandExecuted", false, true);
    }
}
