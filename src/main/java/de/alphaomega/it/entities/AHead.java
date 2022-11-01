package de.alphaomega.it.entities;

import de.alphaomega.it.AOCommands;
import de.alphaomega.it.utils.HeadBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class AHead {

    protected static final String HEX_COLOR = "#cf9c11";

    private UUID uuid;

    private String headName;
    private String actualName;
    private String color;
    private String base64;
    private String texture;

    private Map<String, String> names = new LinkedHashMap<>();
    private Map<String, List<String>> lores = new LinkedHashMap<>();

    public abstract Map<String, String> setNames();
    public abstract Map<String, List<String>> setLore();

    public AHead(final String name, final UUID uuid) {
        this.headName = name;
        this.uuid = uuid;

        final String color = AOCommands.getInstance().getBaseConfig().getString("default-hexColor-from-heads");
        this.color = color == null ? "#cf9c11" : color;
    }

    public ItemStack getHeadAsItemStack(final String locale) {
        final String newLocale = AOCommands.getInstance().getTranslations().get(locale) == null ? "en_US" : locale;
        return new HeadBuilder(this, newLocale).build();
    }

    public String getName(final String locale) {
        return getNames().get(AOCommands.getInstance().getTranslations().get(locale) == null ? "en_US" : locale);
    }

    public List<Component> getLore(final String locale) {
        final List<String> sLore = getLores().get(AOCommands.getInstance().getTranslations().get(locale) == null ? "en_US" : locale);
        final List<Component> cLore = new ArrayList<>();
        sLore.forEach(loreLine -> cLore.add(MiniMessage.miniMessage().deserialize(loreLine).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)));
        return cLore;
    }
}
