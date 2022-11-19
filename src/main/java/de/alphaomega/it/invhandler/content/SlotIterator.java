package de.alphaomega.it.invhandler.content;

import de.alphaomega.it.invhandler.AOCItem;
import de.alphaomega.it.invhandler.AOInv;
import de.alphaomega.it.invhandler.util.Pattern;

import java.util.Optional;

public interface SlotIterator {

	enum Type {
		HORIZONTAL, VERTICAL
	}

	Optional<AOCItem> get();

	SlotIterator set(AOCItem item);

	SlotIterator previous();

	SlotIterator next();

	int row();

	SlotIterator row(int row);

	int column();

	SlotIterator column(int column);

	SlotIterator reset();

	boolean started();

	boolean ended();

	class Impl implements SlotIterator {

		private final InvContents c;
		private final AOInv inv;

		private final Type type;
		private boolean started = false;
		private final int endRow;
		private final int endColumn;
		private final int startRow;
		private final int startColumn;
		private int row, column;

		public Impl(final InvContents c, final AOInv inv, final Type type, final int startRow, final int startColumn) {

			this.c = c;
			this.inv = inv;

			this.type = type;

			this.endRow = this.inv.getRows() - 1;
			this.endColumn = this.inv.getColumns() - 1;

			this.startRow = this.row = startRow;
			this.startColumn = this.column = startColumn;
		}

		public Impl(final InvContents c, final AOInv inv, final Type type) {
			this(c, inv, type, 0, 0);
		}

		@Override
		public Optional<AOCItem> get() {
			return c.get(row, column);
		}

		@Override
		public SlotIterator set(final AOCItem item) {
			if (canPlace()) c.set(row, column, item);

			return this;
		}

		@Override
		public SlotIterator previous() {
			if (row == 0 && column == 0) {
				this.started = true;
				return this;
			}

			do {
				if (!this.started) {
					this.started = true;
				} else {
					switch (type) {
						case HORIZONTAL -> {
							column--;
							if (column == 0) {
								column = inv.getColumns() - 1;
								row--;
							}
						}
						case VERTICAL -> {
							row--;
							if (row == 0) {
								row = inv.getRows() - 1;
								column--;
							}
						}
					}
				}
			} while (!canPlace() && (row != 0 || column != 0));

			return this;
		}

		@Override
		public SlotIterator next() {
			if (ended()) {
				this.started = true;
				return this;
			}

			do {
				if (!this.started) {
					this.started = true;
				} else {
					switch (type) {
						case HORIZONTAL -> {
							column = ++column % inv.getColumns();
							if (column == 0) row++;
						}
						case VERTICAL -> {
							row = ++row % inv.getRows();
							if (row == 0) column++;
						}
					}
				}
			} while (!canPlace() && !ended());

			return this;
		}

		@Override
		public int row() {
			return row;
		}

		@Override
		public SlotIterator row(final int row) {
			this.row = row;
			return this;
		}

		@Override
		public int column() {
			return column;
		}

		@Override
		public SlotIterator column(final int column) {
			this.column = column;
			return this;
		}

		@Override
		public SlotIterator reset() {
			this.started = false;
			this.row = this.startRow;
			this.column = this.startColumn;
			return this;
		}

		@Override
		public boolean started() {
			return this.started;
		}

		@Override
		public boolean ended() {
			return row == endRow && column == endColumn;
		}

		private boolean canPlace() {
			boolean allowOverride = true;
			if (!allowOverride) {
				this.get();
			}
			return true;
		}

		private boolean checkPattern(final Pattern<Boolean> pattern, final int rowOffset, final int columnOffset) {
			if (pattern.isWrapAround()) {
				return pattern.getObject(row - rowOffset, column - columnOffset);
			} else {
				return row >= rowOffset && column >= columnOffset && row < (pattern.getRowCount() + rowOffset) && column < (pattern.getColumnCount() + columnOffset) && pattern.getObject(row - rowOffset, column - columnOffset);
			}
		}
	}
}