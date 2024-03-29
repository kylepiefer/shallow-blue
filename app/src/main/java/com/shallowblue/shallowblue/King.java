package com.shallowblue.shallowblue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class King extends Piece {

	private final Position initialPosition;
	//private Move firstMove = null;
	
	public King(Position argPosition, Color color) {
		super(argPosition, color, color == Color.WHITE ? R.drawable.white_king : R.drawable.black_king);
		initialPosition = argPosition;
	}

	public King(King k) {
		super(k.getPosition(), k.getColor(), k.getColor() == Color.WHITE ? R.drawable.white_king : R.drawable.black_king);
		this.initialPosition = k.initialPosition;
        //this.firstMove = k.firstMove;
	}

	/*@Override
	public boolean hasMoved() {
		return !(firstMove == null);
	}*/

    //public Move getFirstMove() { return this.firstMove; }
    //public void setFirstMove(Move m) { this.firstMove = null; }

    @Override
	public boolean isPromoting() {
		//Unused
		return false;
	}

	@Override
	public List<Move> possibleMoves() {
		List<Move> result = new ArrayList<Move>();
		if (this.getPosition() == null) return Collections.<Move>emptyList();

		int pieceRow = getPosition().getRow();
		int pieceCol = getPosition().getColumn();
		
		for (int i = pieceRow-1; i <= pieceRow+1; i++)
			for (int j = pieceCol-1; j <= pieceCol+1; j++) 
				if (i >= 0 && i <= 7) //row isn't out of bounds
					if (j >= 0 && j <= 7) //column isn't out of bounds
						if (!(i == pieceRow && j == pieceCol)) //we're not adding the current position
							result.add(new Move(this, new Position(i,j)));

		// if a King is in its initial position, it can potentially castle
		if (getPosition().equals(initialPosition)) {
			result.add(new Move(this, new Position(getPosition().getRow(), getPosition().getColumn() + 2)));
            result.add(new Move(this, new Position(getPosition().getRow(), getPosition().getColumn() - 2)));
        }

		return result;
	}

	@Override
	public String toString() {
		
		return "k";
	}
	public String toString(boolean x) {
		if(getColor()==Color.WHITE) {
			return "k";
		}
		return "K";
	}

	public Position getInitialPosition(){
		return initialPosition;
	}
}
