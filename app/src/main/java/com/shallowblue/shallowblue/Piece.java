package com.shallowblue.shallowblue;

import java.util.ArrayList;

/*
 * Abstract Class that defines Piece object.
 * All other pieces will extend this class.
 * 
 */


public abstract class Piece {

	private Position position; //See Position.java
	private Color color; //See Color.java
	private final int drawableId;

	public Piece(Position argPosition, Color color, int drawableId) {
		this.position = argPosition;
		this.color = color;
		this.drawableId = drawableId;
	}

	public int getDrawableId(){
		return drawableId;
	}
	
	/**
	 * Used for determining castling rights
	 * @return whether or not a piece has moved from its starting location.
	 * NOTE: Does not detect when the piece "returns" to its starting location
	 * 			(i.e. would return that it hasn't moved)
	 */
	public abstract boolean hasMoved();
			
	
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
	public abstract ArrayList<Position> possibleMoves();
	
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
	
	
}
