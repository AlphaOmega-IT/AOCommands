package de.alphaomega.it.commands;

import de.alphaomega.it.cmdHandler.Command;
import de.alphaomega.it.cmdHandler.CommandArgs;
import de.alphaomega.it.msgHandler.Message;
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
        final Player p = arg.getPlayer();

        for (int i = 0; i < 150; i++) {
            Bukkit.getServer().broadcast(MiniMessage.miniMessage().deserialize(""));
        }

        for (Player t : Bukkit.getOnlinePlayers()) {
            final Message msg = new Message(t);
            msg.setArgs(List.of(p.getName()));
            msg.sendMessage("chatclear0", true, false);
            msg.sendMessage("chatclear1", true, false);
            msg.sendMessage("chatclear2", true, false);
            msg.sendMessage("chatclear3", true, false);
            msg.sendMessage("chatclear4", true, false);
        }

    }
}
