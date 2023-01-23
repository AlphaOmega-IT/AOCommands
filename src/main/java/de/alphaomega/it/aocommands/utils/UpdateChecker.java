package de.alphaomega.it.aocommands.utils;

import de.alphaomega.it.aocommands.AOCommands;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;

public class UpdateChecker {

    private final AOCommands aoCommands;
    private final Integer ressourceId;

    public UpdateChecker(final AOCommands aoCommands, final Integer ressourceId) {
        this.aoCommands = aoCommands;
        this.ressourceId = ressourceId;
    }

    public void getVersion(final Consumer<String> c) {
        Bukkit.getScheduler().runTaskAsynchronously(this.aoCommands, () -> {
            try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.ressourceId).openStream();
                 Scanner scanner = new Scanner(inputStream)) {
                if (scanner.hasNext())
                    c.accept(scanner.next());
            } catch (IOException exception) {
                this.aoCommands.getLogger().info("[AOCommands] Unable to check for updates: " + exception.getMessage());
            }
        });
    }
}
