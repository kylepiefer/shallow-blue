package com.shallowblue.shallowblue;

import java.util.ArrayList;


public class Rook extends Piece {

	private Position initialPosition;
	
	public Rook(Position argPosition, Color color) {
		super(argPosition, color, color == Color.WHITE ? R.drawable.white_rook : R.drawable.black_rook);
		initialPosition = argPosition;
		
	}

	@Override
	public boolean hasMoved() {
		  return (initialPosition.getRow() == getPosition().getRow()
			  		&& initialPosition.getColumn() == getPosition().getColumn());
	}

	@Override
	public boolean isPromoting() {
		//UNUSED
		return false;
	}

	@Override
	public ArrayList<Position> possibleMoves() {
		
		ArrayList<Position> result = new ArrayList<Position>();
		
		//Left & right movements
		for (int i = 0; i <= 7; i++) if (i != getPosition().getColumn()) result.add(new Position(getPosition().getRow(), i));
		//Up & down
		for (int i = 0; i <= 7; i++) if (i != getPosition().getRow()) result.add(new Position(i, getPosition().getColumn()));
		
		
		return result;
	}

	@Override
	public String toString() {
		
		return "r";
	}

}
