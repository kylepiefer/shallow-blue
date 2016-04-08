package com.shallowblue.shallowblue;

import junit.framework.Assert;

import org.junit.Test;

/**
 * This is a class to run junit testing on Position.java
 * Created by Austin on 4/3/2016.
 * updated by Mohammad on 4/7/2016.
 */
public class PositionTest {

    Position position0 = new Position(0,0);
    Position position1 = new Position(4,6);
    Position position2 = new Position(-6,-6);
    Position position3 = new Position(17,74);

    /**
     * This is method to test Position() constructor.
     * While Row and Column should range from 0-7, inclusive, the constructor should
     * accept any range of integers.
     */
    @Test
    public void position_test() {
        Assert.assertFalse(position0 == null);
        Assert.assertFalse(position1 == null);
        Assert.assertFalse(position2 == null);
        Assert.assertFalse(position3 == null);
    }

    /**
     * This is a method to test getRow() and getColumn().
     * Straightforward: compares methods' outputs to instantiation arguments.
     */
    @Test
    public void testGetters() {
        Assert.assertTrue(position0.getRow() == 0 && position0.getColumn() == 0);
        Assert.assertTrue(position1.getRow() == 4 && position1.getColumn() == 6);
        Assert.assertTrue(position2.getRow() == -6 && position2.getColumn() == -6);
        Assert.assertTrue(position3.getRow() == 17 && position3.getColumn() == 74);
    }

    /**
     * This is a method to test equals() for Position objects.
     *
     * We overrode the standard equals() method to return true if two Position
     * objects have the same Rows and Columns.
     */
    @Test
    public void testEquals() {

        Assert.assertFalse(position0.equals(position1));
        Assert.assertFalse(position2.equals(position3));

        Assert.assertTrue(position0.equals(new Position(0, 0)));
        Assert.assertTrue(position1.equals(position1));

    }

    /**
     * This is a method to test hashCode() for Position.
     * We overrode the hashing function to work as follows:
     *
     * Given a Position object's Row and Column, hashing equation is:
     * hashCode = ( (31 + Row) * 31 ) + Column
     *
     * Therefore, expected output for each of the objects above are:
     * Position   (0,0):  ((31 + 0)*31 ) + 0   = 961
     * Position   (4,6):  ((31 + 4)*31 ) + 6   = 1091
     * Position (-6,-6):  ((31 + -6)*31 ) + -6 = 769
     * Position (17,74):  ((31 + 17)*31 ) + 74 = 1562
     *
     */
    @Test
    public void testHashCode() {

        //1- Position (0,0)
        Assert.assertTrue(position0.hashCode() == 961);

        //2- Position (4,6)
        Assert.assertTrue(position1.hashCode() == 1091);

        //3- Position (-6,-6)
        Assert.assertTrue(position2.hashCode() == 769);

        //4- Position (17,74)
        Assert.assertTrue(position3.hashCode() == 1562);

    }

    /**
     * A method to test toString()
     */
    @Test
    public void testToString() {
        Assert.assertTrue(position0.toString().equals("(0,0)"));
        Assert.assertTrue(position1.toString().equals("(4,6)"));
        Assert.assertTrue(position2.toString().equals("(-6,-6)"));
        Assert.assertTrue(position3.toString().equals("(17,74)"));
    }



}
