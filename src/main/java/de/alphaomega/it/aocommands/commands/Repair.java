package de.alphaomega.it.aocommands.commands;

import de.alphaomega.it.aocommands.AOCommands;
import de.alphaomega.it.aocommands.cmdhandler.Command;
import de.alphaomega.it.aocommands.cmdhandler.CommandArgs;
import de.alphaomega.it.aocommands.msghandler.Message;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import java.util.ArrayList;
import java.util.List;

public class Repair {

    private final AOCommands aoCommands;

    public Repair(final AOCommands aoCommands) {
        this.aoCommands = aoCommands;
    }

    @Command(
            name = "repair",
            aliases = {"fix"},
            permission = "aocommands.fix"
    )
    public void onCommandFix(final CommandArgs arg) {
        final Player player = arg.getPlayer();
        final Message msg = new Message(player);
        final ItemStack item = player.getInventory().getItemInMainHand();

        if (item.getItemMeta() == null || !(item.getItemMeta() instanceof Damageable)) {
            msg.sendMessage("toolNotFixable", false, true);
            return;
        }

        final boolean cmdToolFix = this.aoCommands.getBaseConfig().getBoolean("allowed_custom_model_data_item_fix");

        if (!cmdToolFix) {
            if (item.getItemMeta().hasCustomModelData()) {
                msg.sendMessage("cmdToolNotFixable", false, true);
                return;
            }
        }
        repair(player, msg, List.of(item));
    }

    @Command(
            name = "repairall",
            aliases = {"fixall", "armorfix", "armorrepair"},
            permission = "aocommands.fix.all"
    )
    public void onCommandFixAll(final CommandArgs arg) {
        final Player p = arg.getPlayer();
        final Message msg = new Message(p);
        final List<ItemStack> repairableItems = new ArrayList<>();
        final boolean cmdToolFix = this.aoCommands.getBaseConfig().getBoolean("allowed_custom_model_data_item_fix");

        for (ItemStack item : p.getInventory().getContents()) {
            if (item == null || item.hasItemMeta() && !(item.getItemMeta() instanceof Damageable)) continue;
            if (!cmdToolFix && item.hasItemMeta() && item.getItemMeta().hasCustomModelData()) continue;
            repairableItems.add(item);
        }

        if (repairableItems.isEmpty()) {
            msg.sendMessage("toolsNotFixable", false, true);
            return;
        }

        repair(p, msg, repairableItems);
    }

    private void repair(final Player player, final Message msg, final List<ItemStack> items) {
        for (ItemStack item : items) {
            Damageable itemMeta = (Damageable) item.getItemMeta();
            itemMeta.setDamage(0);
            item.setItemMeta(itemMeta);
            player.getInventory().remove(item);
            player.updateInventory();
            player.getInventory().addItem(item);
            player.updateInventory();
        }
        if (items.size() == 1)
            msg.sendMessage("toolRepaired", false, true);
        else
            msg.sendMessage("toolsRepaired", false, true);
    }
}
