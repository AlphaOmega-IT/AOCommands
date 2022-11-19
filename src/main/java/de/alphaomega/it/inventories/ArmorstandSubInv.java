package de.alphaomega.it.inventories;


import de.alphaomega.it.AOCommands;
import de.alphaomega.it.entities.heads.BackArrow;
import de.alphaomega.it.entities.heads.Confirm;

import de.alphaomega.it.invhandler.AOCItem;
import de.alphaomega.it.invhandler.AOInv;
import de.alphaomega.it.invhandler.content.InvContents;
import de.alphaomega.it.invhandler.content.InvProvider;
import de.alphaomega.it.msghandler.Message;
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
    private AOCommands aoCommands;

    public ArmorstandSubInv(final AOCommands aoCommands, final ArmorStand as) {
        this.aoCommands = aoCommands;
        this.as = as;
    }

    public static AOInv getInv(final ArmorStand as, final AOCommands aoCommands) {
        return AOInv.builder()
                .manager(aoCommands.getManager())
                .id("ArmorstandSubInv")
                .closeable(false)
                .size(6, 9)
                .title("<color:#d60946>Armorstand</color>")
                .provider(new ArmorstandSubInv(aoCommands, as))
                .build(aoCommands);
    }

    @Override
    public void init(final Player p, final InvContents c) {
        if (!this.aoCommands.getArmorStands().containsKey(p.getUniqueId()))
            this.aoCommands.getArmorStands().put(p.getUniqueId(), as);
        else
            as = this.aoCommands.getArmorStands().get(p.getUniqueId());

        c.fill(AOCItem.empty());

        c.set(5, 0, AOCItem.from(new HeadsUtil().getSpecifiedHead(BackArrow.class, p), e -> {
            this.aoCommands.getArmorStands().remove(p.getUniqueId());
            this.aoCommands.getArmorStands().put(p.getUniqueId(), as);
            c.inv().setCloseable(true);
            ArmorstandInv.getInv(this.aoCommands).open(p);
        }));

        c.set(5, 1, AOCItem.from(new HeadsUtil().getSpecifiedHead(Confirm.class, p), e -> {
            this.aoCommands.getArmorStands().remove(p.getUniqueId());
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

                this.aoCommands.getIsUsingAnvil().put(p.getUniqueId(), true);

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
    public void onRename(final PrepareAnvilEvent event) {
        if (event.getResult() == null) {
            final ItemStack item = event.getInventory().getFirstItem();
            if (item != null) {
                if (item.hasItemMeta()) {
                    item.getItemMeta().displayName(MiniMessage.miniMessage().deserialize(PlainTextComponentSerializer.plainText().serialize(item.displayName())));
                }
                event.setResult(item);
            }
        }
        try {
            final Player player = (Player) event.getView().getPlayer();
            if (!this.aoCommands.getIsUsingAnvil().containsKey(player.getUniqueId())) return;
            if (this.aoCommands.getArmorStands().containsKey(player.getUniqueId())) {
                ArmorStand as = this.aoCommands.getArmorStands().get(player.getUniqueId());
                as.customName(MiniMessage.miniMessage().deserialize(event.getInventory().getRenameText() == null ? "" : event.getInventory().getRenameText()));
                this.aoCommands.getArmorStands().computeIfPresent(player.getUniqueId(), ((uuid, armorStand) -> as));
            }
        } catch (Exception ignored) {
        }
    }

    @EventHandler
    public void onAnvilClick(final InventoryClickEvent event) {
        final Player player = (Player) event.getView().getPlayer();
        if (event.getClickedInventory() == null || event.getClickedInventory().getContents().length > 3) return;
        final ItemStack item = event.getClickedInventory().getItem(0);
        if (item != null && item.getItemMeta() != null && item.getItemMeta().hasCustomModelData() && item.getItemMeta().getCustomModelData() == 99999) {
            if (event.getRawSlot() != 2) return;
            if (event.getCurrentItem() == null || event.getCurrentItem().getType().isAir()) return;
            if (event.getCurrentItem().getItemMeta() == null || event.getCurrentItem().getItemMeta().displayName() == null) return;
            if (!this.aoCommands.getIsUsingAnvil().containsKey(player.getUniqueId())) return;
            event.getClickedInventory().setItem(0, null);
            event.getClickedInventory().setItem(1, null);
            event.getClickedInventory().setItem(2, null);
            player.closeInventory();
            ArmorstandSubInv.getInv(this.aoCommands.getArmorStands().get(player.getUniqueId()), this.aoCommands).open(player);
        }
    }
}
