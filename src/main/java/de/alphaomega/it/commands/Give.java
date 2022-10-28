package de.alphaomega.it.commands;

import de.alphaomega.it.cmdHandler.Command;
import de.alphaomega.it.cmdHandler.CommandArgs;
import de.alphaomega.it.msgHandler.Message;
import de.alphaomega.it.utils.CheckPlayer;
import de.alphaomega.it.utils.InputCheck;
import de.alphaomega.it.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Give {

    @Command(
            name = "give",
            aliases = {"i"},
            permission = "aocommands.give"
    )
    public void onCommand(final CommandArgs arg) {
        final Player p = arg.getPlayer();
        final String[] args = arg.getArgs();
        final Message msg = new Message(p);

        if (args.length < 2 || args.length > 4) {
            msg.sendMessage("give-syntax", false, true);
            return;
        }

        if (!CheckPlayer.isOnline(args[0], p)) return;
        final Player target = Bukkit.getPlayer(args[0]);

        final int amount = args.length >= 3 && InputCheck.isFullNumber(args[2]) ? Integer.parseInt(args[2]) : 1;
        Material m;
        try {
            m = Material.valueOf(args[1].replaceAll(" ", "_").toUpperCase());
        } catch (final IllegalArgumentException exc) {
            msg.setArgs(List.of(args[1]));
            msg.sendMessage("itemDoesNotExists", true, true);
            return;
        }

        if (m.isAir()) {
            msg.sendMessage("itemCannotBeAir", false, true);
            return;
        }

        ItemBuilder iBuilder = new ItemBuilder(m, amount);
        if (args.length == 4) {
            if (InputCheck.isFullNumber(args[3]))
                iBuilder.setCustomModelData(Integer.parseInt(args[3]));
            else {
                msg.setArgs(List.of(args[3]));
                msg.sendMessage("cmdIsNotValid", true, false);
                return;
            }
        }
        final ItemStack iS = iBuilder.build();
        target.getInventory().addItem(iS);

        Message msgTarget = new Message(target);
        msgTarget.setArgs(List.of(args[1], args.length < 3 || !InputCheck.isFullNumber(args[2]) ? "1" : args[2], args.length == 4 && InputCheck.isFullNumber(args[3]) ? args[3] : "-/-"));
        msgTarget.sendMessage("addedItemToInventory", true, true);

        if (p != target) {
            msg.setArgs(List.of(args[0], args.length < 3 || !InputCheck.isFullNumber(args[2]) ? "1" : args[2], args.length == 4 && InputCheck.isFullNumber(args[3]) ? args[3] : "-/-"));
            msg.sendMessage("addedItemToTargetInventory", true, true);
        }
    }
}
