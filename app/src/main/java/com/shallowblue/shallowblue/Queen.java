package com.shallowblue.shallowblue;

import java.util.ArrayList;


public class Queen extends Piece {

	public Queen(Position argPosition, Color color) {
		super(argPosition, color, color == Color.WHITE ? R.drawable.white_queen : R.drawable.black_queen);
	}

	public Queen(Queen q) {
		super(q.getPosition(), q.getColor(), q.getColor() == Color.WHITE ? R.drawable.white_queen : R.drawable.black_queen);
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
		if (this.getPosition() == null) return result;

		//if the queen's position is illegal, no moves should be added:
		int checkRow = getPosition().getRow();
		int checkCol = getPosition().getColumn();
		if (checkRow > 7 || checkRow < 0) return result;
		if (checkCol > 7 || checkCol < 0) return result;
		
		//left & right movements
		for (int i = 0; i <= 7; i++)
			if (i != getPosition().getColumn()) result.add(new Position(getPosition().getRow(), i));
		//up and down
		for (int i = 0; i <= 7; i++)
			if (i != getPosition().getRow()) result.add(new Position(i, getPosition().getColumn()));
		
		int x = getPosition().getRow();
		int y = getPosition().getColumn();
		
		//For diagonal movements:
		
		//top left
		for (int i = x, j = y; i <= 7 && j >= 0; i++, j--) {
			if (i != x && j != y) result.add(new Position(i,j));
		}
			
				
		//top right 
		for (int i = x, j = y; i <= 7 && j <= 7; i++, j++) {
			if (i != x && j != y) result.add(new Position(i,j));
		}
		
		//bottom left
		for (int i = x, j = y; i >= 0 && j >= 0; i--, j--) {
			if (i != x && j != y) result.add(new Position(i,j));
		}
		
		//bottom right
		for (int i = x, j = y; i >= 0 && j <= 7; i--, j++) {
			if (i != x && j != y) result.add(new Position(i,j));
		}
		
		
		return result;
	}

	@Override
	public String toString() {
		
		return "q";
	}
	public String toString(boolean x) {
		if(getColor()==Color.WHITE) {
			return "q";
		}
		return "Q";
	}

}
