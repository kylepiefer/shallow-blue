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

	public Position(int x, int y) {
		row = x;
		column = y;
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {

		return column;
	}
}