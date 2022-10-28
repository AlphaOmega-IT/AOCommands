package de.alphaomega.it;

import de.alphaomega.it.cmdHandler.CommandFramework;
import de.alphaomega.it.commands.Fly;
import de.alphaomega.it.commands.Heal;
import de.alphaomega.it.commands.Vanish;
import de.alphaomega.it.commands.Vote;
import de.alphaomega.it.listeners.OnJoin;
import de.alphaomega.it.msgHandler.Message;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Level;

@Getter
public class AOCommands extends JavaPlugin {

    private static AOCommands instance;
    private HashMap<String, FileConfiguration> translations = new HashMap<>();

    private static final HashMap<String, String> noPermsMessage = new HashMap<>();

    @Override
    public void onLoad() {
        instance = this;

        if (!this.getDataFolder().exists()) {
            if (this.getDataFolder().mkdir()) {
                this.saveResource("translations/", false);
                Bukkit.getLogger().log(Level.INFO, "[AOCommands] - Translations got created");
            }
        }
        this.saveResource("translations/language_de.yml", true);
        this.saveResource("translations/language_en.yml", true);


        translations = Message.loadTranslationFiles(this);

        noPermsMessage.put("de", translations.get("de").getString("noPerms"));
        noPermsMessage.put("en", translations.get("en").getString("noPerms"));
    }

    @Override
    public void onEnable() {
        registerCommands();
        registerListener();
    }

    @Override
    public void onDisable() {

    }

    private void registerCommands() {
        CommandFramework cmdF = new CommandFramework(this);
        cmdF.registerCommands(new Fly());
        cmdF.registerCommands(new Heal());
        cmdF.registerCommands(new Vote());
        cmdF.registerCommands(new Vanish());
    }

    private void registerListener() {
        PluginManager plManager = getServer().getPluginManager();
        plManager.registerEvents(new OnJoin(), this);
    }

    public static AOCommands getInstance() { return instance; }

    public static HashMap<String, String> getNoPermsMessage() { return noPermsMessage; }
}
