package com.shallowblue.shallowblue;

/**
 * Created by FU on 4/18/2016.
 */
public class AIMoveFactory {
    public static AIMove newAIMove(boolean remote, double aggression) {
        if(remote)
            return new AIMoveRemote(aggression);
        //return new AIMoveRandom(aggression);
        return new AIMoveRandom();
    }

    public static AIMove newAIMove(double aggression) {
        return newAIMove(false, aggression);
    }

    public static AIMove newAIMove() {
        return newAIMove(1.0);
    }
}
