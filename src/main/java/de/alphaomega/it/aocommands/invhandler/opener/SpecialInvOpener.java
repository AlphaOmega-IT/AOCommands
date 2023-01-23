package de.alphaomega.it.aocommands.invhandler.opener;

import com.google.common.collect.ImmutableList;
import de.alphaomega.it.aocommands.invhandler.AOInv;
import de.alphaomega.it.aocommands.invhandler.InvManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class SpecialInvOpener implements InvOpener {

    private static final List<InventoryType> SUPPORTED = ImmutableList.of(InventoryType.FURNACE, InventoryType.WORKBENCH, InventoryType.DISPENSER, InventoryType.DROPPER, InventoryType.ENCHANTING, InventoryType.BREWING, InventoryType.ANVIL, InventoryType.BEACON, InventoryType.HOPPER);

    @Override
    public Inventory open(final AOInv inv, final Player p) {
        InvManager manager = inv.getManager();
        Inventory handle = Bukkit.createInventory(p, inv.getType(), Component.text(inv.getTitle()));

        fill(handle, manager.getContents(p).get(), p);

        p.openInventory(handle);
        return handle;
    }

    @Override
    public boolean supports(InventoryType type) {
        return SUPPORTED.contains(type);
    }

}