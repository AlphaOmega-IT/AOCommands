package de.alphaomega.it.aocommands.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ItemEditor {

    private final ItemStack item;
    private ItemMeta itemMeta;

    public ItemEditor(final ItemStack item) {
        this.item = item;
        if (this.item.hasItemMeta())
            this.itemMeta = this.item.getItemMeta();
        else if (this.item.getType().isItem())
            this.itemMeta = Bukkit.getItemFactory().getItemMeta(this.item.getType());
    }

    public ItemEditor setName(final String name) {
        itemMeta.displayName(MiniMessage.miniMessage().deserialize(name).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        return this;
    }

    public ItemEditor addLoreLine(final String loreLine) {
        if (this.itemMeta.hasLore()) {
            List<Component> iLore = this.itemMeta.lore();
            if (iLore != null)
                iLore.add(MiniMessage.miniMessage().deserialize(loreLine).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            this.itemMeta.lore(iLore);
            return this;
        }

        this.itemMeta.lore(List.of(MiniMessage.miniMessage().deserialize(loreLine)));
        return this;
    }

    public ItemEditor removeLoreLine(final int loreLine) {
        if (this.itemMeta.hasLore()) {
            List<Component> iLore = new ArrayList<>(Objects.requireNonNull(this.itemMeta.lore()));
            if (iLore.size() > loreLine) {
                iLore.remove(loreLine);
                this.itemMeta.lore(iLore);
            }
        }
        return this;
    }

    public ItemEditor setUnbreakable(final boolean isUnbreakable) {
        this.itemMeta.setUnbreakable(isUnbreakable);
        return this;
    }

    public ItemEditor setItemFlags(final ItemFlag... flags) {
        this.itemMeta.addItemFlags(flags);
        return this;
    }

    public ItemEditor setAmount(final int amount) {
        this.item.setAmount(amount);
        return this;
    }

    public ItemEditor addEnchantment(final Enchantment enchantment, final Integer lvl) {
        prepareEnchantingTask();
        if (this.item.getType().equals(Material.ENCHANTED_BOOK)) {
            if (this.itemMeta instanceof EnchantmentStorageMeta eSM) {
                eSM.addStoredEnchant(enchantment, lvl, true);
                this.itemMeta = eSM;
            }
        }
        item.addUnsafeEnchantment(enchantment, lvl);
        this.itemMeta.addEnchant(enchantment, lvl, true);
        return this;
    }

    public ItemEditor removeEnchantment(final Enchantment enchantment) {
        prepareEnchantingTask();
        if (this.item.getType().equals(Material.ENCHANTED_BOOK)) {
            if (this.itemMeta instanceof EnchantmentStorageMeta eSM) {
                eSM.removeStoredEnchant(enchantment);
                this.itemMeta = eSM;
            }
        } else {
            item.removeEnchantment(enchantment);
            this.itemMeta.removeEnchant(enchantment);
        }
        return this;
    }

    public ItemEditor adjustEnchantment(final Enchantment enchantment, final Integer lvl) {
        prepareEnchantingTask();
        if (this.item.getType().equals(Material.ENCHANTED_BOOK)) {
            if (this.itemMeta instanceof EnchantmentStorageMeta eSM) {
                if (eSM.hasStoredEnchant(enchantment))
                    eSM.getStoredEnchants().compute(enchantment, ((ench, level) -> lvl));

                this.itemMeta = eSM;
            }
        } else {
            if (this.item.containsEnchantment(enchantment)) {
                this.item.removeEnchantment(enchantment);
                this.item.addUnsafeEnchantment(enchantment, lvl);
                this.itemMeta.removeEnchant(enchantment);
                this.itemMeta.addEnchant(enchantment, lvl, true);
            }
        }
        return this;
    }

    public ItemEditor setType(final Material type) {
        if (type.isItem())
            this.item.setType(type);
        return this;
    }

    public ItemEditor removeLore() {
        this.itemMeta.lore(new ArrayList<>());
        return this;
    }

    public ItemEditor setCustomModelData(final int cmd) {
        this.itemMeta.setCustomModelData(cmd);
        return this;
    }

    public ItemStack build() {
        setIM();
        return this.item;
    }

    private void setIM() {
        if (this.itemMeta != null) this.item.setItemMeta(this.itemMeta);
    }

    private void prepareEnchantingTask() {
        if (this.item.getType().equals(Material.BOOK)) this.item.setType(Material.ENCHANTED_BOOK);
    }
}
