package de.alphaomega.it.invhandler.opener;

import de.alphaomega.it.invhandler.AOCItem;
import de.alphaomega.it.invhandler.AOInv;
import de.alphaomega.it.invhandler.content.InvContents;
import de.alphaomega.it.invhandler.content.SlotPos;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public interface InvOpener {
	
	Inventory open(AOInv inv, Player p);
	boolean supports(InventoryType type);
	
	default void fill(Inventory inv, InvContents c, Player p) {
		AOCItem[][] items = c.all();
		
		for (int row = 0; row < items.length; row++) {
			for (int column = 0; column < items[row].length; column++) {
				if (items[row][column] != null) inv.setItem(9 * row + column, items[row][column].getItem());
			}
		}
	}
	
	default SlotPos defaultSize(InventoryType type) {
		return switch (type) {
			case CHEST, ENDER_CHEST, SHULKER_BOX -> SlotPos.of(3, 9);
			case DISPENSER, DROPPER -> SlotPos.of(3, 3);
			default -> SlotPos.of(1, type.getDefaultSize());
		};
	}
	
}
