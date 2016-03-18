package com.shallowblue.shallowblue;

/**
 * Created by gauch on 3/17/2016.
 */
public class Move {
    private final Position from;
    private final Position to;
    private final Piece pieceMoved;
    private Piece pieceCaptured;

    public Move(Piece pieceMoved, Position from, Position to) {
        this.pieceMoved = pieceMoved;
        this.from = from;
        this.to = to;
    }

    public Position getFrom() {
        return from;
    }

    public Piece getPieceMoved() {
        return pieceMoved;
    }

    public Piece getPieceCaptured() {
        return pieceCaptured;
    }

    public Position getTo() {
        return to;
    }

    public void setPieceCaptured(Piece pieceCaptured) {
        this.pieceCaptured = pieceCaptured;
    }
}
