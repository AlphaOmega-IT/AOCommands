package de.alphaomega.it.aocommands.invhandler;

import de.alphaomega.it.aocommands.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

@SuppressWarnings({"unchecked"})
public class AOCItem {

    private final ItemStack iS;
    private final Consumer<?> consumer;
    private final boolean legacy;

    private AOCItem(ItemStack iS, Consumer<?> consumer, boolean legacy) {
        this.iS = iS;
        this.consumer = consumer;
        this.legacy = legacy;
    }

    public static AOCItem empty() {
        return from(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setName("").build(), data -> {
        });
    }

    public static AOCItem empty(ItemStack iS) {
        return from(iS, data -> {
        });
    }

    public static AOCItem from(ItemStack iS, Consumer<AOCData> consumer) {
        return new AOCItem(iS, consumer, false);
    }

    public void run(InventoryClickEvent e) {
        if (!this.legacy) return;

        Consumer<InventoryClickEvent> legacyConsumer = (Consumer<InventoryClickEvent>) this.consumer;
        legacyConsumer.accept(e);
    }


    public void run(AOCData data) {
        if (this.legacy) {
            if (data.e() instanceof InventoryClickEvent event) {
                this.run(event);
            }
        } else {
            Consumer<AOCData> newConsumer = (Consumer<AOCData>) this.consumer;
            newConsumer.accept(data);
        }
    }

    public ItemStack getItem() {
        return this.iS;
    }
}
