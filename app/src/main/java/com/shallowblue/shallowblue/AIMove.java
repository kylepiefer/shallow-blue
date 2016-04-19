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
public abstract class AIMove {

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

    public abstract List<Move> move(GameBoard current, int depth);

}
