package de.alphaomega.it.aocommands.inventories;

import de.alphaomega.it.aocommands.AOCommands;
import de.alphaomega.it.aocommands.invhandler.AOCItem;
import de.alphaomega.it.aocommands.invhandler.AOInv;
import de.alphaomega.it.aocommands.invhandler.content.InvContents;
import de.alphaomega.it.aocommands.invhandler.content.InvProvider;
import de.alphaomega.it.aocommands.msghandler.Message;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.List;

public class InvseeInv implements InvProvider {

    private final AOCommands aoCommands;
    private final Player target;

    public InvseeInv(final AOCommands aoCommands, final Player target) {
        this.aoCommands = aoCommands;
        this.target = target;
    }

    public AOInv getInv(final Player p) {
        final Message msg = new Message(p);
        msg.setArgs(List.of(target.getName()));
        return AOInv.builder()
                .manager(this.aoCommands.getManager())
                .id("InvseeInv_" + this.target.getUniqueId())
                .updateFrequency(2)
                .size(6, 9)
                .closeable(true)
                .provider(new InvseeInv(this.aoCommands, this.target))
                .title(msg.showMessage("invseeInvName", true, false)).build(this.aoCommands);
    }

    @Override
    public void init(Player p, InvContents c) {
        setTargetInventory(c);
    }

    @Override
    public void update(Player p, InvContents c) {
        setTargetInventory(c);
    }

    private void setTargetInventory(final InvContents c) {
        c.fill(AOCItem.empty());

        int x = 0;
        int y = 0;

        final PlayerInventory tInv = this.target.getInventory();
        for (ItemStack item : tInv.getContents()) {
            c.set(y, x, AOCItem.empty(item == null || item.getType().isAir() ? AOCItem.empty().getItem() : item));
            x++;

            if (x == 9) {
                x = 0;
                y++;
            }
            if (y == 4) break;
        }

        for (ItemStack item : tInv.getArmorContents()) {
            c.set(y, x, AOCItem.empty(item == null || item.getType().isAir() ? AOCItem.empty().getItem() : item));
            x++;
        }

        final ItemStack item = tInv.getItemInOffHand();
        c.set(5, 0, AOCItem.empty(item.getType().isAir() ? AOCItem.empty().getItem() : item));
    }
}
