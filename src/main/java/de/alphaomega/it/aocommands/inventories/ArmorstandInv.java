package de.alphaomega.it.aocommands.inventories;


import de.alphaomega.it.aocommands.AOCommands;
import de.alphaomega.it.aocommands.entities.heads.BackArrow;
import de.alphaomega.it.aocommands.invhandler.AOCItem;
import de.alphaomega.it.aocommands.invhandler.AOInv;
import de.alphaomega.it.aocommands.invhandler.content.InvContents;
import de.alphaomega.it.aocommands.invhandler.content.InvProvider;
import de.alphaomega.it.aocommands.msghandler.Message;
import de.alphaomega.it.aocommands.utils.HeadsUtil;
import de.alphaomega.it.aocommands.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class ArmorstandInv implements InvProvider {

    private final AOCommands aoCommands;

    public ArmorstandInv(final AOCommands aoCommands) {
        this.aoCommands = aoCommands;
    }

    public static AOInv getInv(final AOCommands aoCommands) {
        return AOInv.builder()
                .manager(aoCommands.getManager())
                .id("ArmorstandSubInv")
                .closeable(true)
                .size(3, 9)
                .title("<color:#d60946>Armorstand</color>")
                .provider(new ArmorstandInv(aoCommands))
                .build(aoCommands);
    }

    @Override
    public void init(final Player player, final InvContents c) {
        final Message msg = new Message(player);
        c.fill(AOCItem.empty());

        c.set(1, 4, AOCItem.from(new ItemBuilder(Material.ARMOR_STAND).setName(msg.showMessage("asNameCreate", false, false)).setLore(msg.showMessage("asLoreCreate", false, false)).build(), e -> {
            ArmorStand as = (ArmorStand) player.getWorld().spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);
            as.setVisible(false);
            as.setGravity(false);
            as.setBasePlate(false);
            ArmorstandSubInv.getInv(as, this.aoCommands).open(player);
        }));

        c.set(2, 0, AOCItem.from(new HeadsUtil().getSpecifiedHead(BackArrow.class, player), e -> player.closeInventory()));
    }

    @Override
    public void update(final Player p, final InvContents c) {

    }
}
