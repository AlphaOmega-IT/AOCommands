package de.alphaomega.it.utils;

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

    private final ItemStack iS;
    private ItemMeta iM;

    public ItemEditor(final ItemStack iS) {
        this.iS = iS;
        if (this.iS.hasItemMeta())
            this.iM = iS.getItemMeta();
        else
            if (iS.getType().isItem())
                this.iM = Bukkit.getItemFactory().getItemMeta(iS.getType());
    }

    public ItemEditor setName(final String name) {
        iM.displayName(MiniMessage.miniMessage().deserialize(name).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        return this;
    }

    public ItemEditor addLoreLine(final String loreLine) {
        if (iM.hasLore()) {
            List<Component> iLore = iM.lore();
            if (iLore != null)
                iLore.add(MiniMessage.miniMessage().deserialize(loreLine).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            iM.lore(iLore);
            return this;
        }

        iM.lore(List.of(MiniMessage.miniMessage().deserialize(loreLine)));
        return this;
    }

    public ItemEditor removeLoreLine(final int loreLine) {
        if (iM.hasLore()) {
            List<Component> iLore = new ArrayList<>(Objects.requireNonNull(iM.lore()));
            if (iLore.size() > loreLine) {
                iLore.remove(loreLine);
                iM.lore(iLore);
            }
        }
        return this;
    }

    public ItemEditor setUnbreakable(final boolean isUnbreakable) {
        iM.setUnbreakable(isUnbreakable);
        return this;
    }

    public ItemEditor setItemFlags(final ItemFlag... flags) {
        iM.addItemFlags(flags);
        return this;
    }

    public ItemEditor setAmount(final int amount) {
        iS.setAmount(amount);
        return this;
    }

    public ItemEditor addEnchantment(final Enchantment e, final Integer lvl) {
        prepareEnchantingTask();
        if (iS.getType().equals(Material.ENCHANTED_BOOK)) {
            if (iM instanceof EnchantmentStorageMeta eSM) {
                eSM.addStoredEnchant(e, lvl, true);
                iM = eSM;
            }
        }
        iS.addUnsafeEnchantment(e, lvl);
        iM.addEnchant(e, lvl, true);
        return this;
    }

    public ItemEditor removeEnchantment(final Enchantment e) {
        prepareEnchantingTask();
        if (iS.getType().equals(Material.ENCHANTED_BOOK)) {
            if (iM instanceof EnchantmentStorageMeta eSM) {
                eSM.removeStoredEnchant(e);
                iM = eSM;
            }
        } else {
            iS.removeEnchantment(e);
            iM.removeEnchant(e);
        }
        return this;
    }

    public ItemEditor adjustEnchantment(final Enchantment e, final Integer lvl) {
        prepareEnchantingTask();
        if (iS.getType().equals(Material.ENCHANTED_BOOK)) {
            if (iM instanceof EnchantmentStorageMeta eSM) {
                if (eSM.hasStoredEnchant(e))
                    eSM.getStoredEnchants().compute(e, ((enchantment, level) -> lvl));

                iM = eSM;
            }
        } else {
            if (iS.containsEnchantment(e)) {
                iS.removeEnchantment(e);
                iS.addUnsafeEnchantment(e, lvl);
                iM.getEnchants().remove(e);
                iM.getEnchants().put(e, lvl);
            }
        }
        return this;
    }

    public ItemEditor setType(final Material type) {
        if (type.isItem())
            iS.setType(type);
        return this;
    }

    public ItemEditor removeLore() {
        iM.lore(new ArrayList<>());
        return this;
    }

    public ItemEditor setCustomModelData(final int cmd) {
        iM.setCustomModelData(cmd);
        return this;
    }

    public ItemStack build() {
        setIM();
        return iS;
    }

    private void setIM() {
        if (this.iM != null) iS.setItemMeta(iM);
    }

    private void prepareEnchantingTask() {
        if (iS.getType().equals(Material.BOOK)) iS.setType(Material.ENCHANTED_BOOK);
    }
}
