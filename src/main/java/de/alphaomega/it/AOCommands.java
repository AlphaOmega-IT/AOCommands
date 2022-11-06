package de.alphaomega.it;

import de.alphaomega.it.cmdHandler.CommandFramework;
import de.alphaomega.it.commands.*;
import de.alphaomega.it.invHandler.InvManager;
import de.alphaomega.it.inventories.ArmorstandSubInv;
import de.alphaomega.it.listeners.OnJoin;
import de.alphaomega.it.listeners.OnLeave;
import de.alphaomega.it.msgHandler.Message;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.UUID;
import java.util.logging.Level;

@Getter
@Setter
public class AOCommands extends JavaPlugin {

    private static AOCommands instance;
    private HashMap<String, FileConfiguration> translations = new HashMap<>();

    private static final HashMap<String, String> noPermsMessage = new HashMap<>();
    private FileConfiguration baseConfig;

    private final HashMap<UUID, ArmorStand> armorStands = new LinkedHashMap<>();
    private final HashMap<UUID, Boolean> isUsingAnvil = new LinkedHashMap<>();

    private InvManager manager;

    @Override
    public void onLoad() {
        instance = this;

        if (!this.getDataFolder().exists()) {
            if (this.getDataFolder().mkdir()) {
                this.saveResource("translations/", false);
                Bukkit.getLogger().log(Level.INFO, "[AOCommands] - Translations got created");
            }
        }
        this.saveResource("translations/language_de_DE.yml", true);
        this.saveResource("translations/language_en_US.yml", true);
        this.saveResource("translations/language_pt_BR.yml", true);

        if (this.getResource("config.yml") != null)
            this.saveResource("config.yml", true);

        File configFile = new File(this.getDataFolder() + "/config.yml");
        if (configFile.exists())
            baseConfig = YamlConfiguration.loadConfiguration(configFile);


        translations = Message.loadTranslationFiles(this);

        noPermsMessage.put("de_DE", translations.get("de_DE").getString("noPerms"));
        noPermsMessage.put("en_US", translations.get("en_US").getString("noPerms"));
        noPermsMessage.put("pt_BR", translations.get("pt_BR").getString("noPerms"));

        if (baseConfig == null) {
            Bukkit.getLogger().log(Level.SEVERE, "[AOCommands] - Config.yml can not be found!");
            getServer().shutdown();
        }

    }

    @Override
    public void onEnable() {
        registerCommands();
        registerListener();

        this.manager = new InvManager();
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
        cmdF.registerCommands(new Rename(this));
        cmdF.registerCommands(new Hat());
        cmdF.registerCommands(new Feed(this));
        cmdF.registerCommands(new Repair(this));
        cmdF.registerCommands(new Give());
        cmdF.registerCommands(new Workbench());
        cmdF.registerCommands(new Msg());
        cmdF.registerCommands(new Armorstand());
        cmdF.registerCommands(new ClearInv());
        cmdF.registerCommands(new Enderchest());
        cmdF.registerCommands(new Gamemode(this));
        cmdF.registerCommands(new ConfigReload(this));
    }

    private void registerListener() {
        PluginManager plManager = getServer().getPluginManager();
        plManager.registerEvents(new OnJoin(), this);
        plManager.registerEvents(new OnLeave(), this);
        plManager.registerEvents(new ArmorstandSubInv(null), this);
    }

    public static AOCommands getInstance() { return instance; }

    public static HashMap<String, String> getNoPermsMessage() { return noPermsMessage; }
}
