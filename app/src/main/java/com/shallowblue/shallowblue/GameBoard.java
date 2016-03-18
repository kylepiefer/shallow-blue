package com.shallowblue.shallowblue;

/**
 * Created by peter on 3/14/2016.
 */
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.lang.Math;

public class GameBoard {
    private Map<Position, Piece> gameBoard;
    private List<Move> gameHistory;

    public GameBoard() {
        gameBoard = new HashMap<Position,Piece>();
        Position p;
        gameBoard.put((p=new Position(1,1)), new Rook(p, Color.WHITE));
        gameBoard.put((p=new Position(2,1)), new Knight(p, Color.WHITE));
        gameBoard.put((p=new Position(3,1)), new Bishop(p, Color.WHITE));
        gameBoard.put((p=new Position(4,1)), new Queen(p, Color.WHITE));
        gameBoard.put((p=new Position(5,1)), new King(p, Color.WHITE));
        gameBoard.put((p=new Position(6,1)), new Bishop(p, Color.WHITE));
        gameBoard.put((p=new Position(7,1)), new Knight(p, Color.WHITE));
        gameBoard.put((p=new Position(8,1)), new Rook(p, Color.WHITE));
        gameBoard.put((p=new Position(1,2)), new Pawn(p, Color.WHITE));
        gameBoard.put((p=new Position(2,2)), new Pawn(p, Color.WHITE));
        gameBoard.put((p=new Position(3,2)), new Pawn(p, Color.WHITE));
        gameBoard.put((p=new Position(4,2)), new Pawn(p, Color.WHITE));
        gameBoard.put((p=new Position(5,2)), new Pawn(p, Color.WHITE));
        gameBoard.put((p=new Position(6,2)), new Pawn(p, Color.WHITE));
        gameBoard.put((p=new Position(7,2)), new Pawn(p, Color.WHITE));
        gameBoard.put((p=new Position(8,2)), new Pawn(p, Color.WHITE));

        gameBoard.put((p=new Position(1,7)), new Pawn(p, Color.BLACK));
        gameBoard.put((p=new Position(2,7)), new Pawn(p, Color.BLACK));
        gameBoard.put((p=new Position(3,7)), new Pawn(p, Color.BLACK));
        gameBoard.put((p=new Position(4,7)), new Pawn(p, Color.BLACK));
        gameBoard.put((p=new Position(5,7)), new Pawn(p, Color.BLACK));
        gameBoard.put((p=new Position(6,7)), new Pawn(p, Color.BLACK));
        gameBoard.put((p=new Position(7,7)), new Pawn(p, Color.BLACK));
        gameBoard.put((p=new Position(8,7)), new Pawn(p, Color.BLACK));
        gameBoard.put((p=new Position(1,8)), new Rook(p, Color.BLACK));
        gameBoard.put((p=new Position(2,8)), new Knight(p, Color.BLACK));
        gameBoard.put((p=new Position(3,8)), new Bishop(p, Color.BLACK));
        gameBoard.put((p=new Position(4,8)), new Queen(p, Color.BLACK));
        gameBoard.put((p=new Position(5,8)), new King(p, Color.BLACK));
        gameBoard.put((p=new Position(6,8)), new Bishop(p, Color.BLACK));
        gameBoard.put((p=new Position(7,8)), new Knight(p, Color.BLACK));
        gameBoard.put((p=new Position(8,8)), new Rook(p, Color.BLACK));
        gameHistory = new ArrayList<Move>();
    }
    public GameBoard(GameBoard in) {
        gameBoard = new HashMap<Position,Piece>(in.getGameBoard());
        gameHistory = new ArrayList<Move>(in.getGameHistory());
    }

    public List<Move> getAllMoves(){
        List<Move> ret = new ArrayList<Move>();
        for(Position p : gameBoard.keySet())
            ret.addAll(getLegalMoves(p));
        return ret;
    }

    public boolean move(Move m){						//Returns true iff successful

        if(gameBoard.get(m.getFrom()) == null)
            return false;

        m.setPieceCaptured(gameBoard.get(m.getTo()));
        gameBoard.put(m.getTo(), gameBoard.get(m.getFrom()));
        gameBoard.remove(m.getTo());
        gameHistory.add(m);
        return true;

    }
    public boolean put(Piece p){
        if(gameBoard.get(p.getPosition()) == null){
            gameBoard.put(p.getPosition(),p);
            return true;
        }
        return false;
    }
    public String toString(){
        return TextUtils.join("\n", gameHistory);
    }
    public void undo(){ //example move is ka4_b5
        Move m = gameHistory.get(gameHistory.size()-1);
        if(m == null)
            return;
        gameBoard.put(m.getFrom(), m.getPieceMoved());
        gameBoard.put(m.getTo(), m.getPieceCaptured());

        gameHistory.remove(gameHistory.size()-1);
        return;
    }

    public boolean legalMove(Move m) {
        /* TODO I couldn't understand this code
        canmove = true;
            while (m.getFrom() != from){
                if(from[0]-tempint[0] != 0) {
                    tempint[0] = (int) (tempint[0] + Math.signum(from[0] - tempint[0]));
                }
                if(from[1]-tempint[1] != 0) {
                    tempint[1] = (int) (tempint[1] + Math.signum(from[1] - tempint[1]));
                }
                if ( gameBoard[tempint[0]][tempint[1]] != null && movelist.contains(tempint)){
                    canmove = false;
                    if(from[0]-tempint[0] != 0) {
                        tempint[0] = (int) (tempint[0] + Math.signum(from[0] - tempint[0]));
                    }
                    if(from[1]-tempint[1] != 0) {
                        tempint[1] = (int) (tempint[1] + Math.signum(from[1] - tempint[1]));
                    }
                }
            }
         */
        return true;
    }

    public List<Move> getLegalMoves(Position from){
        List<Move> moveList = new ArrayList<Move>();
        for(Position p : gameBoard.get(from).possibleMoves())
            moveList.add(new Move(gameBoard.get(from), from, p));
        List<Move> ret = new ArrayList<Move>();

        for(Move m : moveList)
            if(legalMove(m))
                ret.add(m);
        return ret;
    }

    public String pack(){
        return null;
    }

    public void unpack(String packedString){
        return;
    }

    public Map<Position, Piece> getGameBoard(){
        return gameBoard;
    }

    public List<Move> getGameHistory() { return gameHistory; }
}


