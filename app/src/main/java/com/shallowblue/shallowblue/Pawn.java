package com.shallowblue.shallowblue;

import java.util.ArrayList;


public class Pawn extends Piece {

	private Position initialPosition;
	
	public Pawn(Position argPosition, Color color) {
		super(argPosition, color, color == Color.WHITE ? R.drawable.white_pawn : R.drawable.black_pawn);
		initialPosition = argPosition;
	}

	@Override
	public boolean hasMoved() {
		
		  return (initialPosition.getRow() == getPosition().getRow()
		  		&& initialPosition.getColumn() == getPosition().getColumn());
	}

	@Override
	public boolean isPromoting() {
		//Return whether or not the pawn must promote
		return (getPosition().getRow() == 7);
	}

	@Override
	public ArrayList<Position> possibleMoves() {
		
		ArrayList<Position> result = new ArrayList<Position>();
		
		//if the pawn is white, a new possible position is generated
		//by adding +1 to its row. If it's black, add -1 instead.
		int direction = (initialPosition.getRow() == 1) ? 1 : -1;


		//Change made on 04/06/2016 by Mohammad: No "forward" move should
		//be added to the Pawn's moves list if it's in the final rank.
		boolean canAdvance = true;
		if (getColor() == Color.BLACK && getPosition().getRow() == 0) canAdvance = false;
		if (getColor() == Color.WHITE && getPosition().getRow() == 7) canAdvance = false;
		if (canAdvance)
		result.add(new Position(getPosition().getRow()+direction, getPosition().getColumn()));
		if (getPosition().getColumn() > 0)
			result.add(new Position(getPosition().getRow() + direction, getPosition().getColumn() - 1));
		if (getPosition().getColumn() < 7)
			result.add(new Position(getPosition().getRow() + direction, getPosition().getColumn() + 1));
		
		//canJump refers to the 2 tiles a pawn can jump, which is a special
		//move that can be made only if the pawn hasn't moved yet.
		//1 and 6 comparisons denote the original starting positions for
		//white and black pawns, respectively.//TODO replace 1 and 6 with named constants
		boolean canJump = (  getPosition().getRow() == initialPosition.getRow())? true: false;
		
		if (canJump)
			result.add(new Position(getPosition().getRow()+(direction*2), getPosition().getColumn()));
		
		return result;
	}

	@Override
	public String toString() {
		
		return "p";
	}


}
