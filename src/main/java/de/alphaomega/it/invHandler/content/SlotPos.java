package de.alphaomega.it.invHandler.content;

import java.util.Objects;

public record SlotPos(int row, int column) {

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof SlotPos slotPos)) return false;
		return row == slotPos.row && column == slotPos.column;
	}

	@Override
	public int hashCode() {
		return Objects.hash(row, column);
	}

	@Override
	public String toString() {
		return "SlotPos{" + "row=" + row + ", column=" + column + '}';
	}

	public static SlotPos of(int row, int column) {
		return new SlotPos(row, column);
	}
}
