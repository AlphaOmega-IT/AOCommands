package de.alphaomega.it.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;

public class OnAnvilRename implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onRename(final PrepareAnvilEvent e) {
        try {
            e.getView().getPlayer().sendMessage(e.getInventory().getRenameText());
        } catch (final Exception ignored) {}
    }
}
