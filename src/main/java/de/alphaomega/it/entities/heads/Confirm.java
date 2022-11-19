package de.alphaomega.it.entities.heads;


import de.alphaomega.it.entities.AHead;
import de.alphaomega.it.enums.LANGUAGE;
import org.bukkit.Bukkit;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Confirm extends AHead {

    public Confirm() {
        super();

        setHeadName("confirm");

        final String cUUID = this.aoCommands.getBaseConfig().getString(getHeadName() + "-uuid");
        setUuid(cUUID == null ? UUID.fromString("3e33162a-538f-49c1-a269-394288716902") : UUID.fromString(cUUID));

        final String cTexture = this.aoCommands.getBaseConfig().getString(getHeadName() + "-texture");
        setTexture(cTexture == null ? "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWUyOGJlYTlkMzkzNzNkMzZlZThmYTQwZWM4M2Y5YzNmY2RkOTMxNzUyMjc3NDNmOWRkMWY3ZTc4ODZiN2VlNSJ9fX0=" : cTexture);

        final String cHexColor = this.aoCommands.getBaseConfig().getString("default-hexColor-from-heads");
        setColor(cHexColor == null ? getHexColor() : cHexColor);

        final String actualName = this.aoCommands.getBaseConfig().getString(getHeadName() + "-name");
        setActualName(actualName == null ? "White Checkmark" : actualName);

        setBase64("");
        setNames(setNames());
        setLores(setLore());
    }

    @Override
    public Map<String, String> setNames() {
        getNames().put(LANGUAGE.GERMAN.getLocale(), this.aoCommands.getTranslations().get(LANGUAGE.GERMAN.getLocale()).getString("confirmName"));
        getNames().put(LANGUAGE.ENGLISH.getLocale(), this.aoCommands.getTranslations().get(LANGUAGE.ENGLISH.getLocale()).getString("confirmName"));
        getNames().put(LANGUAGE.BRAZILIAN.getLocale(), this.aoCommands.getTranslations().get(LANGUAGE.BRAZILIAN.getLocale()).getString("confirmName"));
        return getNames();
    }

    @Override
    public Map<String, List<String>> setLore() {
        try {
            getLores().put(LANGUAGE.GERMAN.getLocale(), this.aoCommands.getTranslations().get(LANGUAGE.GERMAN.getLocale()).getConfigurationSection("confirmLore").getValues(false).values().stream().map(Object::toString).toList());
            getLores().put(LANGUAGE.ENGLISH.getLocale(), this.aoCommands.getTranslations().get(LANGUAGE.ENGLISH.getLocale()).getConfigurationSection("confirmLore").getValues(false).values().stream().map(Object::toString).toList());
            getLores().put(LANGUAGE.BRAZILIAN.getLocale(), this.aoCommands.getTranslations().get(LANGUAGE.BRAZILIAN.getLocale()).getConfigurationSection("confirmLore").getValues(false).values().stream().map(Object::toString).toList());
        } catch (Exception exc) {
            setLores(new LinkedHashMap<>());
            Bukkit.getLogger().severe("ConfirmHead config keys are incorrect. Please make sure they are readable!");
        }
        return getLores();
    }
}
