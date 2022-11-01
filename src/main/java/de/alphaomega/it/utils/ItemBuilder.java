package de.alphaomega.it.utils;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@Getter
@Setter
public class ItemBuilder {

    private ItemStack iS;
    private ItemMeta iM;

    public ItemBuilder(final Material m, final Integer amount) {
        if (m != null && !m.isAir()) {
            this.iS = new ItemStack(m);
            this.iS.setAmount(amount);
            this.iM = iS.getItemMeta();
        }
    }

    public ItemBuilder(final Material m) {
        if (m != null && !m.isAir()) {
            this.iS = new ItemStack(m);
            this.iS.setAmount(1);
            this.iM = iS.getItemMeta();
        }
    }

    public ItemBuilder setCustomModelData(final int id) {
        this.iM.setCustomModelData(id);
        return this;
    }

    public ItemBuilder setName(final String name) {
        this.iM.displayName(MiniMessage.miniMessage().deserialize(name).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        return this;
    }

    public ItemStack build() {
        setIM();
        return this.iS;
    }

    private void setIM() {
        if (this.iM != null) iS.setItemMeta(iM);
    }
}
