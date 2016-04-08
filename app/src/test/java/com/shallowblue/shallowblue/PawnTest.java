package com.shallowblue.shallowblue;
import junit.framework.Assert;

import org.junit.Test;



/**
 * Created by Mohammad on 4/6/2016
 * This class is used to utilize junit testing on Pawn.java
 */
public class PawnTest {

    //These are pawns created for testing as many cases as possible:

    //1- a white pawn at its regular starting position
    Pawn wPawn = new Pawn(new Position(1,1), Color.WHITE);

    //2- a black pawn in an illegal position
    Pawn bPawn = new Pawn(new Position(-7,-7), Color.BLACK);

    //3- a white pawn in the final rank, so it qualifies for a promotion
    Pawn pPawn = new Pawn(new Position(7,7), Color.WHITE);


    /**
     * This method is to test the initial movement flag of the Pawn.
     * A newly instantiated Pawn should not have moved yet.
     */
    @Test
    public void testHasMoved() {
        Assert.assertFalse(wPawn.hasMoved());
        Assert.assertFalse(bPawn.hasMoved());
        Assert.assertFalse(pPawn.hasMoved());
    }

    /**
     * This method tests whether or not a Pawn is promoting
     * A Pawn is promoting if it's currently in the final rank with respect to
     * its color.
     * Therefore, only pPawn's isPromoting() should return true.
     */
    @Test
    public void testIsPromoting() {
        Assert.assertFalse(wPawn.isPromoting());
        Assert.assertFalse(bPawn.isPromoting());
        Assert.assertTrue(pPawn.isPromoting());
    }


    /**
     * This method tests a Pawn's possible moves
     * 'possible' = list of moves a piece can make if it were the only piece in the board
     * Since wPawn has an initial Position, its list size is 4 (1&2 steps forward, 2 diagonals)
     * Since bPawn has an illegal Position, its list should be empty
     * Since pPawn is in the final rank, its list should also be empty
     */
    @Test
    public void testPossibleMoves() {
        Assert.assertTrue(wPawn.possibleMoves().size() == 4);
        Assert.assertTrue(bPawn.possibleMoves().size() == 0);
        Assert.assertTrue(pPawn.possibleMoves().size() == 0);
    }


    /**
     * This method tests the return value for toString().
     * This might be a redundant test. It's primarily written for code-coverage
     */
    @Test
    public void testToString() {
        Assert.assertTrue(wPawn.toString().equals("p"));
        Assert.assertTrue(bPawn.toString().equals("p"));
        Assert.assertTrue(pPawn.toString().equals("p"));
    }


}
