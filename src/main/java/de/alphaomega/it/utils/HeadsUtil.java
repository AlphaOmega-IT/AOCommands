package de.alphaomega.it.utils;

import de.alphaomega.it.entities.AHead;
import lombok.SneakyThrows;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class HeadsUtil {

    @SneakyThrows
    public static ItemStack getSpecifiedHead(final Class<? extends AHead> headClazz, final Player p) {
        AHead oHead = (AHead) Class.forName(headClazz.getName()).getConstructor().newInstance();
        return oHead.getHeadAsItemStack(p.locale().toString());
    }
}
