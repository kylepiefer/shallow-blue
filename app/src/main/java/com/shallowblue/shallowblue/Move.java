package com.shallowblue.shallowblue;

/**
 * Created by gauch on 3/17/2016.
 */
public class Move {
    private final Position from;
    private final Position to;
    private final Piece pieceMoved;
    private Piece pieceCaptured = null;

    public Move(Piece pieceMoved, Position from, Position to) {
        this.pieceMoved = pieceMoved;
        this.from = from;
        this.to = to;
    }

    public Move(Piece pieceMoved, Position from, Piece pieceCaptured, Position to) {
        this.pieceMoved = pieceMoved;
        this.from = from;
        this.to = to;
        this.pieceCaptured = pieceCaptured;
    }



    /*
    @Override
    public String toString(){
        if (pieceCaptured == null) {
            return pieceMoved.toString() + from.toString(true) + "_" + to.toString(true);
        }
        return pieceMoved.toString() + from.toString(true) + pieceCaptured.toString() + to.toString(true);
    }*/
    public String toString(boolean x) {
        if (pieceCaptured == null) {
            return pieceMoved.toString() + from.toString(true) + "_" + to.toString(true);
        }
        return pieceMoved.toString(true) + from.toString(true) + pieceCaptured.toString(true) + to.toString(true);
    }
    public Move(Move in) {
        this.from = new Position(in.getFrom());
        this.to = new Position(in.getTo());
        this.pieceMoved = Piece.copy(in.getPieceMoved());
        this.pieceCaptured = Piece.copy(in.getPieceCaptured());

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

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Move)) return false;
        if (obj == this) return true;
        Move other = (Move)obj;
        if (!this.from.equals(other.from)) return false;
        if (!this.to.equals(other.to)) return false;
        if (this.pieceMoved != other.pieceMoved) return false;
        if (this.pieceCaptured != other.pieceCaptured) return false;
        return true;
    }

    @Override
    public String toString() {
        return this.getFrom().toString() + "->" + this.getTo().toString();
    }
}
