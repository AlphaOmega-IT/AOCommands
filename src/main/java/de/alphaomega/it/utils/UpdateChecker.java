package de.alphaomega.it.utils;

import de.alphaomega.it.AOCommands;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;

public record UpdateChecker(AOCommands pl, Integer resourceId) {

    public void getVersion(final Consumer<String> c) {
        Bukkit.getScheduler().runTaskAsynchronously(this.pl, () -> {
            try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.resourceId).openStream();
                 Scanner scanner = new Scanner(inputStream)) {
                if (scanner.hasNext())
                    c.accept(scanner.next());
            } catch (IOException exception) {
                pl.getLogger().info("[AOCommands] Unable to check for updates: " + exception.getMessage());
            }
        });
    }
}
