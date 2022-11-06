package de.alphaomega.it.commands;

import de.alphaomega.it.AOCommands;
import de.alphaomega.it.cmdHandler.Command;
import de.alphaomega.it.cmdHandler.CommandArgs;
import de.alphaomega.it.msgHandler.Message;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public record ConfigReload(AOCommands pl) {

    @Command(
            name = "rconfig",
            aliases = {"reloadc", "reloadconfig"},
            permission = "aocommands.reloadconfig"
    )
    public void onCommand(final CommandArgs args) {
        final Player p = args.getPlayer();
        final Message msg = new Message(p);

        File configFile = new File(pl.getDataFolder() + "/config.yml");
        if (configFile.exists()) {
            pl.setBaseConfig(YamlConfiguration.loadConfiguration(configFile));
            msg.sendMessage("configFileReloaded", false, true);
            return;
        }
        msg.sendMessage("configFileNotExists", false, true);
    }
}
