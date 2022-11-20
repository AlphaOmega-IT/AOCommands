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

    private ItemStack item;
    private ItemMeta itemMeta;

    public ItemBuilder(final Material material, final Integer amount) {
        if (material != null && !material.isAir()) {
            this.item = new ItemStack(material);
            this.item.setAmount(amount);
            this.itemMeta = this.item.getItemMeta();
        }
    }

    public ItemBuilder(final Material material) {
        if (material != null && !material.isAir()) {
            this.item = new ItemStack(material);
            this.item.setAmount(1);
            this.itemMeta = this.item.getItemMeta();
        }
    }

    public ItemBuilder setCustomModelData(final int id) {
        this.itemMeta.setCustomModelData(id);
        return this;
    }

    public ItemBuilder setName(final String name) {
        this.itemMeta.displayName(MiniMessage.miniMessage().deserialize(name).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        return this;
    }

    public ItemBuilder setLore(final String... lines) {
        final List<String> sLore = new ArrayList<>(List.of(lines));
        final List<Component> cLore = new ArrayList<>();
        sLore.forEach(line -> cLore.add(MiniMessage.miniMessage().deserialize(line).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)));
        this.itemMeta.lore(cLore);
        return this;
    }

    public ItemBuilder addItemFlags(final ItemFlag... flags) {
        this.item.addItemFlags(flags);
        return this;
    }

    public ItemBuilder setGlow(final boolean isGlowing) {
        if (isGlowing) {
            this.itemMeta.addEnchant(Enchantment.DURABILITY, 0, true);
            this.itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            this.itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        } else {
            this.itemMeta.removeEnchant(Enchantment.DURABILITY);
            this.itemMeta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
            this.itemMeta.removeItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        }
        return this;
    }

    public ItemStack build() {
        setIM();
        return this.item;
    }

    private void setIM() {
        if (this.itemMeta != null) item.setItemMeta(this.itemMeta);
    }
}
