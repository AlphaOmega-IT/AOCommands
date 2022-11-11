package de.alphaomega.it.inventories;

import de.alphaomega.it.AOCommands;
import de.alphaomega.it.invHandler.AOCItem;
import de.alphaomega.it.invHandler.AOInv;
import de.alphaomega.it.invHandler.content.InvContents;
import de.alphaomega.it.invHandler.content.InvProvider;
import de.alphaomega.it.msgHandler.Message;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.List;

public class InvseeInv implements InvProvider {

    private final AOCommands pl;
    private final Player t;

    public InvseeInv(final AOCommands pl, final Player t) {
        this.pl = pl;
        this.t = t;
    }

    public AOInv getInv(final Player p) {
        final Message msg = new Message(p);
        msg.setArgs(List.of(t.getName()));
        return AOInv.builder()
                .manager(pl.getManager())
                .id("InvseeInv_" + t.getUniqueId())
                .updateFrequency(2)
                .size(6, 9)
                .closeable(true)
                .provider(new InvseeInv(pl, t))
                .title(msg.showMessage("invseeInvName", true, false)).build(pl);
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

        final PlayerInventory tInv = t.getInventory();
        for (ItemStack iS : tInv.getContents()) {
            c.set(y, x, AOCItem.empty(iS == null || iS.getType().isAir() ? AOCItem.empty().getItem() : iS));
            x++;

            if (x == 9) {
                x = 0;
                y ++;
            }
            if (y == 4) break;
        }

        for (ItemStack iS : tInv.getArmorContents()) {
            c.set(y, x, AOCItem.empty(iS == null || iS.getType().isAir() ? AOCItem.empty().getItem() : iS));
            x++;
        }

        final ItemStack iS = tInv.getItemInOffHand();
        c.set(5, 0, AOCItem.empty(iS.getType().isAir() ? AOCItem.empty().getItem() : iS));
    }
}
