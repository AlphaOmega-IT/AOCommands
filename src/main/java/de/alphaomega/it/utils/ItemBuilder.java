package de.alphaomega.it.utils;

import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

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

    public ItemBuilder setLore(final String... lines) {
        final List<String> sLore = new ArrayList<>(List.of(lines));
        final List<Component> cLore = new ArrayList<>();
        sLore.forEach(line -> cLore.add(MiniMessage.miniMessage().deserialize(line).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)));
        this.iM.lore(cLore);
        return this;
    }

    public ItemBuilder addItemFlags(final ItemFlag... flags) {
        this.iS.addItemFlags(flags);
        return this;
    }

    public ItemBuilder setGlow(final boolean isGlowing) {
        if (isGlowing) {
            this.iM.addEnchant(Enchantment.DURABILITY, 0, true);
            this.iM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            this.iM.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        } else {
            this.iM.removeEnchant(Enchantment.DURABILITY);
            this.iM.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
            this.iM.removeItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        }
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
