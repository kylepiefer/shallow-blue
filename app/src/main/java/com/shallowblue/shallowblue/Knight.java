package com.shallowblue.shallowblue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Knight extends Piece {

	public Knight(Position argPosition, Color color) {
		super(argPosition, color, color == Color.WHITE ? R.drawable.white_knight : R.drawable.black_knight);
	}

	public Knight(Knight k) {
		super(k.getPosition(), k.getColor(), k.getColor() == Color.WHITE ? R.drawable.white_knight : R.drawable.black_knight);
	}

	/*@Override
	public boolean hasMoved() {
		//UNUSED
		return false;
	}*/

	@Override
	public boolean isPromoting() {
		// UNUSED
		return false;
	}

	@Override
	public List<Move> possibleMoves() {
		
		List<Move> result = new ArrayList<Move>();
		if (this.getPosition() == null) return Collections.<Move>emptyList();

		int i = getPosition().getRow();
		int j = getPosition().getColumn();
		
		//Taking a value from iPositions and a value from
		//jPositions *BOTH FROM THE SAME INDEX* gives one of the
		//8 tiles describing the Knight's range of motion. See this picture:
		//hotoffthechess.com/wp-content/uploads/2015/12/chess-how-the-knight-moves.jpg
		int[] iPositions = {i-2, i-2, i-1, i-1, i+1, i+1, i+2, i+2};
		int[] jPositions = {j-1, j+1, j-2, j+2, j-2, j+2, j-1, j+1};
		
		for (int k = 0; k < iPositions.length; k++) {
			int row = iPositions[k], col = jPositions[k];
			if (row >= 0 && row <= 7) //if row is not out of bounds
				if (col >= 0 && col <= 7) //and column is not out of bounds
					if (row != i && col != j) //and we're not looking at our current position
						result.add(new Move(this, new Position(row,col)));
		}
		
		return result;
	}

	@Override
	public String toString() {
		
		return "n";
	}
	public String toString(boolean x) {
		if(getColor()==Color.WHITE) {
			return "n";
		}
		return "N";
	}
}
