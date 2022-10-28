package de.alphaomega.it.commands;

import de.alphaomega.it.AOCommands;
import de.alphaomega.it.cmdHandler.Command;
import de.alphaomega.it.cmdHandler.CommandArgs;
import de.alphaomega.it.msgHandler.Message;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public record Rename(AOCommands pl) {

    @Command(
            name = "rename",
            permission = "aocommands.rename"
    )
    public void onCommand(final CommandArgs arg) {
        final Player p = arg.getPlayer();
        final String[] args = arg.getArgs();
        final Message msg = new Message(p);

        if (args.length < 1) {
            msg.sendMessage("rename-syntax", false, true);
            return;
        }

        StringBuilder sBuilder = new StringBuilder();
        for (String s : args) sBuilder.append(s).append(" ");
        sBuilder.deleteCharAt(sBuilder.length() -1);

        final int allowedCount = pl.getBaseConfig().getInt("allowed_rename_char_length");
        if (sBuilder.length() > allowedCount) {
            msg.setArgs(List.of(allowedCount + ""));
            msg.sendMessage("allowedCharLengthReached", true, true);
            return;
        }

        ItemStack iS = p.getInventory().getItemInMainHand().clone();
        if (!iS.getType().isItem()) {
            msg.sendMessage("noValidItemInHand", false, true);
            return;
        }

        final boolean cmdItemsAllowed = pl.getBaseConfig().getBoolean("rename_custom_model_data_items");
        if (!cmdItemsAllowed) {
            if (iS.getItemMeta().hasCustomModelData()) {
                msg.sendMessage("cmdItemsNotRenameable", false, true);
                return;
            }
        }

        iS = rename(iS, sBuilder.toString());
        if (iS == null) {
            msg.sendMessage("itemContainsNoItemMeta", false, true);
            return;
        }

        p.getInventory().setItemInMainHand(iS);
        msg.sendMessage("itemSuccessfullyRenamed", false, true);
    }

    private ItemStack rename(final ItemStack iS, final String renameText) {
        final ItemMeta iM = iS.getItemMeta();
        if (iM == null) return null;
        iM.displayName(MiniMessage.miniMessage().deserialize(renameText).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        iS.setItemMeta(iM);
        return iS;
    }
}
