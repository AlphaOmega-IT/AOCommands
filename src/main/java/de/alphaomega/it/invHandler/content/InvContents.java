package de.alphaomega.it.invHandler.content;

import de.alphaomega.it.invHandler.AOCItem;
import de.alphaomega.it.invHandler.AOInv;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public interface InvContents {
	
	AOInv inv();
	
	Pagination pagination();
	
	Optional<SlotIterator> iterator(String id);
	
	SlotIterator newIterator(String id, SlotIterator.Type type, int startRow, int startColumn);
	
	SlotIterator newIterator(SlotIterator.Type type, int startRow, int startColumn);
	
	SlotIterator newIterator(String id, SlotIterator.Type type, SlotPos startPos);
	
	SlotIterator newIterator(SlotIterator.Type type, SlotPos startPos);
	
	AOCItem[][] all();
	
	List<SlotPos> slots();
	
	Optional<SlotPos> firstEmpty();
	
	Optional<AOCItem> get(int index);
	
	Optional<AOCItem> get(int row, int column);
	
	Optional<AOCItem> get(SlotPos slotPos);

	InvContents set(int index, AOCItem item);
	
	InvContents set(final int row, final int column, final AOCItem item);
	
	InvContents set(final SlotPos slotPos, final AOCItem item);
	
	InvContents add(final AOCItem item);
	
	InvContents updateItem(final int index, final ItemStack iS);
	
	InvContents updateItem(int row, int column, ItemStack iS);
	
	InvContents updateItem(SlotPos slotPos, ItemStack iS);
	
	Optional<SlotPos> findItem(ItemStack item);
	
	Optional<SlotPos> findItem(AOCItem item);
	
	InvContents fill(AOCItem item);
	
	InvContents fillRow(int row, AOCItem item);
	
	InvContents fillColumn(int column, AOCItem item);

	<T> T property(final String name);
	
	<T> T property(final String name, final T def);
	
	InvContents setProperty(final String name, final Object value);
	
	void setEditable(final SlotPos slot, final boolean editable);
	
	boolean isEditable(final SlotPos slot);
	
	class Impl implements InvContents {
		
		private final AOInv inv;
		private final Player p;
		
		private final AOCItem[][] contents;
		
		private final Pagination pagination = new Pagination.Impl();
		private final Map<String, SlotIterator> iterators = new HashMap<>();
		private final Map<String, Object> properties = new HashMap<>();
		
		private final Set<SlotPos> editableSlots = new HashSet<>();
		
		public Impl(final AOInv inv, final Player p) {
			this.inv = inv;
			this.p = p;
			this.contents = new AOCItem[inv.getRows()][inv.getColumns()];
		}
		
		@Override
		public AOInv inv() {
			return inv;
		}
		
		@Override
		public Pagination pagination() {
			return pagination;
		}
		
		@Override
		public Optional<SlotIterator> iterator(final String id) {
			return Optional.ofNullable(this.iterators.get(id));
		}
		
		@Override
		public SlotIterator newIterator(final String id, final SlotIterator.Type type, final int startRow, final int startColumn) {
			SlotIterator iterator = new SlotIterator.Impl(this, inv, type, startRow, startColumn);
			
			this.iterators.put(id, iterator);
			return iterator;
		}
		
		@Override
		public SlotIterator newIterator(final String id, final SlotIterator.Type type, final SlotPos startPos) {
			return newIterator(id, type, startPos.row(), startPos.column());
		}
		
		@Override
		public SlotIterator newIterator(SlotIterator.Type type, int startRow, int startColumn) {
			return new SlotIterator.Impl(this, inv, type, startRow, startColumn);
		}
		
		@Override
		public SlotIterator newIterator(SlotIterator.Type type, SlotPos startPos) {
			return newIterator(type, startPos.row(), startPos.column());
		}
		
		@Override
		public AOCItem[][] all() {
			return contents;
		}
		
		@Override
		public List<SlotPos> slots() {
			List<SlotPos> slotPos = new ArrayList<>();
			for (int row = 0; row < contents.length; row++) {
				for (int column = 0; column < contents[0].length; column++) {
					slotPos.add(SlotPos.of(row, column));
				}
			}
			return slotPos;
		}
		
		@Override
		public Optional<SlotPos> firstEmpty() {
			for (int row = 0; row < contents.length; row++) {
				for (int column = 0; column < contents[0].length; column++) {
					if (this.get(row, column).isEmpty()) return Optional.of(new SlotPos(row, column));
				}
			}
			
			return Optional.empty();
		}
		
		@Override
		public Optional<AOCItem> get(int index) {
			int columnCount = this.inv.getColumns();
			
			return get(index / columnCount, index % columnCount);
		}
		
		@Override
		public Optional<AOCItem> get(int row, int column) {
			if (row < 0 || row >= contents.length) return Optional.empty();
			if (column < 0 || column >= contents[row].length) return Optional.empty();
			
			return Optional.ofNullable(contents[row][column]);
		}
		
		@Override
		public Optional<AOCItem> get(SlotPos slotPos) {
			return get(slotPos.row(), slotPos.column());
		}

		@Override
		public InvContents set(int index, AOCItem item) {
			int columnCount = this.inv.getColumns();
			
			return set(index / columnCount, index % columnCount, item);
		}
		
		@Override
		public InvContents set(int row, int column, AOCItem item) {
			if (row < 0 || row >= contents.length) return this;
			if (column < 0 || column >= contents[row].length) return this;
			
			contents[row][column] = item;
			update(row, column, item == null ? null : item.getItem());
			return this;
		}
		
		@Override
		public InvContents set(SlotPos slotPos, AOCItem item) {
			return set(slotPos.row(), slotPos.column(), item);
		}
		
		@Override
		public InvContents add(AOCItem item) {
			for (int row = 0; row < contents.length; row++) {
				for (int column = 0; column < contents[0].length; column++) {
					if (contents[row][column] == null) {
						set(row, column, item);
						return this;
					}
				}
			}
			
			return this;
		}
		
		@Override
		public InvContents updateItem(final int index, final ItemStack iS) {
			int columnCount = this.inv.getColumns();
			
			return updateItem(index / columnCount, index % columnCount, iS);
		}
		
		@Override
		public InvContents updateItem(final int row, final int column, final ItemStack iS) {
			Optional<AOCItem> optional = get(row, column);
			
			if (optional.isEmpty()) {
				set(row, column, AOCItem.empty(iS));
				return this;
			}

			final AOCItem newAOCItem = optional.get();
			set(row, column, newAOCItem);
			return this;
		}
		
		@Override
		public InvContents updateItem(final SlotPos slotPos, final ItemStack iS) {
			return updateItem(slotPos.row(), slotPos.column(), iS);
		}
		
		@Override
		public Optional<SlotPos> findItem(final ItemStack iS) {
			for (int row = 0; row < contents.length; row++) {
				for (int column = 0; column < contents[0].length; column++) {
					if (contents[row][column] != null && iS.isSimilar(contents[row][column].getItem())) {
						return Optional.of(SlotPos.of(row, column));
					}
				}
			}
			return Optional.empty();
		}
		
		@Override
		public Optional<SlotPos> findItem(final AOCItem AOCItem) {
			return findItem(AOCItem.getItem());
		}
		
		@Override
		public InvContents fill(final AOCItem item) {
			for (int row = 0; row < contents.length; row++)
				for (int column = 0; column < contents[row].length; column++)
					set(row, column, item);
			
			return this;
		}
		
		@Override
		public InvContents fillRow(final int row, final AOCItem item) {
			if (row < 0 || row >= contents.length) return this;
			
			for (int column = 0; column < contents[row].length; column++)
				set(row, column, item);
			
			return this;
		}
		
		@Override
		public InvContents fillColumn(final int column, final AOCItem item) {
			if (column < 0 || column >= contents[0].length) return this;
			
			for (int row = 0; row < contents.length; row++)
				set(row, column, item);
			
			return this;
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T> T property(final String name) {
			return (T) properties.get(name);
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public <T> T property(final String name, final T def) {
			return properties.containsKey(name) ? (T) properties.get(name) : def;
		}
		
		@Override
		public InvContents setProperty(final String name, final Object value) {
			properties.put(name, value);
			return this;
		}
		
		private void update(final int row, final int column, final ItemStack item) {
			if (! inv.getManager().getOpenedPlayers(inv).contains(p)) return;
			
			Inventory topInventory = p.getOpenInventory().getTopInventory();
			topInventory.setItem(inv.getColumns() * row + column, item);
		}
		
		@Override
		public void setEditable(final SlotPos slot, final boolean editable) {
			if (editable) editableSlots.add(slot);
			else editableSlots.remove(slot);
		}
		
		@Override
		public boolean isEditable(final SlotPos slot) {
			return editableSlots.contains(slot);
		}
		
	}
}
