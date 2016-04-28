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
        //System.out.println(packedGameBoard);
        String ret=conn.UrlRequest(packedGameBoard,""+depth,"");
        //ret = ret.replaceAll("<lol>", "\n");
        //System.out.println(ret);
        return AIMoveFactory.newAIMove(false).move(current, depth);
    }
}
