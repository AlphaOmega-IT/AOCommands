package de.alphaomega.it.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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

    public ItemStack build() {
        setIM();
        return this.iS;
    }

    private void setIM() {
        if (this.iM != null) iS.setItemMeta(iM);
    }
}
