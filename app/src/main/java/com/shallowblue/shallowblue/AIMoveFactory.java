package com.shallowblue.shallowblue;

/**
 * Created by FU on 4/18/2016.
 */
public class AIMoveFactory {
    public static AIMove newAIMove(boolean remote) {
        if(remote)
            return new AIMoveRemote();
        return new AIMoveRandom();
    }

    public static AIMove newAIMove() {
        return newAIMove(false);
    }
}
