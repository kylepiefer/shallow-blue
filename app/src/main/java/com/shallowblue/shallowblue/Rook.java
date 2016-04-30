package com.shallowblue.shallowblue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Rook extends Piece {


	private Position initialPosition;
	//private Move firstMove = null;

	public boolean leftright(){   //true if on left side of board at beginning
		return initialPosition.getColumn()==0;
	}

	//private final Position initialPosition;
	

	public Rook(Position argPosition, Color color) {
		super(argPosition, color, color == Color.WHITE ? R.drawable.white_rook : R.drawable.black_rook);
		initialPosition = argPosition;
		
	}

	public Rook(Rook r) {
		super(r.getPosition(), r.getColor(), r.getColor() == Color.WHITE ? R.drawable.white_rook : R.drawable.black_rook);
        this.initialPosition = r.initialPosition;
        //this.firstMove = r.firstMove;
	}

    /*@Override
    public boolean hasMoved() {
        return !(firstMove == null);
    }*/

    //public Move getFirstMove() { return this.firstMove; }
    //public void setFirstMove(Move m) { this.firstMove = null; }

    @Override
	public boolean isPromoting() {
		//UNUSED
		return false;
	}

	@Override
	public List<Move> possibleMoves() {
		
		List<Move> result = new ArrayList<Move>();
		if (this.getPosition() == null) return Collections.<Move>emptyList();

		//if the rook's position is illegal, return no moves
		int checkRow = getPosition().getRow();
		int checkCol = getPosition().getColumn();
		if (checkRow > 7 || checkRow < 0) return Collections.<Move>emptyList();;
		if (checkCol > 7 || checkCol < 0) return Collections.<Move>emptyList();;
		
		//Left & right movements
		for (int i = 0; i <= 7; i++)
			if (i != getPosition().getColumn())
				result.add(new Move(this, new Position(getPosition().getRow(), i)));
		//Up & down
		for (int i = 0; i <= 7; i++)
			if (i != getPosition().getRow())
				result.add(new Move(this, new Position(i, getPosition().getColumn())));

		return result;
	}

	@Override
	public String toString() {
		
		return "r";
	}

	public String toString(boolean x) {
		if(getColor()==Color.WHITE) {
			return "r";
		}
		return "R";
	}

}
