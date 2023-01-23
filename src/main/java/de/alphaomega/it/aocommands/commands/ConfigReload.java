package de.alphaomega.it.aocommands.commands;

import de.alphaomega.it.aocommands.AOCommands;
import de.alphaomega.it.aocommands.cmdhandler.Command;
import de.alphaomega.it.aocommands.cmdhandler.CommandArgs;
import de.alphaomega.it.aocommands.msghandler.Message;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public class ConfigReload {

    private final AOCommands aoCommands;

    public ConfigReload(final AOCommands aoCommands) {
        this.aoCommands = aoCommands;
    }

    @Command(
            name = "rconfig",
            aliases = {"reloadc", "reloadconfig"},
            permission = "aocommands.reloadconfig"
    )
    public void onCommand(final CommandArgs args) {
        final Player player = args.getPlayer();
        final Message msg = new Message(player);

        File configFile = new File(this.aoCommands.getDataFolder() + "/config.yml");
        if (configFile.exists()) {
            this.aoCommands.setBaseConfig(YamlConfiguration.loadConfiguration(configFile));
            msg.loadTranslationFiles();
            msg.sendMessage("configFileReloaded", false, true);
            return;
        }
        msg.sendMessage("configFileNotExists", false, true);
    }
}
