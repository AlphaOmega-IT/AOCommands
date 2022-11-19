package de.alphaomega.it.utils;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import de.alphaomega.it.entities.AHead;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

@Getter
@Setter
public class HeadBuilder extends ItemBuilder {

    final static Material PLAYER_HEAD = Material.PLAYER_HEAD;

    private final SkullMeta sM;

    public HeadBuilder(final Integer amount) {
        super(PLAYER_HEAD, amount);
        this.sM = (SkullMeta) getIS().getItemMeta();
    }

    public HeadBuilder() {
        super(PLAYER_HEAD);
        this.sM = (SkullMeta) getIS().getItemMeta();
    }

    public HeadBuilder(final AHead head, final String locale) {
        super(PLAYER_HEAD);
        if (head == null)
            sM = setSM(getIS().getItemMeta());
        else {
            if (head.getBase64() != null && !head.getBase64().equals(""))
                setIS(ItemStackSerialization.getItemStackFromBase64String(head.getBase64()));
            else
                setIS(new ItemBuilder(Material.PLAYER_HEAD).build());
            sM = setSM(getIS().getItemMeta());

            PlayerProfile pProfile = Bukkit.createProfile(head.getUuid(), head.getActualName());
            pProfile.setProperty(new ProfileProperty("textures", head.getTexture()));
            sM.setPlayerProfile(pProfile);

            sM.displayName(MiniMessage.miniMessage().deserialize("<color:" + head.getColor() + ">" + head.getName(locale) + "</color>").decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            sM.lore(head.getLore(locale));
            ItemStack iS = getIS();
            iS.setItemMeta(sM);
            setIS(iS);
        }
    }

    public ItemStack build() {
        return getIS();
    }

    private SkullMeta setSM(final ItemMeta iM) {
        return (SkullMeta) iM;
    }
}
