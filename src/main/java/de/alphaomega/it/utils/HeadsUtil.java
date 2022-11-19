package de.alphaomega.it.utils;

import de.alphaomega.it.entities.AHead;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class HeadsUtil {

    public ItemStack getSpecifiedHead(final Class<? extends AHead> headClazz, final Player p) {
        try {
        AHead oHead = (AHead) Class.forName(headClazz.getName()).getConstructor().newInstance();
        return oHead.getHeadAsItemStack(p.locale().toString());
        } catch (final Exception e) {
            return new HeadBuilder().build();
        }
    }
}
