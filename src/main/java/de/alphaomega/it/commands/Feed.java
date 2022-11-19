package de.alphaomega.it.commands;

import de.alphaomega.it.AOCommands;
import de.alphaomega.it.cmdhandler.Command;
import de.alphaomega.it.cmdhandler.CommandArgs;
import de.alphaomega.it.msghandler.Message;
import de.alphaomega.it.utils.CheckPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class Feed {

    private final AOCommands aoCommands;

    public Feed(final AOCommands aoCommands) {
        this.aoCommands = aoCommands;
    }

    @Command(
            name = "feed",
            permission = "aocommands.feed"
    )
    public void onCommand(final CommandArgs arg) {
        final Player player = arg.getPlayer();
        final String[] args = arg.getArgs();
        final Message msg = new Message(player);

        if (args.length > 1) {
            msg.sendMessage("feed-syntax", false, true);
            return;
        }

        int foodLevel = this.aoCommands.getBaseConfig().getInt("food_level_amount");
        if (args.length == 0) {
            player.setFoodLevel(foodLevel);
            msg.sendMessage("fed", false, true);
            return;
        }

        if (!CheckPlayer.isOnline(args[0], player)) return;
        final Player target = Bukkit.getPlayer(args[0]);

        if (player != target) {
            msg.setArgs(List.of(target.getName()));
            msg.sendMessage("playerFeedTarget", true, false);
        }

        final Message msgTarget = new Message(target);
        target.setFoodLevel(foodLevel);
        msgTarget.sendMessage("fedTarget", false, true);
    }
}
