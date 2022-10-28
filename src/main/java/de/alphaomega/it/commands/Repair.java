package de.alphaomega.it.commands;

import de.alphaomega.it.AOCommands;
import de.alphaomega.it.cmdHandler.Command;
import de.alphaomega.it.cmdHandler.CommandArgs;
import de.alphaomega.it.msgHandler.Message;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import java.util.ArrayList;
import java.util.List;

public record Repair(AOCommands pl) {

    @Command(
            name = "repair",
            aliases = {"fix"},
            permission = "aocommands.fix"
    )
    public void onCommandFix(final CommandArgs arg) {
        final Player p = arg.getPlayer();
        final Message msg = new Message(p);
        final ItemStack iS = p.getInventory().getItemInMainHand();

        if (iS.getItemMeta() == null || !(iS.getItemMeta() instanceof Damageable)) {
            msg.sendMessage("toolNotFixable", false, true);
            return;
        }

        final boolean cmdToolFix = pl.getBaseConfig().getBoolean("allowed_custom_model_data_item_fix");

        if (!cmdToolFix) {
            if (iS.getItemMeta().hasCustomModelData()) {
                msg.sendMessage("cmdToolNotFixable", false, true);
                return;
            }
        }
        repair(p, msg, List.of(iS));
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
        final boolean cmdToolFix = pl.getBaseConfig().getBoolean("allowed_custom_model_data_item_fix");

        for (ItemStack iS : p.getInventory().getContents()) {
            if (iS == null || iS.hasItemMeta() && !(iS.getItemMeta() instanceof Damageable)) continue;
            if (!cmdToolFix && iS.hasItemMeta() && iS.getItemMeta().hasCustomModelData()) continue;
            repairableItems.add(iS);
        }

        if (repairableItems.isEmpty()) {
            msg.sendMessage("toolsNotFixable", false, true);
            return;
        }

        repair(p, msg, repairableItems);
    }

    private void repair(final Player p, final Message msg, final List<ItemStack> iSs) {
        for (ItemStack iS : iSs) {
            Damageable iM = (Damageable) iS.getItemMeta();
            iM.setDamage(0);
            iS.setItemMeta(iM);
            p.getInventory().remove(iS);
            p.updateInventory();
            p.getInventory().addItem(iS);
            p.updateInventory();
        }
        if (iSs.size() == 1)
            msg.sendMessage("toolRepaired", false, true);
        else
            msg.sendMessage("toolsRepaired", false, true);
    }
}
