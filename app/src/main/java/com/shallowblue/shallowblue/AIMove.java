package com.shallowblue.shallowblue;

import android.util.Log;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;

/**
 * Created by gauch on 4/6/2016.
 */
public class AIMove {

    protected static final Comparator<Entry<Double,Move>> MIN_COMPARATOR =
            new Comparator<Entry<Double,Move>>() {
                public int compare(Entry<Double,Move> e1, Entry<Double,Move> e2){
                    return Double.compare(e1.getKey(), e2.getKey());
                }
            };
    protected static final Comparator<Entry<Double,Move>> MAX_COMPARATOR =
            new Comparator<Entry<Double,Move>>() {
                public int compare(Entry<Double,Move> e1, Entry<Double,Move> e2){
                    return Double.compare(e2.getKey(), e1.getKey());
                }
            };

    public List<Move> move(GameBoard current, int depth) {
        current = new GameBoard(current);
        if(current.playerToMove() == Color.WHITE)
            return maxAction(current, depth);
        return minAction(current, depth);
    }

    //returns a sorted list of moves from best to worst
    private List<Move> maxAction(GameBoard current, int depth) {
        List<Entry<Double,Move>> moveGoodness = new ArrayList<Entry<Double,Move>>();
        double best = Double.NEGATIVE_INFINITY;
        List<Move> moves = current.getAllMoves();
        Collections.shuffle(moves);
        for (Move m : moves) {
            current.move(m);
            double v = minAction(new GameBoard(current), depth - 1, best, Double.POSITIVE_INFINITY);
            if(v > best)
                best = v;

            //the sorted map is sorted low-high so we want to negate the value before the put
            moveGoodness.add(new SimpleEntry<Double, Move>(v, m));
            //current.undo();
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
        List<Move> = current.getAllMoves();
        Collections.shuffle(moves);
        for (Move m : moves) {
            current.move(m);
            double v = maxAction(new GameBoard(current), depth - 1, Double.NEGATIVE_INFINITY, best);
            if(v < best)
                best = v;

            moveGoodness.add(new SimpleEntry<Double, Move>(v, m));
            current.undo();
        }

        Collections.sort(moveGoodness, MIN_COMPARATOR);
        List<Move> ret = new ArrayList<Move>();
        for(Entry<Double,Move> e : moveGoodness)
            ret.add(e.getValue());
        return ret;
    }

    private double maxAction(GameBoard current, int depth, double alpha, double beta) {
        if(depth <= 0 || current.gameOver())
            return current.sbe();

        double v = Double.NEGATIVE_INFINITY;
        for (Move m : current.getAllMoves()) {
            current.move(m);
            double nextV = minAction(new GameBoard(current), depth-1, alpha, beta);

            if(nextV > v)
                v = nextV;
            if(nextV > alpha)
                alpha = v;
            if(nextV >= beta)
                return v;
            current.undo();
        }
        return v;
    }

    private double minAction(GameBoard current, int depth, double alpha, double beta) {
        if(depth <= 0 || current.gameOver())
            return current.sbe();

        double v = Double.NEGATIVE_INFINITY;
        for (Move m : current.getAllMoves()) {
            current.move(m);
            double nextV = maxAction(new GameBoard(current), depth-1, alpha, beta);

            if(nextV < v)
                v = nextV;
            if(nextV <= alpha)
                return v;
            if(nextV < beta)
                beta = v;
            //current.undo();
        }
        return v;
    }
}
