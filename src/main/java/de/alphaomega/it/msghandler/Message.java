package de.alphaomega.it.msghandler;


import de.alphaomega.it.AOCommands;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Setter
@Getter
@NoArgsConstructor

public class Message {

    private final String PREFIX = "prefix";
    private Player player;
    private List<String> args = new ArrayList<>();

    private AOCommands aoCommands = AOCommands.getInstance();

    public Message(final Player player) {
        this.player = player;
    }

    public HashMap<String, FileConfiguration> loadTranslationFiles() {
        File folder = new File(this.aoCommands.getDataFolder() + "/translations");
        HashMap<String, FileConfiguration> translations = new HashMap<>();
        for (File f : Objects.requireNonNull(folder.listFiles())) {
            final String localeStr = f.getName().substring(9, 14);
            if (localeStr.equalsIgnoreCase("en_US")) {
                translations.put("en_US", YamlConfiguration.loadConfiguration(f));
            } else if (localeStr.equalsIgnoreCase("de_DE")) {
                translations.put("de_DE", YamlConfiguration.loadConfiguration(f));
            } else if (localeStr.equalsIgnoreCase("pt_BR")) {
                translations.put("pt_BR", YamlConfiguration.loadConfiguration(f));
            }
        }
        return translations;
    }

    public HashMap<String, FileConfiguration> updateTranslations() {
        return loadTranslationFiles();
    }

    public void sendMessage(final String key, final boolean placeholder, final boolean prefix) {
        if (this.player == null) return;
        if (prefix && !placeholder)
            this.player.sendMessage(MiniMessage.miniMessage().deserialize("<reset/>" + getPath(PREFIX, getFileConfig()) + " " + getPath(key, getFileConfig())));
        else if (!placeholder)
            this.player.sendMessage(MiniMessage.miniMessage().deserialize("<reset/>" + getPath(key, getFileConfig())));
        else if (prefix)
            this.player.sendMessage(MiniMessage.miniMessage().deserialize("<reset/>" + getPlaceholderMessage(key, true)));
        else
            this.player.sendMessage(MiniMessage.miniMessage().deserialize("<reset/>" + getPlaceholderMessage(key, false)));
    }

    public String showMessage(final String key, final boolean placeholder, final boolean prefix) {
        if (this.player == null) return "";
        if (prefix && !placeholder)
            return getPath(PREFIX, getFileConfig()) + " " + getPath(key, getFileConfig());
        else if (!placeholder)
            return getPath(key, getFileConfig());
        else if (prefix)
            return getPlaceholderMessage(key, true);
        else
            return getPlaceholderMessage(key, false);
    }

    private String getPlaceholderMessage(final String key, final boolean withPrefix) {
        String translatedMessageString;
        if (withPrefix)
            translatedMessageString = getPath(PREFIX, getFileConfig()) + " " + getPath(key, getFileConfig());
        else
            translatedMessageString = getPath(key, getFileConfig());
        int i = 0;
        while (translatedMessageString.contains("{" + i + "}") && i <= this.args.size()) {
            translatedMessageString = translatedMessageString.replaceFirst("\\{" + i + "}", this.args.get(i));
            i++;
        }
        return translatedMessageString;
    }


    private FileConfiguration getFileConfig() {
        if (player == null) return this.aoCommands.getTranslations().get("en_US");
        if (this.aoCommands.getTranslations().get(this.player.locale().toString()) == null)
            return this.aoCommands.getTranslations().get("en_US");

        return this.aoCommands.getTranslations().get(this.player.locale().toString());
    }

    private String getPath(final String key, final FileConfiguration fileConfig) {
        return "" + fileConfig.get(key);
    }
}
