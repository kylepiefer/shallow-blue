package com.shallowblue.shallowblue;

import java.util.List;

/**
 * Created by FU on 4/18/2016.
 */
public class AIMoveRemote extends AIMove {
    private double aggression;
    UrlConnection conn;
    public AIMoveRemote(double aggression) {
        conn = new UrlConnection("", "", "");
        this.aggression = aggression;
    }

    public List<Move> move(GameBoard current, int depth) {
        String packedGameBoard = current.pack();
        //System.out.println(packedGameBoard);
        //depth=7;
        String ret=conn.UrlRequest(packedGameBoard,""+depth,""+aggression);
        if(ret.equals(""))
        {
            return AIMoveFactory.newAIMove(false, aggression).move(current,depth);
        }
        return current.parseServerOutput(ret);
    }
}
