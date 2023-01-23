package de.alphaomega.it.aocommands.inventories;


import de.alphaomega.it.aocommands.AOCommands;
import de.alphaomega.it.aocommands.invhandler.AOCItem;
import de.alphaomega.it.aocommands.invhandler.AOInv;
import de.alphaomega.it.aocommands.invhandler.content.InvContents;
import de.alphaomega.it.aocommands.invhandler.content.InvProvider;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class EnderchestInv implements InvProvider {

    private final Inventory eInv;
    private final AOCommands aoCommands;

    public EnderchestInv(final Inventory eInv, final AOCommands aoCommands) {
        this.eInv = eInv;
        this.aoCommands = aoCommands;
    }

    public void getInv(final Player target) {
        AOInv.builder()
                .manager(this.aoCommands.getManager())
                .id("EnderchestInv")
                .provider(new EnderchestInv(this.eInv, this.aoCommands))
                .size(this.eInv.getSize() / 9, 9)
                .closeable(true)
                .title("Enderchest » " + target.getName())
                .build(this.aoCommands);
    }

    @Override
    public void init(Player player, InvContents c) {
        c.fill(AOCItem.empty());

        int x = 0;
        int y = 0;

        for (ItemStack item : this.eInv.getStorageContents()) {
            c.set(x, y, item == null ? AOCItem.empty() : AOCItem.empty(item));
            y++;

            if (y == 9) {
                y = 0;
                x++;
            }
        }
    }

    @Override
    public void update(Player player, InvContents c) {

    }
}
