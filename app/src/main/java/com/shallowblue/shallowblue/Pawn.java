package com.shallowblue.shallowblue;

import java.util.ArrayList;


public class Pawn extends Piece {

	private final Position initialPosition;
	
	public Pawn(Position argPosition, Color color) {
		super(argPosition, color, color == Color.WHITE ? R.drawable.white_pawn : R.drawable.black_pawn);
		initialPosition = argPosition;
	}

	public Pawn(Pawn p) {
		super(p.getPosition(), p.getColor(), p.getColor() == Color.WHITE ? R.drawable.white_pawn : R.drawable.black_pawn);
        this.initialPosition = p.initialPosition;
	}

	/*@Override
	public boolean hasMoved() {
		
		  return (!(initialPosition.getRow() == getPosition().getRow()
		  		&& initialPosition.getColumn() == getPosition().getColumn()));
	}*/

	@Override
	public boolean isPromoting() {
		//Return whether or not the pawn must promote
		return (getPosition().getRow() == 7||getPosition().getRow() == 0);
	}

	@Override
	public ArrayList<Position> possibleMoves() {
		
		ArrayList<Position> result = new ArrayList<Position>();
		if (this.getPosition() == null) return result;

		int row = getPosition().getRow();
		int col = getPosition().getColumn();

		//if the pawn's position is illegal or it can no longer move forward, no moves should be added
		if (row >= 7 || row <= 0 || col > 7 || col < 0) return result;

		//if the pawn is white, a new possible position is generated
		//by adding +1 to its row. If it's black, add -1 instead.


		int direction = (getColor() == Color.WHITE) ? 1 : -1;
		result.add(new Position(getPosition().getRow()+direction, getPosition().getColumn()));

		// check if pawn is at the edge of the board (for diagonal captures)
		if (getPosition().getColumn() > 0)
			result.add(new Position(getPosition().getRow() + direction, getPosition().getColumn() - 1));
		if (getPosition().getColumn() < 7)
			result.add(new Position(getPosition().getRow() + direction, getPosition().getColumn() + 1));
		
		// if a pawn is in its starting row (meaning it hasn't yet moved) it can move forward 2 spaces
		if ((getColor() == Color.WHITE && getPosition().getRow() == 1) ||
				(getColor() == Color.BLACK && getPosition().getRow() == 7)) {
			result.add(new Position(getPosition().getRow()+(direction*2), getPosition().getColumn()));
		}

		return result;
	}

	@Override
	public String toString() {
		
		return "p";
	}
	public String toString(boolean x) {
		if(getColor()==Color.WHITE) {
			return "p";
		}
		return "P";
	}

}
