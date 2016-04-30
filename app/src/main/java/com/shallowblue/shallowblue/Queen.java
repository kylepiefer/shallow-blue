package com.shallowblue.shallowblue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Queen extends Piece {

	public Queen(Position argPosition, Color color) {
		super(argPosition, color, color == Color.WHITE ? R.drawable.white_queen : R.drawable.black_queen);
	}

	public Queen(Queen q) {
		super(q.getPosition(), q.getColor(), q.getColor() == Color.WHITE ? R.drawable.white_queen : R.drawable.black_queen);
	}

	/*@Override
	public boolean hasMoved() {
		// Unused
		return false;
	}*/

	@Override
	public boolean isPromoting() {
		// Unused as well
		return false;
	}

	@Override
	public List<Move> possibleMoves() {
		
		List<Move> result = new ArrayList<Move>();
		if (this.getPosition() == null) return Collections.<Move>emptyList();

		//if the queen's position is illegal, no moves should be added:
		int checkRow = getPosition().getRow();
		int checkCol = getPosition().getColumn();
		if (checkRow > 7 || checkRow < 0) return Collections.<Move>emptyList();
		if (checkCol > 7 || checkCol < 0) return Collections.<Move>emptyList();
		
		//left & right movements
		for (int i = 0; i <= 7; i++)
			if (i != getPosition().getColumn())
				result.add(new Move(this, new Position(getPosition().getRow(), i)));
		//up and down
		for (int i = 0; i <= 7; i++)
			if (i != getPosition().getRow())
				result.add(new Move(this, new Position(i, getPosition().getColumn())));
		
		int x = getPosition().getRow();
		int y = getPosition().getColumn();
		
		//For diagonal movements:
		
		//top left
		for (int i = x, j = y; i <= 7 && j >= 0; i++, j--) {
			if (i != x && j != y)
				result.add(new Move(this, new Position(i,j)));
		}
			
				
		//top right 
		for (int i = x, j = y; i <= 7 && j <= 7; i++, j++) {
			if (i != x && j != y)
				result.add(new Move(this, new Position(i,j)));
		}
		
		//bottom left
		for (int i = x, j = y; i >= 0 && j >= 0; i--, j--) {
			if (i != x && j != y)
				result.add(new Move(this, new Position(i,j)));

		}
		
		//bottom right
		for (int i = x, j = y; i >= 0 && j <= 7; i--, j++) {
			if (i != x && j != y)
				result.add(new Move(this, new Position(i,j)));
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
