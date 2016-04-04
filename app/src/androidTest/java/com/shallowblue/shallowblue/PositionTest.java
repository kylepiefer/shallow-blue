package com.shallowblue.shallowblue;

import org.junit.Test;

import static org.junit.Assert.assertFalse;

/**
 * Created by Austin on 4/3/2016.
 */
public class PositionTest{

    @Test
    public void position_test() {
        assertFalse(new Position(0,0) == null);

    }
}
