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
import org.bukkit.entity.Player;

public class ArmorstandSubInv implements InvProvider {

    public static AOInv getInv() {
        return AOInv.builder()
                .manager(AOCommands.getInstance().getManager())
                .id("ArmorstandSubInv")
                .closeable(true)
                .size(6, 9)
                .title("<color:#d60946>Armorstand</color>")
                .provider(new ArmorstandSubInv())
                .build();
    }

    @Override
    public void init(final Player p, final InvContents c) {
        final Message msg = new Message(p);
        c.fill(AOCItem.empty());

        c.set(2, 2, AOCItem.from(new ItemBuilder(Material.GLOWSTONE_DUST).setName(msg.showMessage("asNameGlow", false, false)).setLore(msg.showMessage("asLoreGlow", false, false)).build(), e -> {

        }));

        c.set(2, 3, AOCItem.from(new ItemBuilder(Material.SMOOTH_STONE_SLAB).setName(msg.showMessage("asNamePlate", false, false)).setLore(msg.showMessage("asLorePlate", false, false)).build(), e -> {

        }));

        c.set(2, 4, AOCItem.from(new ItemBuilder(Material.ARMOR_STAND).setName(msg.showMessage("asNameArms", false, false)).setLore(msg.showMessage("asLoreArms", false, false)).build(), e -> {

        }));

        c.set(2, 5, AOCItem.from(new ItemBuilder(Material.NAME_TAG).setName(msg.showMessage("asNameName", false, false)).setLore(msg.showMessage("asLoreName", false, false)).build(), e -> {

        }));

        c.set(2, 6, AOCItem.from(new ItemBuilder(Material.FEATHER).setName(msg.showMessage("asNameGravity", false, false)).setLore(msg.showMessage("asLoreGravity", false, false)).build(), e -> {

        }));

        c.set(5, 0, AOCItem.from(HeadsUtil.getSpecifiedHead(BackArrow.class, p), e -> ArmorstandInv.getInv().open(p)));

        c.set(5, 1, AOCItem.from(HeadsUtil.getSpecifiedHead(Confirm.class, p), e -> {

        }));
    }

    @Override
    public void update(final Player p, final InvContents c) {

    }
}
