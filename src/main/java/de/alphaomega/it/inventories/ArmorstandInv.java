package de.alphaomega.it.inventories;



import de.alphaomega.it.AOCommands;
import de.alphaomega.it.entities.heads.BackArrow;
import de.alphaomega.it.invHandler.AOCItem;
import de.alphaomega.it.invHandler.AOInv;
import de.alphaomega.it.invHandler.content.InvContents;
import de.alphaomega.it.invHandler.content.InvProvider;
import de.alphaomega.it.msgHandler.Message;
import de.alphaomega.it.utils.HeadsUtil;
import de.alphaomega.it.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class ArmorstandInv implements InvProvider {

    private AOCommands pl;

    public AOInv getInv(final AOCommands pl) {
        this.pl = pl;
        return AOInv.builder()
                .manager(pl.getManager())
                .id("ArmorstandSubInv")
                .closeable(true)
                .size(3, 9)
                .title("<color:#d60946>Armorstand</color>")
                .provider(new ArmorstandInv())
                .build(pl);
    }

    @Override
    public void init(final Player p, final InvContents c) {
        final Message msg = new Message(p);
        c.fill(AOCItem.empty());

        c.set(1, 4, AOCItem.from(new ItemBuilder(Material.ARMOR_STAND).setName(msg.showMessage("asNameCreate", false, false)).setLore(msg.showMessage("asLoreCreate", false, false)).build(), e -> {
            ArmorStand as = (ArmorStand) p.getWorld().spawnEntity(p.getLocation(), EntityType.ARMOR_STAND);
            as.setVisible(false);
            as.setGravity(false);
            as.setBasePlate(false);
            new ArmorstandSubInv().getInv(as, pl).open(p);
        }));

        c.set(2, 0, AOCItem.from(HeadsUtil.getSpecifiedHead(BackArrow.class, p), e -> p.closeInventory()));
    }

    @Override
    public void update(final Player p, final InvContents c) {

    }
}
