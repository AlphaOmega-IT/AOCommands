package de.alphaomega.it.aocommands;


import de.alphaomega.it.aocommands.api.AOCommandsAPI;
import de.alphaomega.it.aocommands.cmdhandler.CommandFramework;
import de.alphaomega.it.aocommands.commands.*;
import de.alphaomega.it.aocommands.inventories.ArmorstandSubInv;
import de.alphaomega.it.aocommands.invhandler.InvManager;
import de.alphaomega.it.aocommands.listeners.OnJoin;
import de.alphaomega.it.aocommands.listeners.OnJoinInitPlayer;
import de.alphaomega.it.aocommands.listeners.OnLeave;
import de.alphaomega.it.aocommands.listeners.OnLeaveSavePlayer;
import de.alphaomega.it.aocommands.msghandler.Message;
import de.alphaomega.it.aocommands.utils.UpdateChecker;
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
import java.util.*;
import java.util.logging.Level;

@Getter
@Setter
public class AOCommands extends JavaPlugin {


    private static AOCommands instance;
    private final HashMap<String, String> noPermsMessage = new HashMap<>();
    private final HashMap<UUID, ArmorStand> armorStands = new LinkedHashMap<>();
    private final HashMap<UUID, Boolean> isUsingAnvil = new LinkedHashMap<>();
    private final Set<UUID> vanishedPlayers = new LinkedHashSet<>();
    private AOCommandsAPI aoCommandsAPI;
    private HashMap<String, FileConfiguration> translations = new HashMap<>();
    private FileConfiguration baseConfig;
    private SessionFactory sF;
    private InvManager manager;

    public static AOCommands getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {
        instance = this;

        if (!getDataFolder().exists()) {
            if (getDataFolder().mkdir()) {
                saveResource("translations/", false);
                Bukkit.getLogger().log(Level.INFO, "[AOCommands] - Translations got created");
            }
        }

        if (getDataFolder().exists()) {
            if (new File(getDataFolder() + "/libs/").mkdir())
                Bukkit.getLogger().log(Level.INFO, "[AOCommands] - Libraries got created");
        }

        saveResource("translations/language_de_DE.yml", true);
        saveResource("translations/language_en_US.yml", true);
        saveResource("translations/language_pt_BR.yml", true);

        if (getResource("config.yml") != null)
            saveResource("config.yml", true);

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
        this.aoCommandsAPI = new AOCommandsAPI(this);
        this.manager = this.aoCommandsAPI.getInvManager();

        registerCommands();
        registerListener();

        this.translations = new Message().loadTranslationFiles();

        this.noPermsMessage.put("de_DE", this.translations.get("de_DE").getString("noPerms"));
        this.noPermsMessage.put("en_US", this.translations.get("en_US").getString("noPerms"));
        this.noPermsMessage.put("pt_BR", this.translations.get("pt_BR").getString("noPerms"));

        new UpdateChecker(this, 105963).getVersion(v -> {
            if (getDescription().getVersion().equals(v))
                getLogger().info("[AOCommands] There is not a new update available.");
            else
                getLogger().info("[AOCommands] There is a new update available.");
        });

        this.sF = aoCommandsAPI.getSF();
        if (this.sF == null) {
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
        cmdF.registerCommands(new Vanish(this));
        cmdF.registerCommands(new Rename(this));
        cmdF.registerCommands(new Hat());
        cmdF.registerCommands(new Feed(this));
        cmdF.registerCommands(new Repair(this));
        cmdF.registerCommands(new Give());
        cmdF.registerCommands(new Workbench());
        cmdF.registerCommands(new Msg());
        cmdF.registerCommands(new Armorstand(this));
        cmdF.registerCommands(new ClearInv());
        cmdF.registerCommands(new Enderchest(this));
        cmdF.registerCommands(new Gamemode(this));
        cmdF.registerCommands(new ConfigReload(this));
        cmdF.registerCommands(new ItemEdit(this));
        cmdF.registerCommands(new Invsee(this));
        cmdF.registerCommands(new ClearChat());
        cmdF.registerCommands(new Spawn(this));
    }

    private void registerListener() {
        final PluginManager plManager = getServer().getPluginManager();
        plManager.registerEvents(new OnJoin(this), this);
        plManager.registerEvents(new OnLeave(), this);
        plManager.registerEvents(new ArmorstandSubInv(this, null), this);
        plManager.registerEvents(new OnJoinInitPlayer(this), this);
        plManager.registerEvents(new OnLeaveSavePlayer(this), this);
    }
}
