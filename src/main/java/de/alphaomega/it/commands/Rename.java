package de.alphaomega.it.commands;

import de.alphaomega.it.AOCommands;
import de.alphaomega.it.cmdhandler.Command;
import de.alphaomega.it.cmdhandler.CommandArgs;
import de.alphaomega.it.msghandler.Message;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class Rename {

    private final AOCommands aoCommands;

    public Rename(final AOCommands aoCommands) {
        this.aoCommands = aoCommands;
    }

    @Command(
            name = "rename",
            permission = "aocommands.rename"
    )
    public void onCommand(final CommandArgs arg) {
        final Player player = arg.getPlayer();
        final String[] args = arg.getArgs();
        final Message msg = new Message(player);

        if (args.length < 1) {
            msg.sendMessage("rename-syntax", false, true);
            return;
        }

        StringBuilder sBuilder = new StringBuilder();
        for (String s : args) sBuilder.append(s).append(" ");
        sBuilder.deleteCharAt(sBuilder.length() -1);

        final int allowedCount = this.aoCommands.getBaseConfig().getInt("allowed_rename_char_length");
        if (sBuilder.length() > allowedCount) {
            msg.setArgs(List.of(allowedCount + ""));
            msg.sendMessage("allowedCharLengthReached", true, true);
            return;
        }

        ItemStack item = player.getInventory().getItemInMainHand().clone();
        if (!item.getType().isItem()) {
            msg.sendMessage("noValidItemInHand-rename", false, true);
            return;
        }

        final boolean cmdItemsAllowed = this.aoCommands.getBaseConfig().getBoolean("rename_custom_model_data_items");
        if (!cmdItemsAllowed) {
            if (item.getItemMeta().hasCustomModelData()) {
                msg.sendMessage("cmdItemsNotRenameable", false, true);
                return;
            }
        }

        item = rename(item, sBuilder.toString());
        if (item == null) {
            msg.sendMessage("itemContainsNoItemMeta", false, true);
            return;
        }

        player.getInventory().setItemInMainHand(item);
        msg.sendMessage("itemSuccessfullyRenamed", false, true);
    }

    private ItemStack rename(final ItemStack item, final String renameText) {
        final ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) return null;
        itemMeta.displayName(MiniMessage.miniMessage().deserialize(renameText).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        item.setItemMeta(itemMeta);
        return item;
    }
}
