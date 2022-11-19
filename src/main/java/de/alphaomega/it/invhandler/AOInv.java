package de.alphaomega.it.invhandler;


import de.alphaomega.it.AOCommands;
import de.alphaomega.it.invhandler.content.InvContents;
import de.alphaomega.it.invhandler.content.InvProvider;
import de.alphaomega.it.invhandler.content.SlotPos;
import de.alphaomega.it.invhandler.opener.InvOpener;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.*;

@SuppressWarnings("unchecked")
@Getter
@Setter
public class AOInv {

	private String id;
	private String title;
	private InventoryType type;
	private int rows, columns;
	private boolean closeable;
	private int updateFrequency;

	private InvProvider provider;
	private AOInv parent;

	private List<InvListener<? extends Event>> listeners;
	private final InvManager manager;

	private AOInv(final InvManager manager) {
		this.manager = manager;
	}

	public void open(final Player player) {
		open(player, 0, Collections.EMPTY_MAP);
	}

	public Inventory open(final Player player, final int page) {
		return open(player, page, Collections.EMPTY_MAP);
	}

	public Inventory open(final Player player, final Map<String, Object> properties) {
		return open(player, 0, properties);
	}

	public Inventory open(final Player player, final int page, final Map<String, Object> properties) {
		Optional<AOInv> oldInv = this.manager.getInventory(player);

		oldInv.ifPresent(inv -> {
			inv.getListeners().stream().filter(listener -> listener.type() == InventoryCloseEvent.class).forEach(listener -> ((InvListener<InventoryCloseEvent>) listener).accept(new InventoryCloseEvent(player.getOpenInventory())));

			this.manager.setInventory(player, null);
		});

		InvContents c = new InvContents.Impl.Impl(this, player);
		c.pagination().page(page);
		properties.forEach(c::setProperty);
		this.manager.setContents(player, c);
		this.provider.init(player, c);

		InvOpener opener = this.manager.findOpener(type).orElseThrow(() -> new IllegalStateException("No opener found for the inventory type " + type.name()));
		Inventory inv = opener.open(this, player);

		this.manager.setInventory(player, this);
		this.manager.scheduleUpdateTask(player, this);

		return inv;
	}

	public void close(Player player) {
		listeners.stream().filter(listener -> listener.type() == InventoryCloseEvent.class).forEach(listener -> ((InvListener<InventoryCloseEvent>) listener).accept(new InventoryCloseEvent(player.getOpenInventory())));

		this.manager.setInventory(player, null);
		player.closeInventory();

		this.manager.setContents(player, null);
		this.manager.cancelUpdateTask(player);
	}

	public boolean checkBounds(int row, int col) {
		if (row < 0 || col < 0) return false;
		return row < this.rows && col < this.columns;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder {

		private String id = "unknown";
		private String title = "";
		private InventoryType type = InventoryType.CHEST;
		private Optional<Integer> rows = Optional.empty();
		private Optional<Integer> columns = Optional.empty();
		private boolean closeable = true;
		private int updateFrequency = 1;

		private InvManager manager;
		private InvProvider provider;
		private AOInv parent;

		private final List<InvListener<? extends Event>> listeners = new ArrayList<>();

		private Builder() {}

		public Builder id(final String id) {
			this.id = id;
			return this;
		}

		public Builder title(final String title) {
			this.title = title;
			return this;
		}

		public Builder type(final InventoryType type) {
			this.type = type;
			return this;
		}

		public Builder size(final int rows, final int columns) {
			this.rows = Optional.of(rows);
			this.columns = Optional.of(columns);
			return this;
		}

		public Builder closeable(final boolean closeable) {
			this.closeable = closeable;
			return this;
		}

		public Builder updateFrequency(final int frequency) {
			this.updateFrequency = frequency;
			return this;
		}

		public Builder provider(final InvProvider provider) {
			this.provider = provider;
			return this;
		}

		public Builder parent(final AOInv parent) {
			this.parent = parent;
			return this;
		}

		public Builder listener(final InvListener<? extends Event> listener) {
			this.listeners.add(listener);
			return this;
		}

		public Builder manager(final InvManager manager) {
			this.manager = manager;
			return this;
		}

		public AOInv build(final AOCommands pl) {
			if (this.provider == null)
				throw new IllegalStateException("Provider is not set!");

			if (this.manager == null) {
				this.manager = pl.getManager();
				if (this.manager == null) {
					throw new IllegalStateException("InvManager is not set!");
				}
			}

			AOInv inv = new AOInv(manager);
			inv.id = this.id;
			inv.title = this.title;
			inv.type = this.type;
			inv.rows = this.rows.orElseGet(() -> getDefaultDimensions(type).row());
			inv.columns = this.columns.orElseGet(() -> getDefaultDimensions(type).column());
			inv.closeable = this.closeable;
			inv.updateFrequency = this.updateFrequency;
			inv.provider = this.provider;
			inv.parent = this.parent;
			inv.listeners = this.listeners;
			return inv;
		}

		private SlotPos getDefaultDimensions(InventoryType type) {
			InvOpener opener = this.manager.findOpener(type).orElse(null);
			if (opener == null) throw new IllegalStateException("InvOpener does not exists for type: " + type);

			SlotPos size = opener.defaultSize(type);
			if (size == null)
				throw new IllegalStateException(String.format("%s returned null for input InventoryType %s", opener.getClass().getSimpleName(), type));

			return size;
		}

	}
}
