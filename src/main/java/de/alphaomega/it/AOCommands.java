package de.alphaomega.it;


import de.alphaomega.it.api.AOCommandsAPI;
import de.alphaomega.it.cmdHandler.CommandFramework;
import de.alphaomega.it.commands.*;
import de.alphaomega.it.invHandler.InvManager;
import de.alphaomega.it.inventories.ArmorstandSubInv;
import de.alphaomega.it.listeners.OnJoin;
import de.alphaomega.it.listeners.OnJoinInitPlayer;
import de.alphaomega.it.listeners.OnLeave;
import de.alphaomega.it.listeners.OnLeaveSavePlayer;
import de.alphaomega.it.msgHandler.Message;
import de.alphaomega.it.utils.UpdateChecker;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.hibernate.SessionFactory;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.UUID;
import java.util.logging.Level;

@Getter
@Setter
public class AOCommands extends JavaPlugin {


    private static AOCommands instance;
    private static AOCommandsAPI aoCommandsAPI;

    private HashMap<String, FileConfiguration> translations = new HashMap<>();

    private static final HashMap<String, String> noPermsMessage = new HashMap<>();

    private FileConfiguration baseConfig;
    private SessionFactory sF;

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

        if (baseConfig == null) {
            Bukkit.getLogger().log(Level.SEVERE, "[AOCommands] - Config.yml can not be found!");
            getServer().shutdown();
        }
    }

    @Override
    public void onEnable() {
        aoCommandsAPI = new AOCommandsAPI(this);
        this.manager = aoCommandsAPI.getInvManager();

        registerCommands();
        registerListener();

        translations = new Message().loadTranslationFiles();

        noPermsMessage.put("de_DE", translations.get("de_DE").getString("noPerms"));
        noPermsMessage.put("en_US", translations.get("en_US").getString("noPerms"));
        noPermsMessage.put("pt_BR", translations.get("pt_BR").getString("noPerms"));

        new UpdateChecker(this, 105963).getVersion(v -> {
            if (this.getDescription().getVersion().equals(v))
                getLogger().info("[AOCommands] There is not a new update available.");
            else
                getLogger().info("[AOCommands] There is a new update available.");
        });

        this.sF = AOCommandsAPI.sF;
        if (sF == null) {
            getLogger().log(Level.SEVERE, "[AOCommands] You need a database in order to run this plugin.");
            getLogger().log(Level.SEVERE, "[AOCommands] Go into your plugin folder, search for the database folder");
            getLogger().log(Level.SEVERE, "[AOCommands] and enter your credentials in the hibernate.cfg.xml file!");
            getLogger().log(Level.SEVERE, "[AOCommands] Make sure the database aocommands is created correctly!");
            onDisable();
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("[AOCommands] Plugin disabled!");
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
        cmdF.registerCommands(new Armorstand(this));
        cmdF.registerCommands(new ClearInv());
        cmdF.registerCommands(new Enderchest());
        cmdF.registerCommands(new Gamemode(this));
        cmdF.registerCommands(new ConfigReload(this));
        cmdF.registerCommands(new ItemEdit(this));
        cmdF.registerCommands(new Invsee(this));
        cmdF.registerCommands(new ClearChat());
    }

    private void registerListener() {
        final PluginManager plManager = getServer().getPluginManager();
        plManager.registerEvents(new OnJoin(), this);
        plManager.registerEvents(new OnLeave(), this);
        plManager.registerEvents(new ArmorstandSubInv(this, null), this);
        plManager.registerEvents(new OnJoinInitPlayer(aoCommandsAPI), this);
        plManager.registerEvents(new OnLeaveSavePlayer(aoCommandsAPI), this);
    }

    public static HashMap<String, String> getNoPermsMessage() { return noPermsMessage; }

    public static AOCommands getInstance() { return instance; }
}
