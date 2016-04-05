package com.shallowblue.shallowblue;

/**
 * A chess Position is described by two integer values, a row and a column.
 * Row and Column values range from 1-8 (inclusive), to reflect a board's rank
 * Places in which Shallow Blue uses Position include:
 * a variable for Piece and another for each "tile" on the game board
 */

public class Position {

	private final int row;
	private final int column;

	public Position(int row, int column) {
		this.row = row;
		this.column = column;
	}

	public int getRow() {
		return this.row;
	}

	public int getColumn() {

		return this.column;
	}

	@Override
	public boolean equals(Object other) {
		if (other == null) return false;
		Position otherPosition = (Position)other;
		if (this.row != otherPosition.row) return false;
		if (this.column != otherPosition.column) return false;
		return true;
	}

	@Override
	public int hashCode() {
		int hashCode = 1;
		hashCode = 31 * hashCode + this.row;
		hashCode = 31 * hashCode + this.column;
		return hashCode;
	}

	@Override
	public String toString() {
		return "(" + this.row + "," + this.column + ")";
	}
}