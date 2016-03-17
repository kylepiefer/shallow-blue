package com.shallowblue.shallowblue;

/**
 * Created by peter on 3/14/2016.
 */
import java.util.ArrayList;
import java.util.List;
import java.lang.Math;

public class GameBoard {
    private Piece gameBoard[][];
    private List<String> gameHistory;
    public GameBoard() {
        gameBoard = new Piece[8][8];
        gameBoard[0][0] = new Rook(new Position(0,0), Color.WHITE);
        gameBoard[1][0] = new Knight(new Position(1,0), Color.WHITE);
        gameBoard[2][0] = new Bishop(new Position(2,0), Color.WHITE);
        gameBoard[3][0] = new Queen(new Position(3,0), Color.WHITE);
        gameBoard[4][0] = new King(new Position(4,0), Color.WHITE);
        gameBoard[5][0] = new Bishop(new Position(5,0), Color.WHITE);
        gameBoard[6][0] = new Knight(new Position(6,0), Color.WHITE);
        gameBoard[7][0] = new Rook(new Position(7,0), Color.WHITE);
        gameBoard[0][1] = new Pawn(new Position(0,1), Color.WHITE);
        gameBoard[1][1] = new Pawn(new Position(1,1), Color.WHITE);
        gameBoard[2][1] = new Pawn(new Position(2,1), Color.WHITE);
        gameBoard[3][1] = new Pawn(new Position(3,1), Color.WHITE);
        gameBoard[4][1] = new Pawn(new Position(4,1), Color.WHITE);
        gameBoard[5][1] = new Pawn(new Position(5,1), Color.WHITE);
        gameBoard[6][1] = new Pawn(new Position(6,1), Color.WHITE);
        gameBoard[7][1] = new Pawn(new Position(7,1), Color.WHITE);

        gameBoard[0][6] = new Pawn(new Position(0,6), Color.BLACK);
        gameBoard[1][6] = new Pawn(new Position(1,6), Color.BLACK);
        gameBoard[2][6] = new Pawn(new Position(2,6), Color.BLACK);
        gameBoard[3][6] = new Pawn(new Position(3,6), Color.BLACK);
        gameBoard[4][6] = new Pawn(new Position(4,6), Color.BLACK);
        gameBoard[5][6] = new Pawn(new Position(5,6), Color.BLACK);
        gameBoard[6][6] = new Pawn(new Position(6,6), Color.BLACK);
        gameBoard[7][6] = new Pawn(new Position(7,6), Color.BLACK);
        gameBoard[0][7] = new Rook(new Position(0,7), Color.BLACK);
        gameBoard[1][7] = new Knight(new Position(1,7), Color.BLACK);
        gameBoard[2][7] = new Bishop(new Position(2,7), Color.BLACK);
        gameBoard[3][7] = new Queen(new Position(3,7), Color.BLACK);
        gameBoard[4][7] = new King(new Position(4,7), Color.BLACK);
        gameBoard[5][7] = new Bishop(new Position(5,7), Color.BLACK);
        gameBoard[6][7] = new Knight(new Position(6,7), Color.BLACK);
        gameBoard[7][7] = new Rook(new Position(7,7), Color.BLACK);

        gameHistory = new ArrayList<String>();
    }
    public GameBoard(GameBoard in) {
        gameBoard = new Piece[8][9];
        for(int i= 0; i<8; i++){
            for(int j= 0; i<9; i++){
                gameBoard[i][j] = in.getGameBoard()[i][j];
            }
        }


        gameHistory = new ArrayList<String>();


    }
    public List<int[][]> getAllMoves(){
        List<int[][]> templist = new ArrayList<int[][]>();
        int[] tempcoord = {0,0};
        for(int i= 0; i<8; i++){
            tempcoord[0] = i;
            for(int j= 0; i<9; i++){
                tempcoord[1] = j;
                if (gameBoard[i][j] != null){
                    templist.addAll(this.getLegalMoves(tempcoord));
                }
            }
        }
        return templist;
    }

    public boolean Move(int from[], int to[]){						//Returns true iff successful
        if(gameBoard[from[0]][from[1]] != null){					//example move is ka4_b5
            String tempstring = "";									//Adds to gameHistory
            tempstring += gameBoard[from[0]][from[1]].toString();
            switch (from[0]) {
                case 0: tempstring += "a" + from[1];
                    break;
                case 1: tempstring += "b" + from[1];
                    break;
                case 2: tempstring += "c" + from[1];
                    break;
                case 3: tempstring += "d" + from[1];
                    break;
                case 4: tempstring += "e" + from[1];
                    break;
                case 5: tempstring += "f" + from[1];
                    break;
                case 6: tempstring += "g" + from[1];
                    break;
                case 7: tempstring += "h" + from[1];
                    break;
            }
            if(gameBoard[to[0]][to[1]] != null){
                tempstring += gameBoard[to[0]][to[1]].toString();
            }
            else{
                tempstring += "_";
            }
            switch (to[0]) {
                case 0: tempstring += "a" + to[1];
                    break;
                case 1: tempstring += "b" + to[1];
                    break;
                case 2: tempstring += "c" + to[1];
                    break;
                case 3: tempstring += "d" + to[1];
                    break;
                case 4: tempstring += "e" + to[1];
                    break;
                case 5: tempstring += "f" + to[1];
                    break;
                case 6: tempstring += "g" + to[1];
                    break;
                case 7: tempstring += "h" + to[1];
                    break;
            }
            gameBoard[to[0]][to[1]] = gameBoard[from[0]][from[1]]; 	//Actually perform the move
            gameBoard[from[0]][from[1]] = null;
            gameHistory.add(tempstring);
            return true;
        }
        return false;
    }
    public boolean put(Piece piece, int[] to){
        if(gameBoard[to[0]][to[1]] != null){
            gameBoard[to[0]][to[1]] = piece;
            return true;
        }
        return false;
    }
    public String toString(){
        String tempstring = gameHistory.get(0);
        for(int i= 1; i<gameHistory.size(); i++){
            tempstring += "/n" + gameHistory.get(i);
        }

        return tempstring;
    }
    public void undo(){ //example move is ka4_b5
        int to[] = new int[2];
        int from[] = new int[2];
        String temp = gameHistory.get(gameHistory.size()-1);


        switch (temp.charAt(1)) {
            case 'a': from[0] = 0;
            case 'b': from[0] = 1;
            case 'c': from[0] = 2;
            case 'd': from[0] = 3;
            case 'e': from[0] = 4;
            case 'f': from[0] = 5;
            case 'g': from[0] = 6;
            case 'h': from[0] = 7;
        }
        switch (temp.charAt(2)) {
            case '1': from[1] = 1;
            case '2': from[1] = 2;
            case '3': from[1] = 3;
            case '4': from[1] = 4;
            case '5': from[1] = 5;
            case '6': from[1] = 6;
            case '7': from[1] = 7;
            case '8': from[1] = 8;
        }
        switch (temp.charAt(4)) {
            case 'a': to[0] = 0;
            case 'b': to[0] = 1;
            case 'c': to[0] = 2;
            case 'd': to[0] = 3;
            case 'e': to[0] = 4;
            case 'f': to[0] = 5;
            case 'g': to[0] = 6;
            case 'h': to[0] = 7;
        }
        switch (temp.charAt(5)) {
            case '1': to[1] = 1;
            case '2': to[1] = 2;
            case '3': to[1] = 3;
            case '4': to[1] = 4;
            case '5': to[1] = 5;
            case '6': to[1] = 6;
            case '7': to[1] = 7;
            case '8': to[1] = 8;
        }
        gameBoard[from[0]][from[1]] = gameBoard[to[0]][to[1]];
        gameBoard[to[0]][to[1]] = null;
        switch (temp.charAt(3)) {
            // TODO: Construct the pieces properly here
            case 'p': this.put(new Pawn(new Position(0,0), Color.BLACK), to);
            case 'k': this.put(new King(new Position(0,0), Color.BLACK), to);
            case 'r': this.put(new Rook(new Position(0,0), Color.BLACK), to);
            case 'n': this.put(new Knight(new Position(0,0), Color.BLACK), to);
            case 'b': this.put(new Bishop(new Position(0,0), Color.BLACK), to);
            case 'q': this.put(new Queen(new Position(0,0), Color.BLACK), to);
            case '_':
        }
        gameHistory.remove(gameHistory.get(gameHistory.size()-1));
        return;
    }
    public List<int[][]> getLegalMoves(int[] from){
        ArrayList<Position> movelist = gameBoard[from[0]][from[1]].possibleMoves();
        ArrayList<int[][]> templist = new ArrayList<int[][]>();
        int i = 0;
        boolean canmove;
        int[] tempint;
        while(i<movelist.size()){
            canmove = true;
            tempint = new int[2];
            Position temppos = movelist.get(i);
            tempint[0] = temppos.getRow();
            tempint[1] = temppos.getColumn();
            while (tempint != from){
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
            if(canmove){
                int[][] temparray = {from,tempint};
                templist.add(temparray);
            }
            i++;
        }
        return templist;
    }
    public String pack(){
        return null;
    }
    public void unpack(String packedString){
        return;
    }
    public Piece[][] getGameBoard(){
        return gameBoard;
    }
}


