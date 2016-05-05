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

    public List<Move> move(GameBoard in, int depth) {
        synchronized(in) {
            GameBoard current = new GameBoard(in);
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
    }

    //returns a sorted list of moves from best to worst
    private List<Move> maxAction(GameBoard in, int depth) {
        
        List<Entry<Double,Move>> moveGoodness = new ArrayList<Entry<Double,Move>>();
        double best = Double.NEGATIVE_INFINITY;
        List<Move> moves = in.getAllLegalMoves();
        Collections.shuffle(moves);
        for (Move m : moves) {
            GameBoard current = new GameBoard(in);
            current.move(m);
            double v = minAction(current, depth - 1, best, Double.POSITIVE_INFINITY);
            if(v > best)
                best = v;

            //the sorted map is sorted low-high so we want to negate the value before the put
            moveGoodness.add(new SimpleEntry<Double, Move>(v, m));
            //current.undo();
            positionsExamined++;
        }
        Collections.sort(moveGoodness, MAX_COMPARATOR);
        List<Move> ret = new ArrayList<Move>();
        for(Entry<Double,Move> e : moveGoodness) {
            Log.i("AIMoveLocalMax", "Move: " + e.getValue().toString() + " Score: " + e.getKey().toString());
            ret.add(e.getValue());
            break;
        }
        return ret;
    }

    //returns a sorted list of moves from best to worst
    private List<Move> minAction(GameBoard in, int depth) {
        List<Entry<Double,Move>> moveGoodness = new ArrayList<Entry<Double,Move>>();
        double best = Double.POSITIVE_INFINITY;
        List<Move> moves = in.getAllLegalMoves();
        Collections.shuffle(moves);
        for (Move m : moves) {
            GameBoard current = new GameBoard(in);
            current.move(m);
            double v = maxAction(current, depth - 1, Double.NEGATIVE_INFINITY, best);
            if(v < best)
                best = v;

            moveGoodness.add(new SimpleEntry<Double, Move>(v, m));
            //current.undo();
            positionsExamined++;
        }

        Collections.sort(moveGoodness, MIN_COMPARATOR);
        List<Move> ret = new ArrayList<Move>();
        for(Entry<Double,Move> e : moveGoodness) {
            Log.i("AIMoveLocalMin", "Move: " + e.getValue().toString() + " Score: " + e.getKey().toString());
            ret.add(e.getValue());
            break;
        }
        return ret;
    }

    private double maxAction(GameBoard in, int depth, double alpha, double beta) {
        if(depth <= 0 || in.gameOver())
            return sbe(in);

        double v = Double.NEGATIVE_INFINITY;
        for (Move m : in.getAllLegalMoves()) {
            GameBoard current = new GameBoard(in);
            current.move(m);
            double nextV = minAction(current, depth-1, alpha, beta);

            if(nextV > v)
                v = nextV;
            if(nextV > alpha)
                alpha = v;
            if(nextV >= beta)
                return v;
            //current.undo();
            positionsExamined++;
        }
        return v;
    }

    private double minAction(GameBoard in, int depth, double alpha, double beta) {
        if(depth <= 0 || in.gameOver())
            return sbe(in);

        double v = Double.POSITIVE_INFINITY;
        for (Move m : in.getAllLegalMoves()) {
            GameBoard current = new GameBoard(in);
            current.move(m);
            double nextV = maxAction(current, depth-1, alpha, beta);

            if(nextV < v)
                v = nextV;
            if(nextV <= alpha)
                return v;
            if(nextV < beta)
                beta = v;
            //current.undo();
            positionsExamined++;
        }
        return v;
    }

    public double sbe(GameBoard current) {
        if(current.gameOver()) {
            if(current.isDraw())
                return 0;
            if(current.inCheckMate()) {
                if(current.playerToMove == Color.BLACK)
                    return Double.POSITIVE_INFINITY;
                else
                    return Double.NEGATIVE_INFINITY;
            } else Log.i("AIMoveLocalSBE:", "This shouldn't happen");
        }


        int rookC[] = new int[2];
        int queenC[] = new int[2];
        int knightC[] = new int[2];
        int bishopC[] = new int[2];
        int pawnC[] = new int[2];
        int kingC[] = new int[2];


        boolean endgame = false;

        if (current.getGameHistory() != null && current.getGameHistory().size() > 75)
            endgame = true;


        if (current.gameBoard.size() < 12){
            endgame = true;
        }


        int sum = 0;
        int[][] PawnCount = new int[2][8];

        for (Piece p : current.gameBoard.values()) {
            int delta = 0;
            int cIndex;
            double pMult;
            int pCoord;
            if(p.getColor() == Color.WHITE) {
                pCoord = (7 - p.getPosition().getRow()) * 8 + p.getPosition().getColumn();
                pMult = aggression;
                cIndex = 0;
            }
            else {
                pCoord = p.getPosition().getRow()*8 + p.getPosition().getColumn();
                pMult = 1/aggression;
                cIndex = 1;
            }

            if (p instanceof Rook) {
                delta += 500*pMult;
                rookC[cIndex]++;
            }
            else if (p instanceof Queen) {
                delta += 900*pMult;
                queenC[cIndex]++;
            }
            else if (p instanceof Knight) {
                delta += 300*pMult;
                delta += KnightTable[pCoord];
                if (endgame){
                    delta -= 10;
                }
                knightC[cIndex]++;
            }
            else if (p instanceof King) {
                if (endgame){
                    delta += KingTableEndGame[pCoord];
                } else {
                    delta += KingTable[pCoord];
                }
                kingC[cIndex]++;
            }
            else if (p instanceof Bishop) {
                delta += 325*pMult;
                delta += BishopTable[pCoord];
                if (endgame){
                    delta += 10;
                }
                bishopC[cIndex]++;
                if(bishopC[cIndex] == 2)
                    delta += 10;
            }
            else if (p instanceof Pawn) {
                PawnCount[cIndex][p.getPosition().getColumn()]++;
                delta += 100*pMult;
                delta += PawnTable[pCoord];
                pawnC[cIndex]++;
            }
            else
                Log.i("Warning: ", "null pieces in GameBoard"); // null

            if(p.getColor() == Color.WHITE)
                sum += delta;
            else
                sum -= delta;
        }


        //Black Isolated Pawns
        if (PawnCount[1][0] >= 1 && PawnCount[1][1] == 0)
        {
            sum += 12;
        }
        if (PawnCount[1][1] >= 1 && PawnCount[1][0] == 0 &&
                PawnCount[1][2] == 0)
        {
            sum += 14;
        }
        if (PawnCount[1][2] >= 1 && PawnCount[1][1] == 0 &&
                PawnCount[1][3] == 0)
        {
            sum += 16;
        }
        if (PawnCount[1][3] >= 1 && PawnCount[1][2] == 0 &&
                PawnCount[1][4] == 0)
        {
            sum += 20;
        }
        if (PawnCount[1][4] >= 1 && PawnCount[1][3] == 0 &&
                PawnCount[1][5] == 0)
        {
            sum += 20;
        }
        if (PawnCount[1][5] >= 1 && PawnCount[1][4] == 0 &&
                PawnCount[1][6] == 0)
        {
            sum += 16;
        }
        if (PawnCount[1][6] >= 1 && PawnCount[1][5] == 0 &&
                PawnCount[1][7] == 0)
        {
            sum += 14;
        }
        if (PawnCount[1][7] >= 1 && PawnCount[1][6] == 0)
        {
            sum += 12;
        }

        //White Isolated Pawns
        if (PawnCount[0][0] >= 1 && PawnCount[0][1] == 0)
        {
            sum -= 12;
        }
        if (PawnCount[0][1] >= 1 && PawnCount[0][0] == 0 &&
                PawnCount[0][2] == 0)
        {
            sum -= 14;
        }
        if (PawnCount[0][2] >= 1 && PawnCount[0][1] == 0 &&
                PawnCount[0][3] == 0)
        {
            sum -= 16;
        }
        if (PawnCount[0][3] >= 1 && PawnCount[0][2] == 0 &&
                PawnCount[0][4] == 0)
        {
            sum -= 20;
        }
        if (PawnCount[0][4] >= 1 && PawnCount[0][3] == 0 &&
                PawnCount[0][5] == 0)
        {
            sum -= 20;
        }
        if (PawnCount[0][5] >= 1 && PawnCount[0][4] == 0 &&
                PawnCount[0][6] == 0)
        {
            sum -= 16;
        }
        if (PawnCount[0][6] >= 1 && PawnCount[0][5] == 0 &&
                PawnCount[0][7] == 0)
        {
            sum -= 14;
        }
        if (PawnCount[0][7] >= 1 && PawnCount[0][6] == 0)
        {
            sum -= 12;
        }

//Black Passed Pawns
        if (PawnCount[1][0] >= 1 && PawnCount[0][0] == 0)
        {
            sum -= PawnCount[1][0];
        }
        if (PawnCount[1][1] >= 1 && PawnCount[0][1] == 0)
        {
            sum -= PawnCount[1][1];
        }
        if (PawnCount[1][2] >= 1 && PawnCount[0][2] == 0)
        {
            sum -= PawnCount[1][2];
        }
        if (PawnCount[1][3] >= 1 && PawnCount[0][3] == 0)
        {
            sum -= PawnCount[1][3];
        }
        if (PawnCount[1][4] >= 1 && PawnCount[0][4] == 0)
        {
            sum -= PawnCount[1][4];
        }
        if (PawnCount[1][5] >= 1 && PawnCount[0][5] == 0)
        {
            sum -= PawnCount[1][5];
        }
        if (PawnCount[1][6] >= 1 && PawnCount[0][6] == 0)
        {
            sum -= PawnCount[1][6];
        }
        if (PawnCount[1][7] >= 1 && PawnCount[0][7] == 0)
        {
            sum -= PawnCount[1][7];
        }

//White Passed Pawns
        if (PawnCount[0][0] >= 1 && PawnCount[1][1] == 0)
        {
            sum += PawnCount[0][0];
        }
        if (PawnCount[0][1] >= 1 && PawnCount[1][1] == 0)
        {
            sum += PawnCount[0][1];
        }
        if (PawnCount[0][2] >= 1 && PawnCount[1][2] == 0)
        {
            sum += PawnCount[0][2];
        }
        if (PawnCount[0][3] >= 1 && PawnCount[1][3] == 0)
        {
            sum += PawnCount[0][3];
        }
        if (PawnCount[0][4] >= 1 && PawnCount[1][4] == 0)
        {
            sum += PawnCount[0][4];
        }
        if (PawnCount[0][5] >= 1 && PawnCount[1][5] == 0)
        {
            sum += PawnCount[0][5];
        }
        if (PawnCount[0][6] >= 1 && PawnCount[1][6] == 0)
        {
            sum += PawnCount[0][6];
        }
        if (PawnCount[0][7] >= 1 && PawnCount[1][7] == 0)
        {
            sum += PawnCount[0][7];
        }

        if(current.inCheck()) {
            if(current.playerToMove == Color.WHITE)
                sum -= 175;
            else
                sum += 175;
        }

        return sum;
    }

}
