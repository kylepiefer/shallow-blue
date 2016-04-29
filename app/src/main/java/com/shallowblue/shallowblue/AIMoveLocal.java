package com.shallowblue.shallowblue;

import android.util.Log;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Created by gauch on 4/6/2016.
 */
public class AIMoveLocal extends AIMove {
    private double aggression;

    public AIMoveLocal(double aggression) {
        this.aggression = aggression;
    }

    private static int positionsExamined = 0;

    private int[] PawnTable = new int[]
            {
                    0,  0,  0,  0,  0,  0,  0,  0,
                    50, 50, 50, 50, 50, 50, 50, 50,
                    10, 10, 20, 30, 30, 20, 10, 10,
                    5,  5, 10, 27, 27, 10,  5,  5,
                    0,  0,  0, 25, 25,  0,  0,  0,
                    5, -5,-10,  0,  0,-10, -5,  5,
                    5, 10, 10,-25,-25, 10, 10,  5,
                    0,  0,  0,  0,  0,  0,  0,  0
            };

    private int[] KnightTable = new int[]
            {
                    -50,-40,-30,-30,-30,-30,-40,-50,
                    -40,-20,  0,  0,  0,  0,-20,-40,
                    -30,  0, 10, 15, 15, 10,  0,-30,
                    -30,  5, 15, 20, 20, 15,  5,-30,
                    -30,  0, 15, 20, 20, 15,  0,-30,
                    -30,  5, 10, 15, 15, 10,  5,-30,
                    -40,-20,  0,  5,  5,  0,-20,-40,
                    -50,-40,-20,-30,-30,-20,-40,-50,
            };

    private int[] BishopTable = new int[]
            {
                    -20,-10,-10,-10,-10,-10,-10,-20,
                    -10,  0,  0,  0,  0,  0,  0,-10,
                    -10,  0,  5, 10, 10,  5,  0,-10,
                    -10,  5,  5, 10, 10,  5,  5,-10,
                    -10,  0, 10, 10, 10, 10,  0,-10,
                    -10, 10, 10, 10, 10, 10, 10,-10,
                    -10,  5,  0,  0,  0,  0,  5,-10,
                    -20,-10,-40,-10,-10,-40,-10,-20,
            };

    private int[] KingTable = new int[]
            {
                    -30, -40, -40, -50, -50, -40, -40, -30,
                    -30, -40, -40, -50, -50, -40, -40, -30,
                    -30, -40, -40, -50, -50, -40, -40, -30,
                    -30, -40, -40, -50, -50, -40, -40, -30,
                    -20, -30, -30, -40, -40, -30, -30, -20,
                    -10, -20, -20, -20, -20, -20, -20, -10,
                    20,  20,   0,   0,   0,   0,  20,  20,
                    20,  30,  10,   0,   0,  10,  30,  20
            };

    private int[] KingTableEndGame = new int[]
            {
                    -50,-40,-30,-20,-20,-30,-40,-50,
                    -30,-20,-10,  0,  0,-10,-20,-30,
                    -30,-10, 20, 30, 30, 20,-10,-30,
                    -30,-10, 30, 40, 40, 30,-10,-30,
                    -30,-10, 30, 40, 40, 30,-10,-30,
                    -30,-10, 20, 30, 30, 20,-10,-30,
                    -30,-30,  0,  0,  0,  0,-30,-30,
                    -50,-30,-30,-30,-30,-30,-30,-50
            };

    public List<Move> move(GameBoard current, int depth) {
        current = new GameBoard(current);
        List<Move> moves;

        positionsExamined = 0;
        long startTime = System.currentTimeMillis();

        if(current.playerToMove() == Color.WHITE)
            moves = maxAction(current, depth);
        else
            moves = minAction(current, depth);

        long stopTime = System.currentTimeMillis();
        float elapsedTime = (float)((stopTime - startTime) / 1000.0);
        Log.i("AIMove", "Time taken: " + String.format("%.3f", elapsedTime) + " seconds.");
        Log.i("AIMove", "Positions examined: " + positionsExamined);
        Log.i("AIMove", "Rate: " + String.format("%.3f", ((float)positionsExamined / elapsedTime)) + " positions per second");

        return moves;
    }

    //returns a sorted list of moves from best to worst
    private List<Move> maxAction(GameBoard current, int depth) {
        List<Entry<Double,Move>> moveGoodness = new ArrayList<Entry<Double,Move>>();
        double best = Double.NEGATIVE_INFINITY;
        List<Move> moves = current.getAllLegalMoves();
        Collections.shuffle(moves);
        for (Move m : moves) {
            current.move(m);
            double v = minAction(current, depth - 1, best, Double.POSITIVE_INFINITY);
            if(v > best)
                best = v;

            //the sorted map is sorted low-high so we want to negate the value before the put
            moveGoodness.add(new SimpleEntry<Double, Move>(v, m));
            current.undo();
            positionsExamined++;
        }
        Collections.sort(moveGoodness, MIN_COMPARATOR);
        List<Move> ret = new ArrayList<Move>();
        for(Entry<Double,Move> e : moveGoodness)
            ret.add(e.getValue());
        return ret;    }

    //returns a sorted list of moves from best to worst
    private List<Move> minAction(GameBoard current, int depth) {
        List<Entry<Double,Move>> moveGoodness = new ArrayList<Entry<Double,Move>>();
        double best = Double.POSITIVE_INFINITY;
        List<Move> moves = current.getAllLegalMoves();
        Collections.shuffle(moves);
        for (Move m : moves) {
            current.move(m);
            double v = maxAction(current, depth - 1, Double.NEGATIVE_INFINITY, best);
            if(v < best)
                best = v;

            moveGoodness.add(new SimpleEntry<Double, Move>(v, m));
            current.undo();
            positionsExamined++;
        }

        Collections.sort(moveGoodness, MIN_COMPARATOR);
        List<Move> ret = new ArrayList<Move>();
        for(Entry<Double,Move> e : moveGoodness)
            ret.add(e.getValue());
        return ret;
    }

    private double maxAction(GameBoard current, int depth, double alpha, double beta) {
        if(depth <= 0 || current.gameOver())
            return sbe(current);

        double v = Double.NEGATIVE_INFINITY;
        for (Move m : current.getAllLegalMoves()) {
            current.move(m);
            double nextV = minAction(current, depth-1, alpha, beta);

            if(nextV > v)
                v = nextV;
            if(nextV > alpha)
                alpha = v;
            if(nextV >= beta)
                return v;
            current.undo();
            positionsExamined++;
        }
        return v;
    }

    private double minAction(GameBoard current, int depth, double alpha, double beta) {
        if(depth <= 0 || current.gameOver())
            return sbe(current);

        double v = Double.NEGATIVE_INFINITY;
        for (Move m : current.getAllLegalMoves()) {
            current.move(m);
            double nextV = maxAction(current, depth-1, alpha, beta);

            if(nextV < v)
                v = nextV;
            if(nextV <= alpha)
                return v;
            if(nextV < beta)
                beta = v;
            current.undo();
            positionsExamined++;
        }
        return v;
    }

    public double sbe(GameBoard current) {
        Piece wking = null;
        Piece bking = null;

        int wrookC = 0;
        int wqueenC = 0;
        int wknightC = 0;
        int wbishopC = 0;
        int wpawnC = 0;
        int wkingC = 0;

        int brookC = 0;
        int bqueenC = 0;
        int bknightC = 0;
        int bbishopC = 0;
        int bpawnC = 0;
        int bkingC = 0;

        Map<Position, Piece> gameBoard = current.gameBoard;

        List<Piece> allPiecesOnBoard = new ArrayList<>();
        List<Piece> whitePiece = new ArrayList<>();
        List<Piece> blackPiece = new ArrayList<>();
        boolean endgame = false;

        if (current.getGameHistory() != null){
            if (current.getGameHistory().size() > 75){
                endgame = true;
            }
        }


        for (Position p : gameBoard.keySet()){
            allPiecesOnBoard.add(gameBoard.get(p));
            if (gameBoard.get(p).getColor() == Color.WHITE){
                whitePiece.add(gameBoard.get(p));
                if (gameBoard.get(p) instanceof King){
                    wking = gameBoard.get(p);
                }
            } else {
                blackPiece.add(gameBoard.get(p));
                if (gameBoard.get(p) instanceof King){
                    bking = gameBoard.get(p);
                }
            }
        }

        if (allPiecesOnBoard.size() < 12){
            endgame = true;
        }
        Color first = Color.WHITE;
        int calibrate = 0;
        if (current.getGameHistory() == null){
            if (wking.getPosition().getRow() == 0){
                calibrate = 7;
            }
        } else {
            Move firstM = current.getGameHistory().get(0);
            Position firstPos = firstM.getFrom();
            if (firstPos.getRow() < 2){
                calibrate = 7;
            }
        }



        int sum = 0;

        int[] blackPawnCount = new int[8];
        int[] whitePawnCount = new int[8];
        for (int i = 0; i < 8; i++){
            whitePawnCount[i] = 0;
            blackPawnCount[i] = 0;
        }


        for (Piece p : whitePiece) {

            int pCoord = ((Math.abs(calibrate - p.getPosition().getRow())) * 8) + p.getPosition().getColumn();

            if (p instanceof Rook) {
                sum += 500*aggression;
                wrookC++;
            }
            else if (p instanceof Queen) {
                sum += 900*aggression;
                wqueenC++;
            }
            else if (p instanceof Knight) {
                sum += 300*aggression;
                sum += KnightTable[pCoord];
                if (endgame){
                    sum -= 10;
                }
                wknightC++;
            }
            else if (p instanceof King) {
                sum += 10000*aggression;
                if (endgame){
                    sum += KingTableEndGame[pCoord];
                } else {
                    sum += KingTable[pCoord];
                }
                wkingC++;
            }
            else if (p instanceof Bishop) {
                 sum += 325*aggression;
                 sum += BishopTable[pCoord];
                if (endgame){
                    sum += 10;
                }
                wbishopC++;
            }
            else if (p instanceof Pawn) {
                whitePawnCount[p.getPosition().getColumn()]++;
                sum += 100*aggression;
                sum += PawnTable[pCoord];
                wpawnC++;
            }
            else
                continue; // null
        }

        if (wbishopC >= 2){
            sum += 10;
        }

        if (current.playerToMove != Color.BLACK){
            if (calibrate == 0){
                calibrate = 7;
            } else {
                calibrate = 0;
            }
        }

        for (Piece p : blackPiece){
            int pCoord = ((Math.abs(calibrate - p.getPosition().getRow())) * 8) + p.getPosition().getColumn();
            if (p instanceof Rook) {
                sum -= 500/aggression;
                brookC++;
            }
            else if (p instanceof Queen) {
                sum -= 900/aggression;
                bqueenC++;
            }
            else if (p instanceof Knight) {
                sum -= 300/aggression;
                sum -= KnightTable[pCoord];
                if (endgame){
                    sum += 10;
                }
                bknightC++;
            }
            else if (p instanceof King) {
                sum -= 10000/aggression;
                if (endgame){
                    sum -= KingTableEndGame[pCoord];
                } else {
                    sum -= KingTable[pCoord];
                }

                bkingC++;
            }
            else if (p instanceof Bishop) {
                sum -= 325/aggression;
                sum -= BishopTable[pCoord];
                if (endgame){
                    sum -= 10;
                }
                bbishopC++;
            }
            else if (p instanceof Pawn) {
                blackPawnCount[p.getPosition().getColumn()]++;
                sum -= 100/aggression;
                sum -= PawnTable[pCoord];
                bpawnC++;
            }
            else
                continue; // null
        }

        if (blackPawnCount[0] >= 1 && blackPawnCount[1] == 0)
        {
            sum += 12;
        }
        if (blackPawnCount[1] >= 1 && blackPawnCount[0] == 0 &&
                blackPawnCount[2] == 0)
        {
            sum += 14;
        }
        if (blackPawnCount[2] >= 1 && blackPawnCount[1] == 0 &&
                blackPawnCount[3] == 0)
        {
            sum += 16;
        }
        if (blackPawnCount[3] >= 1 && blackPawnCount[2] == 0 &&
                blackPawnCount[4] == 0)
        {
            sum += 20;
        }
        if (blackPawnCount[4] >= 1 && blackPawnCount[3] == 0 &&
                blackPawnCount[5] == 0)
        {
            sum += 20;
        }
        if (blackPawnCount[5] >= 1 && blackPawnCount[4] == 0 &&
                blackPawnCount[6] == 0)
        {
            sum += 16;
        }
        if (blackPawnCount[6] >= 1 && blackPawnCount[5] == 0 &&
                blackPawnCount[7] == 0)
        {
            sum += 14;
        }
        if (blackPawnCount[7] >= 1 && blackPawnCount[6] == 0)
        {
            sum += 12;
        }
//White Isolated Pawns
        if (whitePawnCount[0] >= 1 && whitePawnCount[1] == 0)
        {
            sum -= 12;
        }
        if (whitePawnCount[1] >= 1 && whitePawnCount[0] == 0 &&
                whitePawnCount[2] == 0)
        {
            sum -= 14;
        }
        if (whitePawnCount[2] >= 1 && whitePawnCount[1] == 0 &&
                whitePawnCount[3] == 0)
        {
            sum -= 16;
        }
        if (whitePawnCount[3] >= 1 && whitePawnCount[2] == 0 &&
                whitePawnCount[4] == 0)
        {
            sum -= 20;
        }
        if (whitePawnCount[4] >= 1 && whitePawnCount[3] == 0 &&
                whitePawnCount[5] == 0)
        {
            sum -= 20;
        }
        if (whitePawnCount[5] >= 1 && whitePawnCount[4] == 0 &&
                whitePawnCount[6] == 0)
        {
            sum -= 16;
        }
        if (whitePawnCount[6] >= 1 && whitePawnCount[5] == 0 &&
                whitePawnCount[7] == 0)
        {
            sum -= 14;
        }
        if (whitePawnCount[7] >= 1 && whitePawnCount[6] == 0)
        {
            sum -= 12;
        }

//Black Passed Pawns
        if (blackPawnCount[0] >= 1 && whitePawnCount[0] == 0)
        {
            sum -= blackPawnCount[0];
        }
        if (blackPawnCount[1] >= 1 && whitePawnCount[1] == 0)
        {
            sum -= blackPawnCount[1];
        }
        if (blackPawnCount[2] >= 1 && whitePawnCount[2] == 0)
        {
            sum -= blackPawnCount[2];
        }
        if (blackPawnCount[3] >= 1 && whitePawnCount[3] == 0)
        {
            sum -= blackPawnCount[3];
        }
        if (blackPawnCount[4] >= 1 && whitePawnCount[4] == 0)
        {
            sum -= blackPawnCount[4];
        }
        if (blackPawnCount[5] >= 1 && whitePawnCount[5] == 0)
        {
            sum -= blackPawnCount[5];
        }
        if (blackPawnCount[6] >= 1 && whitePawnCount[6] == 0)
        {
            sum -= blackPawnCount[6];
        }
        if (blackPawnCount[7] >= 1 && whitePawnCount[7] == 0)
        {
            sum -= blackPawnCount[7];
        }

//White Passed Pawns
        if (whitePawnCount[0] >= 1 && blackPawnCount[1] == 0)
        {
            sum += whitePawnCount[0];
        }
        if (whitePawnCount[1] >= 1 && blackPawnCount[1] == 0)
        {
            sum += whitePawnCount[1];
        }
        if (whitePawnCount[2] >= 1 && blackPawnCount[2] == 0)
        {
            sum += whitePawnCount[2];
        }
        if (whitePawnCount[3] >= 1 && blackPawnCount[3] == 0)
        {
            sum += whitePawnCount[3];
        }
        if (whitePawnCount[4] >= 1 && blackPawnCount[4] == 0)
        {
            sum += whitePawnCount[4];
        }
        if (whitePawnCount[5] >= 1 && blackPawnCount[5] == 0)
        {
            sum += whitePawnCount[5];
        }
        if (whitePawnCount[6] >= 1 && blackPawnCount[6] == 0)
        {
            sum += whitePawnCount[6];
        }
        if (whitePawnCount[7] >= 1 && blackPawnCount[7] == 0)
        {
            sum += whitePawnCount[7];
        }

        if (bbishopC >= 2){
            sum -= 10;
        }

        if (current.playerToMove == Color.WHITE){
            if (current.inCheck()){
                sum -= 175;
                if (current.getAllLegalMoves().isEmpty()){
                    sum -= 100000;
                }
            }

        } else {
            if (current.inCheck()){
                sum += 175;
                if (current.getAllLegalMoves().isEmpty()){
                    sum -= 100000;
                }
            }

        }

        return sum;
    }

}
