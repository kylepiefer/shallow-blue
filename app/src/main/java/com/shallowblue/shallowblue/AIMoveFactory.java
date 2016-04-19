package com.shallowblue.shallowblue;

/**
 * Created by FU on 4/18/2016.
 */
public class AIMoveFactory {
    public static AIMove newAIMove() {
        return new AIMoveRandom();
    }
}
