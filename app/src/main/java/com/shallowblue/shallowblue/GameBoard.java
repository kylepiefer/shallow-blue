package com.shallowblue.shallowblue;

/**
 * Created by peter on 3/14/2016.
 */
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class GameBoard {

    public static GameBoard activeGameBoard;
    public Map<Position, Piece> gameBoard;
    public List<Move> gameHistory;
    private Color playerToMove;
    private Stack<Move> redoStack;

    public GameBoard() {
        if (gameBoard == null) {
            gameBoard = new HashMap<Position, Piece>();
            Position p;
            gameBoard.put((p = new Position(0, 0)), new Rook(p, Color.WHITE));
            gameBoard.put((p = new Position(0, 1)), new Knight(p, Color.WHITE));
            gameBoard.put((p = new Position(0, 2)), new Bishop(p, Color.WHITE));
            gameBoard.put((p = new Position(0, 3)), new Queen(p, Color.WHITE));
            gameBoard.put((p = new Position(0, 4)), new King(p, Color.WHITE));
            gameBoard.put((p = new Position(0, 5)), new Bishop(p, Color.WHITE));
            gameBoard.put((p = new Position(0, 6)), new Knight(p, Color.WHITE));
            gameBoard.put((p = new Position(0, 7)), new Rook(p, Color.WHITE));
            gameBoard.put((p = new Position(1, 0)), new Pawn(p, Color.WHITE));
            gameBoard.put((p = new Position(1, 1)), new Pawn(p, Color.WHITE));
            gameBoard.put((p = new Position(1, 2)), new Pawn(p, Color.WHITE));
            gameBoard.put((p = new Position(1, 3)), new Pawn(p, Color.WHITE));
            gameBoard.put((p = new Position(1, 4)), new Pawn(p, Color.WHITE));
            gameBoard.put((p = new Position(1, 5)), new Pawn(p, Color.WHITE));
            gameBoard.put((p = new Position(1, 6)), new Pawn(p, Color.WHITE));
            gameBoard.put((p = new Position(1, 7)), new Pawn(p, Color.WHITE));

            gameBoard.put((p = new Position(6, 0)), new Pawn(p, Color.BLACK));
            gameBoard.put((p = new Position(6, 1)), new Pawn(p, Color.BLACK));
            gameBoard.put((p = new Position(6, 2)), new Pawn(p, Color.BLACK));
            gameBoard.put((p = new Position(6, 3)), new Pawn(p, Color.BLACK));
            gameBoard.put((p = new Position(6, 4)), new Pawn(p, Color.BLACK));
            gameBoard.put((p = new Position(6, 5)), new Pawn(p, Color.BLACK));
            gameBoard.put((p = new Position(6, 6)), new Pawn(p, Color.BLACK));
            gameBoard.put((p = new Position(6, 7)), new Pawn(p, Color.BLACK));
            gameBoard.put((p = new Position(7, 0)), new Rook(p, Color.BLACK));
            gameBoard.put((p = new Position(7, 1)), new Knight(p, Color.BLACK));
            gameBoard.put((p = new Position(7, 2)), new Bishop(p, Color.BLACK));
            gameBoard.put((p = new Position(7, 3)), new Queen(p, Color.BLACK));
            gameBoard.put((p = new Position(7, 4)), new King(p, Color.BLACK));
            gameBoard.put((p = new Position(7, 5)), new Bishop(p, Color.BLACK));
            gameBoard.put((p = new Position(7, 6)), new Knight(p, Color.BLACK));
            gameBoard.put((p = new Position(7, 7)), new Rook(p, Color.BLACK));
        }
        playerToMove = Color.WHITE;
        gameHistory = new ArrayList<Move>();
        redoStack = new Stack<Move>();
    }
    public GameBoard(GameBoard in) {
        gameBoard = new HashMap<Position,Piece>();
        for(Map.Entry<Position,Piece> e : in.gameBoard.entrySet())
            gameBoard.put(e.getKey(),Piece.copy(e.getValue()));
        gameHistory = new ArrayList<Move>(in.getGameHistory());
        playerToMove = in.playerToMove();
        redoStack = new Stack<Move>();
    }

    private void switchPlayerToMove() {
        this.playerToMove = (playerToMove == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    public List<Move> getAllMoves(){
        List<Move> ret = new ArrayList<Move>();
        for(Map.Entry<Position,Piece> e : gameBoard.entrySet())
            if(e.getValue() != null && e.getValue().getColor() == playerToMove)
                ret.addAll(getLegalMoves(e.getKey()));
        return ret;
    }

    public boolean move(Move m){						//Returns true iff successful
        redoStack.clear();
        if(gameBoard.get(m.getFrom()) == null)
            return false;

        if (gameBoard.get(m.getTo()) != null) {
            m.setPieceCaptured(gameBoard.get(m.getTo()));
            this.gameBoard.remove(m.getTo()).setPosition(null);
        }

        gameBoard.put(m.getTo(), gameBoard.get(m.getFrom()));
        gameBoard.remove(m.getFrom());
        m.getPieceMoved().setPosition(m.getTo());

        gameHistory.add(m);
        switchPlayerToMove();
        return true;
    }

    public void addMove(Move m){
        gameHistory.add(m);
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
    public boolean undo(){ //example move is ka4_b5
        if (gameHistory.isEmpty()) return false;

        Move m = gameHistory.get(gameHistory.size()-1);
        gameBoard.put(m.getFrom(), m.getPieceMoved());
        gameBoard.put(m.getTo(), m.getPieceCaptured());

        gameHistory.remove(gameHistory.size() - 1);
        redoStack.push(m);
        switchPlayerToMove();
        return true;
    }
    public boolean redo(){
        if (redoStack.isEmpty()){
            return false;
        }
        Move m = redoStack.pop();
        this.move(m);
        return true;
    }

    public boolean legalMove(Move m) {
        if(gameBoard.get(m.getTo()) != null && gameBoard.get(m.getFrom()) != null &&
                gameBoard.get(m.getTo()).getColor() == gameBoard.get(m.getFrom()).getColor()){
            return false;
        }
        if(m.getPieceMoved().toString().equals("p")){
            if(m.getTo().getColumn() == m.getFrom().getColumn() && m.getPieceMoved().
                    possibleMoves().contains(m.getTo()) && gameBoard.get(m.getTo()) == null){ //nothing is blocking the pawn
                return true;
            }
            return ((m.getTo().getColumn() == m.getFrom().getColumn()+1 || m.getTo().getColumn() == m.getFrom().getColumn() -1 &&             //is in adjacent column
                    m.getTo().getRow() == m.getFrom().getRow()+1 || m.getTo().getRow() == m.getFrom().getRow() -1) &&                         //is in adjacent row
                    (m.getPieceMoved().possibleMoves().contains(new Position(m.getTo().getRow(),m.getTo().getColumn()+1)) ||                 //is in the same direction
                    m.getPieceMoved().possibleMoves().contains(new Position(m.getTo().getRow(),m.getTo().getColumn()+-1))) &&
                    gameBoard.get(m.getTo()) != null);
        }
        boolean canmove = true;
        Position tempPos = m.getTo();
        /*if (m.getPieceMoved() instanceof Rook &&
                gameBoard.get(m.getTo()) instanceof King &&
                !m.getPieceMoved().hasMoved()&&!gameBoard.get(m.getTo()).hasMoved()) { //castle

            tempPos = m.getTo();
            while (m.getFrom().getColumn() != tempPos.getColumn()){ //checks if anything is between the castling pieces
                int tempCol = tempPos.getColumn();

                if(m.getFrom().getColumn() > tempPos.getColumn()){
                    tempCol++;
                }
                else if(m.getFrom().getColumn() < tempPos.getColumn()){
                    tempCol--;
                }

                tempPos = new Position(m.getFrom().getRow(), tempCol);
                if(m.getPieceMoved().possibleMoves().contains(tempPos)&&gameBoard.containsKey(tempPos)){
                    canmove = false;
                }

            }
            //return canmove;
        }*/
        tempPos = m.getTo();
        while (!m.getFrom().equals(tempPos)){ //naive evaluation of non-pawn pieces
            int tempCol = tempPos.getColumn();
            int tempRow = tempPos.getRow();
            if(m.getFrom().getColumn() > tempPos.getColumn()){
                tempCol++;
            }
            else if(m.getFrom().getColumn() < tempPos.getColumn()){
                tempCol--;
            }

            if(m.getFrom().getRow() > tempPos.getRow()){
                tempRow++;
            }
            else if(m.getFrom().getRow() < tempPos.getRow()){
                tempRow--;
            }
            tempPos = new Position(tempRow, tempCol);
            if(m.getPieceMoved().possibleMoves().contains(tempPos) && gameBoard.get(tempPos) != null){
                canmove = false;
            }

        }
        if(false){ //skewer
            move(m);
            if(false){ //king is threatned
               return false;
            }
            this.undo();
            redoStack.pop();
        }

        return canmove;
    }



    public List<Move> getLegalMoves(Position from){
        List<Move> moveList = new ArrayList<Move>();
        for(Position p : gameBoard.get(from).possibleMoves())
            moveList.add(new Move(gameBoard.get(from), from, p));
        List<Move> ret = new ArrayList<Move>();

        Piece piece = gameBoard.get(from);

        List<Position> possibleMoves = piece.possibleMoves();
        List<Position> legalMoves = new ArrayList<Position>();
        for (int p = possibleMoves.size() - 1; p >= 0; p--) {
            Position curr = possibleMoves.get(p);
            Move possible = new Move(piece, piece.getPosition(), curr);
            if (legalMove(possible)) {
                possibleMoves.remove(p);
                ret.add(possible);
            }
        }
        return ret;
    }

    public String pack(){
        String temp = "";
        try {
            Position p;
            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < 7; j++) {
                    p = new Position(i, j);
                    if (gameBoard.containsKey(p)) {
                        temp += gameBoard.get(p).toString();
                    } else {
                        temp += "_";
                    }
                }
            }

            temp += "+" + gameHistory.get(0).toString() + gameHistory.get(1).toString() + gameHistory.get(2).toString() + "+\n";
            return temp;
        }
        catch(Exception e)
        {
            //null exception when on gameHistory available
        }
        return temp;
    }

    public void unpack(String packedString){

    }

    public Map<Position, Piece> getGameBoard(){
        return gameBoard;
    }

    public List<Move> getGameHistory() { return gameHistory; }

    public Color playerToMove() {
        return playerToMove;
    }

    public double sbe() {
        return Math.random();
    }
    public List<Move> isThreatened(Piece p) {
        List<Move> ret = new ArrayList<Move>();
        for (Map.Entry<Position, Piece> e : gameBoard.entrySet()){
            if (!(e.getValue().getColor() == playerToMove))
                ret.addAll(getLegalMoves(e.getKey()));
        }
        for (Move e : ret){
            if(e.getTo()!=p.getPosition()){
                ret.remove(e);
            }
        }
        return ret;
    }

    //TODO Detects when a player has won.
    public boolean gameOver() {
        return false;
    }
}


