package com.shallowblue.shallowblue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


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

	@Override
	public boolean isPromoting() {
		//Return whether or not the pawn must promote
		return (getPosition().getRow() == 7||getPosition().getRow() == 0);
	}

	@Override
	public List<Move> possibleMoves() {
		
		if (this.getPosition() == null)
            return Collections.<Move>emptyList();

		int row = getPosition().getRow();
		int col = getPosition().getColumn();

		//if the pawn's position is illegal or it can no longer move forward, no moves should be added
		if (row >= 7 || row <= 0 || col > 7 || col < 0) return Collections.<Move>emptyList();

        List<Position> positions = new ArrayList<Position>();
        //if the pawn is white, a new possible position is generated
		//by adding +1 to its row. If it's black, add -1 instead.
		int direction = (getColor() == Color.WHITE) ? 1 : -1;
		positions.add(new Position(getPosition().getRow()+direction, getPosition().getColumn()));

		// check if pawn is at the edge of the board (for diagonal captures)
		if (getPosition().getColumn() > 0)
			positions.add(new Position(getPosition().getRow() + direction, getPosition().getColumn() - 1));
		if (getPosition().getColumn() < 7)
			positions.add(new Position(getPosition().getRow() + direction, getPosition().getColumn() + 1));
		
		// if a pawn is in its starting row (meaning it hasn't yet moved) it can move forward 2 spaces
		if ((getColor() == Color.WHITE && getPosition().getRow() == 1) ||
				(getColor() == Color.BLACK && getPosition().getRow() == 6)) {
			positions.add(new Position(getPosition().getRow()+(direction*2), getPosition().getColumn()));
		}

		List<Move> result = new ArrayList<Move>();
		for(Position p : positions) {
			if(p.getRow() == 0 || p.getRow() == 7) {
				Move toBishop = new Move(this, getPosition(), p);
				toBishop.setPiecePromoted(new Bishop(p, getColor()));
				result.add(toBishop);

				Move toKnight = new Move(this, getPosition(), p);
				toKnight.setPiecePromoted(new Knight(p, getColor()));
				result.add(toKnight);

				Move toRook = new Move(this, getPosition(), p);
				toRook.setPiecePromoted(new Rook(p, getColor()));
				result.add(toRook);

				Move toQueen = new Move(this, getPosition(), p);
				toQueen.setPiecePromoted(new Queen(p, getColor()));
				result.add(toQueen);
			} else {
				result.add(new Move(this, p));
			}
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
