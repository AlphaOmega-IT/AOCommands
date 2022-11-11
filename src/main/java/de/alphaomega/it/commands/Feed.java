package de.alphaomega.it.commands;

import de.alphaomega.it.AOCommands;
import de.alphaomega.it.cmdHandler.Command;
import de.alphaomega.it.cmdHandler.CommandArgs;
import de.alphaomega.it.msgHandler.Message;
import de.alphaomega.it.utils.CheckPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public record Feed(AOCommands pl) {

    @Command(
            name = "feed",
            permission = "aocommands.feed"
    )
    public void onCommand(final CommandArgs arg) {
        final Player p = arg.getPlayer();
        final String[] args = arg.getArgs();
        final Message msg = new Message(p);

        if (args.length > 1) {
            msg.sendMessage("feed-syntax", false, true);
            return;
        }

        int foodLevel = pl.getBaseConfig().getInt("food_level_amount");
        if (args.length == 0) {
            p.setFoodLevel(foodLevel);
            msg.sendMessage("fed", false, true);
            return;
        }

        if (!CheckPlayer.isOnline(args[0], p)) return;
        final Player target = Bukkit.getPlayer(args[0]);

        if (p != target) {
            msg.setArgs(List.of(target.getName()));
            msg.sendMessage("playerFeedTarget", true, false);
        }

        final Message msgTarget = new Message(target);
        target.setFoodLevel(foodLevel);
        msgTarget.sendMessage("fedTarget", false, true);
    }
}
