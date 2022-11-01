package de.alphaomega.it.entities.heads;

import de.alphaomega.it.AOCommands;
import de.alphaomega.it.entities.AHead;
import de.alphaomega.it.enums.LANGUAGE;
import org.bukkit.Bukkit;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BackArrow extends AHead {

    private final static String NAME = "backArrow";
    private final static String UUID_STRING = AOCommands.getInstance().getBaseConfig().getString(NAME + "-uuid");
    private final static String TEXTURE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODE2ZWEzNGE2YTZlYzVjMDUxZTY5MzJmMWM0NzFiNzAxMmIyOThkMzhkMTc5ZjFiNDg3YzQxM2Y1MTk1OWNkNCJ9fX0=";
    private final static String DEFAULT_NAME = "Oak Wood Backward II";

    public BackArrow() {
        super(NAME, UUID_STRING == null ? UUID.fromString("ca6160ab-3d3a-4cd2-a4d3-a55a4f022d85") : UUID.fromString(UUID_STRING));

        final String cTexture = AOCommands.getInstance().getBaseConfig().getString(NAME + "-texture");
        setTexture(cTexture == null ? TEXTURE : cTexture);

        final String cHexColor = AOCommands.getInstance().getBaseConfig().getString("default-hexColor-from-heads");
        setColor(cHexColor == null ? AHead.HEX_COLOR : cHexColor);

        final String actualName = AOCommands.getInstance().getBaseConfig().getString(NAME + "-name");
        setActualName(actualName == null ? DEFAULT_NAME : actualName);
        setBase64("");
        setNames(setNames());
        setLores(setLore());
    }

    @Override
    public Map<String, String> setNames() {
        getNames().put(LANGUAGE.GERMAN.getLocale(), AOCommands.getInstance().getTranslations().get(LANGUAGE.GERMAN.getLocale()).getString("backArrowName"));
        getNames().put(LANGUAGE.ENGLISH.getLocale(), AOCommands.getInstance().getTranslations().get(LANGUAGE.ENGLISH.getLocale()).getString("backArrowName"));
        getNames().put(LANGUAGE.BRAZILIAN.getLocale(), AOCommands.getInstance().getTranslations().get(LANGUAGE.BRAZILIAN.getLocale()).getString("backArrowName"));
        return getNames();
    }

    @Override
    public Map<String, List<String>> setLore() {
        try {
            getLores().put(LANGUAGE.GERMAN.getLocale(), AOCommands.getInstance().getTranslations().get(LANGUAGE.GERMAN.getLocale()).getConfigurationSection("backArrowLore").getValues(false).values().stream().map(Object::toString).toList());
            getLores().put(LANGUAGE.ENGLISH.getLocale(), AOCommands.getInstance().getTranslations().get(LANGUAGE.ENGLISH.getLocale()).getConfigurationSection("backArrowLore").getValues(false).values().stream().map(Object::toString).toList());
            getLores().put(LANGUAGE.BRAZILIAN.getLocale(), AOCommands.getInstance().getTranslations().get(LANGUAGE.BRAZILIAN.getLocale()).getConfigurationSection("backArrowLore").getValues(false).values().stream().map(Object::toString).toList());
        } catch (Exception exc) {
            setLores(new LinkedHashMap<>());
            Bukkit.getLogger().severe("BackArrow config keys are incorrect. Please make sure they are readable!");
        }
        return getLores();
    }
}
