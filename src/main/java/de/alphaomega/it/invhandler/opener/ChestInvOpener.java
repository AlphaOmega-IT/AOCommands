package de.alphaomega.it.invhandler.opener;

import com.google.common.base.Preconditions;
import de.alphaomega.it.invhandler.AOInv;
import de.alphaomega.it.invhandler.InvManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class ChestInvOpener implements InvOpener {

    @Override
    public Inventory open(final AOInv aoInv, final Player p) {
        Preconditions.checkArgument(aoInv.getColumns() == 9, "The column count for the chest inventory must be 9, found: %s.", aoInv.getColumns());
        Preconditions.checkArgument(aoInv.getRows() >= 1 && aoInv.getRows() <= 6, "The row count for the chest inventory must be between 1 and 6, found: %s", aoInv.getRows());

        InvManager m = aoInv.getManager();
        Inventory inv = Bukkit.createInventory(p, aoInv.getRows() * aoInv.getColumns(), MiniMessage.miniMessage().deserialize(aoInv.getTitle()));
        fill(inv, m.getContents(p).get(), p);

        p.openInventory(inv);
        return inv;
    }

    @Override
    public boolean supports(InventoryType type) {
        return type == InventoryType.CHEST || type == InventoryType.ENDER_CHEST || type == InventoryType.SHULKER_BOX;
    }

}
