package de.alphaomega.it.aocommands.commands;

import de.alphaomega.it.aocommands.cmdhandler.Command;
import de.alphaomega.it.aocommands.cmdhandler.CommandArgs;
import de.alphaomega.it.aocommands.msghandler.Message;
import de.alphaomega.it.aocommands.utils.CheckPlayer;
import de.alphaomega.it.aocommands.utils.InputCheck;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class Fly {

    @Command(
            name = "fly",
            permission = "aocommands.fly"
    )
    public void onCommand(final CommandArgs arg) {
        final Player player = arg.getPlayer();
        final String[] args = arg.getArgs();
        final Message msg = new Message(player);

        if (args.length > 2) {
            msg.sendMessage("fly-syntax", false, true);
            return;
        }

        if (args.length == 0) {
            setFly(player, "1");
            return;
        }

        if (args.length == 1) {
            if (InputCheck.isFullNumber(args[0])) {
                if (player.getAllowFlight()) {
                    if (player.isFlying()) {
                        player.setFlySpeed(InputCheck.isFullNumber(args[0]) ? Float.parseFloat(args[0]) <= 10 ? Float.parseFloat(args[0]) / 10 : 1.0F : 0.1F);
                        msg.setArgs(List.of(player.getFlySpeed() + ""));
                        msg.sendMessage("setFlySpeed", true, true);
                    }
                }
            } else {
                if (!CheckPlayer.isOnline(args[0], player)) return;
                final Player target = Bukkit.getPlayer(args[0]);
                setFly(target, "1");
            }
            return;
        }
        if (!CheckPlayer.isOnline(args[0], player)) return;
        final Player target = Bukkit.getPlayer(args[0]);
        setFly(target, args[1]);
    }

    private void setFly(final Player target, final String speed) {
        final Message msgTarget = new Message(target);
        if (target.getAllowFlight()) {
            target.setFlying(false);
            target.setAllowFlight(false);
            msgTarget.sendMessage("flyDisabled", false, true);
        } else {
            target.setAllowFlight(true);
            target.setFlying(true);
            target.setFlySpeed(InputCheck.isFullNumber(speed) ? Float.parseFloat(speed) <= 10 ? Float.parseFloat(speed) / 10 : 1.0F : 0.1F);
            msgTarget.sendMessage("flyEnabled", false, true);
        }
    }
}
