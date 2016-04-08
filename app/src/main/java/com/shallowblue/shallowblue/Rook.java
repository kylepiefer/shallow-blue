package com.shallowblue.shallowblue;

import java.util.ArrayList;


public class Rook extends Piece {

	private Position initialPosition;
	
	public Rook(Position argPosition, Color color) {
		super(argPosition, color, color == Color.WHITE ? R.drawable.white_rook : R.drawable.black_rook);
		initialPosition = argPosition;
		
	}

	public Rook(Rook r) {
		super(r.getPosition(), r.getColor(), r.getColor() == Color.WHITE ? R.drawable.white_rook : R.drawable.black_rook);
		this.initialPosition = r.getPosition();
	}

	@Override
	public boolean hasMoved() {
		  return (!(initialPosition.getRow() == getPosition().getRow()
			  		&& initialPosition.getColumn() == getPosition().getColumn()));
	}

	@Override
	public boolean isPromoting() {
		//UNUSED
		return false;
	}

	@Override
	public ArrayList<Position> possibleMoves() {
		
		ArrayList<Position> result = new ArrayList<Position>();
		if (this.getPosition() == null) return result;

		//if the rook's position is illegal, return no moves
		int checkRow = getPosition().getRow();
		int checkCol = getPosition().getColumn();
		if (checkRow > 7 || checkRow < 0) return result;
		if (checkCol > 7 || checkCol < 0) return result;
		
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
