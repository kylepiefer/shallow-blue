package com.shallowblue.shallowblue;

import org.junit.Test;
import org.junit.Assert;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by gauch on 4/7/2016.
 */
public class AIMoveTest extends AIMove{

    @Test
    public static void comparatorTest() {
        List<Map.Entry<Double,Move>> input = new ArrayList<Map.Entry<Double,Move>>();
        input.addAll(Arrays.asList(
                new AbstractMap.SimpleEntry<Double, Move>(2.0,null),
                new AbstractMap.SimpleEntry<Double, Move>(3.0,null),
                new AbstractMap.SimpleEntry<Double, Move>(1.0,null)
        ));

        Collections.sort(input, MIN_COMPARATOR);
        Assert.assertTrue(input.get(0).getKey() == 1.0);
        Assert.assertTrue(input.get(1).getKey() == 2.0);
        Assert.assertTrue(input.get(2).getKey() == 3.0);

        Collections.sort(input, MAX_COMPARATOR);
        Assert.assertTrue(input.get(0).getKey() == 3.0);
        Assert.assertTrue(input.get(1).getKey() == 2.0);
        Assert.assertTrue(input.get(2).getKey() == 1.0);

    }
}
