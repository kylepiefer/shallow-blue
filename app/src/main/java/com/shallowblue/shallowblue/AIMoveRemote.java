package com.shallowblue.shallowblue;

import java.util.List;

/**
 * Created by FU on 4/18/2016.
 */
public class AIMoveRemote extends AIMove {
    private double aggression;
    UrlConnection conn;
    public AIMoveRemote(double aggression) {
        conn = new UrlConnection("","",""); this.aggression = aggression;
    }

    public List<Move> move(GameBoard current, int depth) {
        String packedGameBoard = current.pack();
        conn.UrlRequest(packedGameBoard,"","");


        return AIMoveFactory.newAIMove(false, aggression).move(current,depth);
    }
}
