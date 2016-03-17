package com.shallowblue.shallowblue;

import java.util.ArrayList;


public class Queen extends Piece {

	public Queen(Position argPosition, Color color) {
		super(pieceType.QUEEN, argPosition, color);
		
	}

	@Override
	public boolean hasMoved() {
		// Unused
		return false;
	}

	@Override
	public boolean isPromoting() {
		// Unused as well
		return false;
	}

	@Override
	public ArrayList<Position> possibleMoves() {
		
		ArrayList<Position> result = new ArrayList<Position>();
		
		//left & right movements
		for (int i = 1; i <= 8; i++) if (i != getPosition().getColumn()) result.add(new Position(getPosition().getRow(), i));
		//up and down
		for (int i = 1; i <= 8; i++) if (i != getPosition().getRow()) result.add(new Position(i, getPosition().getColumn()));
		
		int x = getPosition().getRow();
		int y = getPosition().getColumn();
		
		//For diagonal movements:
		
		//top left
		for (int i = x, j = y; i <= 8 && j >= 1; i++, j--) {
			if (i != x && j != y) result.add(new Position(i,j));
		}
			
				
		//top right 
		for (int i = x, j = y; i <= 8 && j <= 8; i++, j++) {
			if (i != x && j != y) result.add(new Position(i,j));
		}
		
		//bottom left
		for (int i = x, j = y; i >= 1 && j >= 1; i--, j--) {
			if (i != x && j != y) result.add(new Position(i,j));
		}
		
		//bottom right
		for (int i = x, j = y; i >= 1 && j <= 8; i--, j++) {
			if (i != x && j != y) result.add(new Position(i,j));
		}
		
		
		return result;
	}

	@Override
	public String toString() {
		
		return "q";
	}

}
