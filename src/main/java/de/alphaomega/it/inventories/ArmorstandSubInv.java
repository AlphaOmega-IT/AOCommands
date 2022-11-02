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
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

public class ArmorstandSubInv implements InvProvider {

    private ArmorStand as;

    public ArmorstandSubInv(final ArmorStand as) {
        this.as = as;
    }

    public static AOInv getInv(final ArmorStand as) {
        return AOInv.builder()
                .manager(AOCommands.getInstance().getManager())
                .id("ArmorstandSubInv")
                .closeable(false)
                .size(6, 9)
                .title("<color:#d60946>Armorstand</color>")
                .provider(new ArmorstandSubInv(as))
                .build();
    }

    @Override
    public void init(final Player p, final InvContents c) {
        final Message msg = new Message(p);

        if (!AOCommands.getInstance().getArmorStands().containsKey(p.getUniqueId()))
            AOCommands.getInstance().getArmorStands().put(p.getUniqueId(), as);
        else
            as = AOCommands.getInstance().getArmorStands().get(p.getUniqueId());

        c.fill(AOCItem.empty());

        c.set(2, 5, AOCItem.from(new ItemBuilder(Material.NAME_TAG).setName(msg.showMessage("asNameName", false, false)).setLore(msg.showMessage("asLoreName", false, false)).build(), e -> {

        }));

        c.set(5, 0, AOCItem.from(HeadsUtil.getSpecifiedHead(BackArrow.class, p), e -> {
            AOCommands.getInstance().getArmorStands().remove(p.getUniqueId());
            AOCommands.getInstance().getArmorStands().put(p.getUniqueId(), as);
            c.inv().setCloseable(true);
            ArmorstandInv.getInv().open(p);
        }));

        c.set(5, 1, AOCItem.from(HeadsUtil.getSpecifiedHead(Confirm.class, p), e -> {
            AOCommands.getInstance().getArmorStands().remove(p.getUniqueId());
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

        c.set(2, 6, AOCItem.from(new ItemBuilder(Material.FEATHER).setGlow(as.hasGravity()).setName(msg.showMessage("asNameGravity", false, false)).setLore(msg.showMessage("asLoreGravity", false, false)).build(), e -> {
            as.setGravity(!as.hasGravity());
        }));
    }
}
