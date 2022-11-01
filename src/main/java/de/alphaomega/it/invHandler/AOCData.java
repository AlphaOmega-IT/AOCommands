package de.alphaomega.it.invHandler;

import de.alphaomega.it.invHandler.content.SlotPos;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

public record AOCData(Event e, Player p, ItemStack iS, SlotPos slot) {}
