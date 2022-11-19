package de.alphaomega.it.commands;


import de.alphaomega.it.AOCommands;
import de.alphaomega.it.cmdhandler.Command;
import de.alphaomega.it.cmdhandler.CommandArgs;
import de.alphaomega.it.inventories.EnderchestInv;
import de.alphaomega.it.msghandler.Message;
import de.alphaomega.it.utils.CheckPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Enderchest {

    private final AOCommands aoCommands;

    public Enderchest(final AOCommands aoCommands) {
        this.aoCommands = aoCommands;
    }

    @Command(
            name = "enderchest",
            aliases = "ec",
            permission = "aocommands.enderchest"
    )
    public void onCommand(final CommandArgs arg) {
        final Player player = arg.getPlayer();
        final String[] args = arg.getArgs();
        final Message msg = new Message(player);

        if (args.length > 1) {
            msg.sendMessage("enderchest-syntax", false, true);
            return;
        }

        if (args.length == 1) {
            if (!CheckPlayer.isOnline(args[0], player)) return;
            final Player target = Bukkit.getPlayer(args[0]);
            if (player.isOp() || player.hasPermission("aocommands.enderchest.*"))
                player.openInventory(target.getEnderChest());
            else
                new EnderchestInv(target.getEnderChest(), this.aoCommands).getInv(target);
            return;
        }

        player.openInventory(player.getEnderChest());
    }
}
