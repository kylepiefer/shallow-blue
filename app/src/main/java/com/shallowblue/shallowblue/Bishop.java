package com.shallowblue.shallowblue;

import java.util.ArrayList;



public class Bishop extends Piece {

	public Bishop(Position argPosition, Color color) {
		super(argPosition, color, color == Color.WHITE ? R.drawable.white_bishop : R.drawable.black_bishop);
		
	}

	@Override
	public boolean hasMoved() {
		// UNUSED
		return false;
	}

	@Override
	public boolean isPromoting() {
		// UNUSED
		return false;
	}

	@Override
	public ArrayList<Position> possibleMoves() {
		ArrayList<Position> result = new ArrayList<Position>();
		
		int x = getPosition().getRow();
		int y = getPosition().getColumn();
		
		//top left diagonal range
		for (int i = x, j = y; i <= 7 && j >= 0 && i >= 0 && j <= 7; i++, j--) {
			if (i != x && j != y) result.add(new Position(i,j));
		}
			
				
		//top right 
		for (int i = x, j = y; i <= 7 && j <= 7 && i >= 0 && j >= 0; i++, j++) {
			if (i != x && j != y) result.add(new Position(i,j));
		}
		
		//bottom left
		for (int i = x, j = y; i >= 0 && j >= 0 && i <= 7 && j <= 7; i--, j--) {
			if (i != x && j != y) result.add(new Position(i,j));
		}
		
		//bottom right
		for (int i = x, j = y; i >= 0 && j <= 7 && i <= 7 && j >= 0; i--, j++) {
			if (i != x && j != y) result.add(new Position(i,j));
		}
		
		
		
		return result;
	}

	@Override
	public String toString() {
		
		return "b";
	}

}
