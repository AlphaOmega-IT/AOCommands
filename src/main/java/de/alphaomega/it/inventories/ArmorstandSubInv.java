package de.alphaomega.it.inventories;


import de.alphaomega.it.AOCommands;
import de.alphaomega.it.entities.heads.BackArrow;
import de.alphaomega.it.entities.heads.Confirm;

import de.alphaomega.it.invHandler.AOCItem;
import de.alphaomega.it.invHandler.AOInv;
import de.alphaomega.it.invHandler.content.InvContents;
import de.alphaomega.it.invHandler.content.InvProvider;
import de.alphaomega.it.msgHandler.Message;
import de.alphaomega.it.utils.HeadsUtil;
import de.alphaomega.it.utils.ItemBuilder;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;

public class ArmorstandSubInv implements InvProvider, Listener {

    private ArmorStand as;
    private AOCommands pl;

    public ArmorstandSubInv(final AOCommands pl, final ArmorStand as) {
        this.pl = pl;
        this.as = as;
    }

    public static AOInv getInv(final ArmorStand as, final AOCommands pl) {
        return AOInv.builder()
                .manager(pl.getManager())
                .id("ArmorstandSubInv")
                .closeable(false)
                .size(6, 9)
                .title("<color:#d60946>Armorstand</color>")
                .provider(new ArmorstandSubInv(pl, as))
                .build(pl);
    }

    @Override
    public void init(final Player p, final InvContents c) {
        final Message msg = new Message(p);

        if (!pl.getArmorStands().containsKey(p.getUniqueId()))
            pl.getArmorStands().put(p.getUniqueId(), as);
        else
            as = pl.getArmorStands().get(p.getUniqueId());

        c.fill(AOCItem.empty());

        c.set(5, 0, AOCItem.from(HeadsUtil.getSpecifiedHead(BackArrow.class, p), e -> {
            pl.getArmorStands().remove(p.getUniqueId());
            pl.getArmorStands().put(p.getUniqueId(), as);
            c.inv().setCloseable(true);
            ArmorstandInv.getInv(pl).open(p);
        }));

        c.set(5, 1, AOCItem.from(HeadsUtil.getSpecifiedHead(Confirm.class, p), e -> {
            pl.getArmorStands().remove(p.getUniqueId());
            as.setVisible(true);
            c.inv().setCloseable(true);
            p.closeInventory();
        }));
    }

    @Override
    public void update(final Player p, final InvContents c) {
        final Message msg = new Message(p);

        c.set(2, 2, AOCItem.from(new ItemBuilder(Material.GLOWSTONE_DUST).setGlow(as.isGlowing()).setName(msg.showMessage("asNameGlow", false, false)).setLore(msg.showMessage("asLoreGlow", false, false)).build(), e -> {
            as.setGlowing(!as.isGlowing());
        }));

        c.set(2, 3, AOCItem.from(new ItemBuilder(Material.SMOOTH_STONE_SLAB).setGlow(as.hasBasePlate()).setName(msg.showMessage("asNamePlate", false, false)).setLore(msg.showMessage("asLorePlate", false, false)).build(), e -> {
            as.setBasePlate(!as.hasBasePlate());
        }));

        c.set(2, 4, AOCItem.from(new ItemBuilder(Material.ARMOR_STAND).setGlow(as.hasArms()).setName(msg.showMessage("asNameArms", false, false)).setLore(msg.showMessage("asLoreArms", false, false)).build(), e -> {
            as.setArms(!as.hasArms());
        }));

        c.set(2, 5, AOCItem.from(new ItemBuilder(Material.NAME_TAG).setGlow(as.isCustomNameVisible()).setName(msg.showMessage("asEnableNameName", false, false)).setLore(msg.showMessage("asEnableLoreName", false, false)).build(), e -> {
            as.setCustomNameVisible(!as.isCustomNameVisible());
        }));

        if (as.isCustomNameVisible()) {
            c.set(3, 5, AOCItem.from(new ItemBuilder(Material.NAME_TAG).setName(msg.showMessage("asNameName", false, false)).setLore(msg.showMessage("asLoreName", false, false)).build(), e -> {
                c.inv().setCloseable(true);
                p.closeInventory();

                pl.getIsUsingAnvil().put(p.getUniqueId(), true);

                p.openAnvil(p.getLocation(), true);
                p.getOpenInventory().getTopInventory().setItem(0, new ItemBuilder(Material.NAME_TAG).setCustomModelData(99999).setName(msg.showMessage("asSaveName", false, false)).build());
            }));
        } else
            c.set(3, 5, AOCItem.empty());

        c.set(2, 6, AOCItem.from(new ItemBuilder(Material.FEATHER).setGlow(as.hasGravity()).setName(msg.showMessage("asNameGravity", false, false)).setLore(msg.showMessage("asLoreGravity", false, false)).build(), e -> {
            as.setGravity(!as.hasGravity());
        }));
    }

    @EventHandler
    public void onRename(final PrepareAnvilEvent e) {
        if (e.getResult() == null) {
            final ItemStack iS = e.getInventory().getFirstItem();
            if (iS != null) {
                if (iS.hasItemMeta()) {
                    iS.getItemMeta().displayName(MiniMessage.miniMessage().deserialize(PlainTextComponentSerializer.plainText().serialize(iS.displayName())));
                }
                e.setResult(iS);
            }
        }
        try {
            final Player p = (Player) e.getView().getPlayer();
            if (!pl.getIsUsingAnvil().containsKey(p.getUniqueId())) return;
            if (pl.getArmorStands().containsKey(p.getUniqueId())) {
                ArmorStand as = pl.getArmorStands().get(p.getUniqueId());
                as.customName(MiniMessage.miniMessage().deserialize(e.getInventory().getRenameText() == null ? "" : e.getInventory().getRenameText()));
                pl.getArmorStands().computeIfPresent(p.getUniqueId(), ((uuid, armorStand) -> as));
            }
        } catch (Exception ignored) {
        }
    }

    @EventHandler
    public void onAnvilClick(final InventoryClickEvent e) {
        final Player p = (Player) e.getView().getPlayer();
        if (e.getClickedInventory() == null || e.getClickedInventory().getContents().length > 3) return;
        final ItemStack iS = e.getClickedInventory().getItem(0);
        if (iS != null && iS.getItemMeta() != null && iS.getItemMeta().hasCustomModelData() && iS.getItemMeta().getCustomModelData() == 99999) {
            if (e.getRawSlot() != 2) return;
            if (e.getCurrentItem() == null || e.getCurrentItem().getType().isAir()) return;
            if (e.getCurrentItem().getItemMeta() == null || e.getCurrentItem().getItemMeta().displayName() == null) return;
            if (!pl.getIsUsingAnvil().containsKey(p.getUniqueId())) return;
            e.getClickedInventory().setItem(0, null);
            e.getClickedInventory().setItem(1, null);
            e.getClickedInventory().setItem(2, null);
            p.closeInventory();
            ArmorstandSubInv.getInv(pl.getArmorStands().get(p.getUniqueId()), pl).open(p);
        }
    }
}
