package de.alphaomega.it.commands;

import de.alphaomega.it.cmdhandler.Command;
import de.alphaomega.it.cmdhandler.CommandArgs;
import de.alphaomega.it.msghandler.Message;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class ClearChat {

    @Command(
            name = "clearchat",
            aliases = {"cc", "chatclear"},
            permission = "aocommands.clearchat"
    )
    public void onCommand(final CommandArgs arg) {
        final Player player = arg.getPlayer();

        for (int i = 0; i < 150; i++) {
            Bukkit.getServer().broadcast(MiniMessage.miniMessage().deserialize(""));
        }

        for (Player target : Bukkit.getOnlinePlayers()) {
            final Message msg = new Message(target);
            msg.setArgs(List.of(player.getName()));
            msg.sendMessage("chatclear0", true, false);
            msg.sendMessage("chatclear1", true, false);
            msg.sendMessage("chatclear2", true, false);
            msg.sendMessage("chatclear3", true, false);
            msg.sendMessage("chatclear4", true, false);
        }

    }
}
