package com.shallowblue.shallowblue;

import java.util.Collections;
import java.util.List;

/**
 * Created by FU on 4/18/2016.
 */
public class AIMoveRandom extends AIMove {

    @Override
    public List<Move> move(GameBoard current, int depth) {
        List<Move> moves = current.getAllMoves();
        Collections.shuffle(moves);
        return moves;
    }
}
