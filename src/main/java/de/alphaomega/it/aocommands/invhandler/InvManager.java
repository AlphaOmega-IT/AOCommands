package de.alphaomega.it.aocommands.invhandler;


import de.alphaomega.it.aocommands.AOCommands;
import de.alphaomega.it.aocommands.invhandler.content.InvContents;
import de.alphaomega.it.aocommands.invhandler.content.InvProvider;
import de.alphaomega.it.aocommands.invhandler.content.SlotPos;
import de.alphaomega.it.aocommands.invhandler.opener.ChestInvOpener;
import de.alphaomega.it.aocommands.invhandler.opener.InvOpener;
import de.alphaomega.it.aocommands.invhandler.opener.SpecialInvOpener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;


public class InvManager {

    private final Map<Player, AOInv> inventories;
    private final Map<Player, InvContents> contents;
    private final Map<Player, BukkitRunnable> updateTasks;

    private final List<InvOpener> defaultOpeners;
    private final List<InvOpener> openers;

    public InvManager(final JavaPlugin pl) {
        this.inventories = new HashMap<>();
        this.contents = new HashMap<>();
        this.updateTasks = new HashMap<>();

        this.defaultOpeners = Arrays.asList(new ChestInvOpener(), new SpecialInvOpener());

        this.openers = new ArrayList<>();

        init(pl);
    }

    public void init(final JavaPlugin pl) {
        pl.getServer().getPluginManager().registerEvents(new InvListener(), pl);
    }

    public Optional<InvOpener> findOpener(final InventoryType type) {
        Optional<InvOpener> opInv = this.openers.stream().filter(opener -> opener.supports(type)).findAny();

        if (opInv.isEmpty()) {
            opInv = this.defaultOpeners.stream().filter(opener -> opener.supports(type)).findAny();
        }

        return opInv;
    }

    public void registerOpeners(final InvOpener... openers) {
        this.openers.addAll(Arrays.asList(openers));
    }

    public List<Player> getOpenedPlayers(final AOInv inv) {
        List<Player> list = new ArrayList<>();

        this.inventories.forEach((p, pInv) -> {
            if (inv.equals(pInv)) list.add(p);
        });

        return list;
    }

    public Optional<AOInv> getInventory(final Player p) {
        return Optional.ofNullable(this.inventories.get(p));
    }

    protected void setInventory(final Player p, final AOInv inv) {
        if (inv == null) this.inventories.remove(p);
        else this.inventories.put(p, inv);
    }

    public Optional<InvContents> getContents(final Player p) {
        return Optional.ofNullable(this.contents.get(p));
    }

    protected void setContents(final Player p, final InvContents c) {
        if (c == null) this.contents.remove(p);
        else this.contents.put(p, c);
    }

    protected void scheduleUpdateTask(final Player p, final AOInv inv) {
        PlayerInvTask task = new PlayerInvTask(p, inv.getProvider(), contents.get(p));
        task.runTaskTimer(AOCommands.getInstance(), 1, inv.getUpdateFrequency());
        this.updateTasks.put(p, task);
    }

    protected void cancelUpdateTask(final Player p) {
        if (updateTasks.containsKey(p)) {
            int bukkitTaskId = this.updateTasks.get(p).getTaskId();
            Bukkit.getScheduler().cancelTask(bukkitTaskId);
            this.updateTasks.remove(p);
        }
    }

    static class PlayerInvTask extends BukkitRunnable {

        private final Player p;
        private final InvProvider provider;
        private final InvContents contents;

        public PlayerInvTask(Player p, InvProvider provider, InvContents contents) {
            this.p = Objects.requireNonNull(p);
            this.provider = Objects.requireNonNull(provider);
            this.contents = Objects.requireNonNull(contents);
        }

        @Override
        public void run() {
            try {
                provider.update(this.p, this.contents);
            } catch (final Exception exc) {
                cancel();
            }
        }
    }

    @SuppressWarnings("unchecked")
    class InvListener implements Listener {

        @EventHandler(priority = EventPriority.LOWEST)
        public void onInventoryClick(final InventoryClickEvent e) {
            Player p = (Player) e.getWhoClicked();
            AOInv inv = inventories.get(p);
            Inventory bottomInv = p.getOpenInventory().getBottomInventory();
            Inventory topInv = p.getOpenInventory().getTopInventory();
            InvContents invContents = contents.get(p);

            if (inv == null) return;

            if ((e.getClick() == ClickType.SHIFT_LEFT || e.getClick() == ClickType.SHIFT_RIGHT) && e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY && e.getClickedInventory() == bottomInv) {
                int firstEmptyRow = topInv.firstEmpty() / 9;
                int firstEmptyColumn = topInv.firstEmpty() % 9;

                SlotPos firstEmptySlot = SlotPos.of(firstEmptyRow, firstEmptyColumn);
                if (!invContents.isEditable(firstEmptySlot)) {
                    e.setCancelled(true);
                    return;
                }

                inv.getListeners().stream().filter(listener -> listener.type() == InventoryClickEvent.class).forEach(listener -> ((de.alphaomega.it.aocommands.invhandler.InvListener<InventoryClickEvent>) listener).accept(e));

                invContents.get(firstEmptySlot).ifPresent(item -> item.run(new AOCData(e, p, e.getCurrentItem(), firstEmptySlot)));
            } else if (e.getClickedInventory() == topInv) {

                int row = e.getSlot() / 9;
                int column = e.getSlot() % 9;

                if (!inv.checkBounds(row, column)) return;

                SlotPos slot = SlotPos.of(row, column);

                if (!invContents.isEditable(slot) && (e.getAction() == InventoryAction.COLLECT_TO_CURSOR || e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY || e.getAction() == InventoryAction.NOTHING || e.getAction() == InventoryAction.HOTBAR_SWAP)) {

                    e.setCancelled(true);
                    return;
                }

                if (!invContents.isEditable(slot)) e.setCancelled(true);

                inv.getListeners().stream().filter(listener -> listener.type() == InventoryClickEvent.class).forEach(listener -> ((de.alphaomega.it.aocommands.invhandler.InvListener<InventoryClickEvent>) listener).accept(e));

                invContents.get(slot).ifPresent(item -> item.run(new AOCData(e, p, e.getCurrentItem(), slot)));

                // Don't update if the clicked slot is editable - prevent item glitching
                if (!invContents.isEditable(slot)) {
                    p.updateInventory();
                }
            }
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onInventoryDrag(final InventoryDragEvent e) {
            Player p = (Player) e.getWhoClicked();

            if (!inventories.containsKey(p)) return;

            AOInv inv = inventories.get(p);
            InvContents content = contents.get(p);

            for (int slot : e.getRawSlots()) {
                SlotPos pos = SlotPos.of(slot / 9, slot % 9);
                if (slot >= p.getOpenInventory().getTopInventory().getSize() || content.isEditable(pos)) continue;

                e.setCancelled(true);
                break;
            }

            inv.getListeners().stream().filter(listener -> listener.type() == InventoryDragEvent.class).forEach(listener -> ((de.alphaomega.it.aocommands.invhandler.InvListener<InventoryDragEvent>) listener).accept(e));
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onInventoryOpen(final InventoryOpenEvent e) {
            Player p = (Player) e.getPlayer();

            if (!inventories.containsKey(p)) return;

            AOInv inv = inventories.get(p);

            inv.getListeners().stream().filter(listener -> listener.type() == InventoryOpenEvent.class).forEach(listener -> ((de.alphaomega.it.aocommands.invhandler.InvListener<InventoryOpenEvent>) listener).accept(e));
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onInventoryClose(InventoryCloseEvent e) {
            Player p = (Player) e.getPlayer();

            if (!inventories.containsKey(p)) return;

            AOInv inv = inventories.get(p);

            try {
                inv.getListeners().stream().filter(listener -> listener.type() == InventoryCloseEvent.class).forEach(listener -> ((de.alphaomega.it.aocommands.invhandler.InvListener<InventoryCloseEvent>) listener).accept(e));
            } finally {
                if (inv.isCloseable()) {
                    e.getInventory().clear();
                    InvManager.this.cancelUpdateTask(p);

                    inventories.remove(p);
                    contents.remove(p);
                } else Bukkit.getScheduler().runTask(AOCommands.getInstance(), () -> p.openInventory(e.getInventory()));
            }
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onPlayerQuit(final PlayerQuitEvent e) {
            Player p = e.getPlayer();

            if (!inventories.containsKey(p)) return;

            AOInv inv = inventories.get(p);

            try {
                inv.getListeners().stream().filter(listener -> listener.type() == PlayerQuitEvent.class).forEach(listener -> ((de.alphaomega.it.aocommands.invhandler.InvListener<PlayerQuitEvent>) listener).accept(e));
            } finally {
                inventories.remove(p);
                contents.remove(p);
            }
        }

        @EventHandler(priority = EventPriority.LOW)
        public void onPluginDisable(PluginDisableEvent e) {
            new HashMap<>(inventories).forEach((p, inv) -> {
                try {
                    inv.getListeners().stream().filter(listener -> listener.type() == PluginDisableEvent.class).forEach(listener -> ((de.alphaomega.it.aocommands.invhandler.InvListener<PluginDisableEvent>) listener).accept(e));
                } finally {
                    inv.close(p);
                }
            });

            inventories.clear();
            contents.clear();
        }

    }

    class InvTask extends BukkitRunnable {

        @Override
        public void run() {
            new HashMap<>(inventories).forEach((p, inv) -> inv.getProvider().update(p, contents.get(p)));
        }

    }
}