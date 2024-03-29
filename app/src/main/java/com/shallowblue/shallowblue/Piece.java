package com.shallowblue.shallowblue;

import java.util.ArrayList;
import java.util.List;

/*
 * Abstract Class that defines Piece object.
 * All other pieces will extend this class.
 * 
 */


public abstract class Piece {

	private Position position; //See Position.java
	private final Color color; //See Color.java
	private int drawableId;
	private int numMoves = 0;
	public Piece(Position argPosition, Color color, int drawableId) {
		this.position = argPosition;
		this.color = color;
		this.drawableId = drawableId;
	}

	public int getDrawableId(){
		return drawableId;
	}

	public void setDrawableId(int id){
		drawableId = id;
	}
	
	/**
	 * Used for determining castling rights
	 * @return whether or not a piece has moved from its starting location.
	 * NOTE: Does not detect when the piece "returns" to its starting location
	 * 			(i.e. would return that it hasn't moved)
	 */
	public boolean hasMoved() {return (getNumMoves() != 0);}
			
	
	/**
	 * Used for the Pawn to determine whether or not it's reached
	 * the final rank, by comparing its Row position to 8.
	 * @return if the Pawn's current position requires it to promote
	 */
	public abstract boolean isPromoting();
	
	/**
	 * Returns a list of "possible" moves for the corresponding Piece object.
	 * "Possible" = the range of motion a piece has if it's the only Piece on the board.
	 * Accounts for board boundaries and the Pawn's initial jump move.
	 * Does not account for Pawn's en passant and Pawn's diagonal captures.
	 * @return ArrayList of "Position" objects.
	 */
	public abstract List<Move> possibleMoves();
	
	//Getters & Setters
	public Position getPosition(){return position;};
	public Color getColor() {return color;}
	
	/**
	 * Returns a letter representation of the Piece object.
	 * The letter is a lower-case String. Letters are:
	 * King   = "k"
	 * Queen  = "q"
	 * Pawn   = "p"
	 * Knight = "n"
	 * Bishop = "b"
	 * Rook   = "r"
	 */
	public abstract String toString();
	public abstract String toString(boolean x);

	public void setPosition(Position p){
		position = p;
	}
	
	public static Piece copy(Piece p) {
		if (p instanceof Pawn) return new Pawn((Pawn)p);
		if (p instanceof Rook) return new Rook((Rook)p);
		if (p instanceof Knight) return new Knight((Knight)p);
		if (p instanceof Queen) return new Queen((Queen)p);
		if (p instanceof King) return new King((King)p);
		if (p instanceof Bishop) return new Bishop((Bishop)p);
		return null;
	}


	public int getNumMoves() {return numMoves;}
	public void setNumMoves(int i) {
		numMoves = i;

	}
	public void incrementNumMoves(int i) {numMoves += i;}

}
