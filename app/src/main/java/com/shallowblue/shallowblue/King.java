package com.shallowblue.shallowblue;

import java.util.ArrayList;


public class King extends Piece {

	private Position initialPosition;
	
	public King(Position argPosition, Color color) {
		super(argPosition, color, color == Color.WHITE ? R.drawable.white_king : R.drawable.black_king);
		initialPosition = argPosition;
		
	}

	@Override
	public boolean hasMoved() {
		
		  return (initialPosition.getRow() == getPosition().getRow()
			  		&& initialPosition.getColumn() == getPosition().getColumn());
	}

	@Override
	public boolean isPromoting() {
		//Unused
		return false;
	}

	@Override
	public ArrayList<Position> possibleMoves() {
		ArrayList<Position> result = new ArrayList<Position>();
		int pieceRow = getPosition().getRow();
		int pieceCol = getPosition().getColumn();
		
		for (int i = pieceRow-1; i <= pieceRow+1; i++)
			for (int j = pieceCol-1; j <= pieceCol+1; j++) 
				if (i >= 1 && i <= 8) //row isn't out of bounds
					if (j >= 1 && j <= 8) //column isn't out of bounds
						if (i != pieceRow && j != pieceCol) //we're not adding the current position
						result.add(new Position(i,j));
		
		return result;
	}

	@Override
	public String toString() {
		
		return "k";
	}

}
