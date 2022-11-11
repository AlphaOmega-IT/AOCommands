package de.alphaomega.it.inventories;



import de.alphaomega.it.AOCommands;
import de.alphaomega.it.invHandler.AOCItem;
import de.alphaomega.it.invHandler.AOInv;
import de.alphaomega.it.invHandler.content.InvContents;
import de.alphaomega.it.invHandler.content.InvProvider;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class EnderchestInv implements InvProvider {

    private final Inventory eInv;


    private AOCommands pl;

    public EnderchestInv(final Inventory eInv) {
        this.eInv = eInv;
    }

    public void getInv(final Inventory eInv, final Player target) {
        AOInv.builder()
                .manager(pl.getManager())
                .id("EnderchestInv")
                .provider(new EnderchestInv(eInv))
                .size(eInv.getSize() / 9, 9)
                .closeable(true)
                .title("Enderchest » " + target.getName())
                .build(pl);
    }

    @Override
    public void init(Player p, InvContents c) {
        c.fill(AOCItem.empty());

        int x = 0;
        int y = 0;

        for (ItemStack iS : eInv.getStorageContents()) {
            c.set(x, y, iS == null ? AOCItem.empty() : AOCItem.empty(iS));
            y++;

            if (y == 9) {
                y = 0;
                x++;
            }
        }
    }

    @Override
    public void update(Player p, InvContents c) {

    }
}
