package com.shallowblue.shallowblue;

import java.util.List;

/**
 * Created by FU on 4/18/2016.
 */
public class AIMoveRemote extends AIMove {
    UrlConnection conn;
    public AIMoveRemote() {
        conn = new UrlConnection("","","");
    }

    public List<Move> move(GameBoard current, int depth) {
        String packedGameBoard = current.pack();
        conn.UrlRequest(packedGameBoard,"","");


        return AIMoveFactory.newAIMove(false).move(current,depth);
    }
}
