package de.alphaomega.it.msgHandler;

import de.alphaomega.it.AOCommands;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Setter
@Getter
public class Message {

    private final String PREFIX = "prefix";
    private final Player p;
    private List<String> args = new ArrayList<>();

    public Message(final Player p) {
        this.p = p;
    }

    public static HashMap<String, FileConfiguration> loadTranslationFiles(final JavaPlugin pl) {
        File folder = new File(pl.getDataFolder() + "/translations");
        HashMap<String, FileConfiguration> translations = new HashMap<>();
        for (File f : Objects.requireNonNull(folder.listFiles())) {
            final String localeStr = f.getName().substring(9, 11);
            if (localeStr.equalsIgnoreCase("en")) {
                translations.put("en", YamlConfiguration.loadConfiguration(f));
            } else if (localeStr.equalsIgnoreCase("de")) {
                translations.put("de", YamlConfiguration.loadConfiguration(f));
            }
        }
        return translations;
    }

    public static HashMap<String, FileConfiguration> updateTranslations(final JavaPlugin pl) {
        return loadTranslationFiles(pl);
    }

    public void sendMessage(final String key, final boolean placeholder, final boolean prefix) {
        if (prefix && !placeholder) {
            this.p.sendMessage(MiniMessage.miniMessage().deserialize("<reset/>" + getPath(PREFIX, getFileConfig()) + " " + getPath(key, getFileConfig())));
        } else if (!placeholder) {
            this.p.sendMessage(MiniMessage.miniMessage().deserialize("<reset/>" + getPath(key, getFileConfig())));
        } else if (prefix) {
            this.p.sendMessage(MiniMessage.miniMessage().deserialize("<reset/>" + getPlaceholderMessage(key, true)));
        } else {
            this.p.sendMessage(MiniMessage.miniMessage().deserialize("<reset/>" + getPlaceholderMessage(key, false)));
        }
    }

    public String showMessage(final String key, final boolean placeholder, final boolean prefix) {
        if (prefix && !placeholder) {
            return getPath(PREFIX, getFileConfig()) + " " + getPath(key, getFileConfig());
        } else if (!placeholder) {
            return getPath(key, getFileConfig());
        } else if (prefix) {
            return getPlaceholderMessage(key, true);
        } else {
            return getPlaceholderMessage(key, false);
        }
    }

    private String getPlaceholderMessage(final String key, final boolean withPrefix) {
        String translatedMessageString;
        if (withPrefix) {
            translatedMessageString = getPath(PREFIX, getFileConfig()) + " " + getPath(key, getFileConfig());
        } else {
            translatedMessageString = getPath(key, getFileConfig());
        }
        int i = 0;
        while (translatedMessageString.contains("{" + i + "}") && i <= this.args.size()) {
            translatedMessageString = translatedMessageString.replaceFirst("\\{" + i + "}", this.args.get(i));
            i++;
        }
        return translatedMessageString;
    }

    private FileConfiguration getFileConfig() {
        if (AOCommands.getInstance().getTranslations().get(p.locale().getLanguage()) == null) {
            return AOCommands.getInstance().getTranslations().get("en");
        }
        return AOCommands.getInstance().getTranslations().get(p.locale().getLanguage());
    }

    private String getPath(final String key, final FileConfiguration fileConfig) {
        return "" + fileConfig.get(key);
    }
}
