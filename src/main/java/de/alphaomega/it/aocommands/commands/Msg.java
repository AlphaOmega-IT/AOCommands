package de.alphaomega.it.aocommands.commands;

import de.alphaomega.it.aocommands.cmdhandler.Command;
import de.alphaomega.it.aocommands.cmdhandler.CommandArgs;
import de.alphaomega.it.aocommands.msghandler.Message;
import de.alphaomega.it.aocommands.utils.CheckPlayer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Msg {

    //Sender uuid, Receiver uuid
    private final Map<UUID, UUID> conversations = new LinkedHashMap<>();

    @Command(
            name = "msg",
            aliases = {"message", "nachricht", "whisper", "tell"},
            permission = "aocommands.message"
    )
    public void onCommandMessage(final CommandArgs arg) {
        final Player player = arg.getPlayer();
        final String[] args = arg.getArgs();
        final Message msg = new Message(player);

        if (args.length < 2) {
            msg.sendMessage("msg-syntax", false, true);
            return;
        }

        if (!CheckPlayer.isOnline(args[0], player)) return;
        final Player target = Bukkit.getPlayer(args[0]);

        if (player == target) {
            msg.sendMessage("noMessageToYourself", false, true);
            return;
        }

        setConversations(player.getUniqueId(), target.getUniqueId());
        sendMessage(msg, getMessage(args), target, player);
    }

    @Command(
            name = "R",
            aliases = {"r", "reply"},
            permission = "aocommands.message.reply"
    )
    public void onCommandMessageReply(final CommandArgs arg) {
        final Player player = arg.getPlayer();
        final String[] args = arg.getArgs();
        final Message msg = new Message(player);

        if (args.length < 1) {
            msg.sendMessage("reply-syntax", false, true);
            return;
        }

        final UUID messager = getMessager(player.getUniqueId());
        if (messager == null) {
            msg.sendMessage("noConversation", false, true);
            return;
        }

        final OfflinePlayer target = Bukkit.getOfflinePlayer(messager);
        if (!CheckPlayer.isOnline(target.getName(), player)) return;

        sendMessage(msg, getMessage(args), target.getPlayer(), player);
    }

    private void setConversations(final UUID msgSender, final UUID msgReceiver) {
        conversations.put(msgSender, msgReceiver);
        conversations.put(msgReceiver, msgSender);
    }

    private UUID getMessager(final UUID msgSender) {
        return conversations.get(msgSender);
    }

    private String getMessage(final String[] args) {
        final StringBuilder sBuilder = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            sBuilder.append(args[i]).append(" ");
        }
        sBuilder.deleteCharAt(sBuilder.length() - 1);
        return sBuilder.toString();
    }

    private void sendMessage(final Message msg, final String message, final Player target, final Player player) {
        msg.setArgs(List.of(target.getName(), message));
        msg.sendMessage("msgSent", true, false);

        final Message msgTarget = new Message(target);
        msgTarget.setArgs(List.of(player.getName(), message));
        msgTarget.sendMessage("msgReceived", true, false);
    }
}
