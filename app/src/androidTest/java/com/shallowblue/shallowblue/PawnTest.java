package com.shallowblue.shallowblue;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
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
        assertFalse(wPawn.hasMoved());
        assertFalse(bPawn.hasMoved());
        assertFalse(pPawn.hasMoved());
    }

    /**
     * This method tests whether or not a Pawn is promoting
     * A Pawn is promoting if it's currently in the final rank with respect to
     * its color.
     * Therefore, only pPawn's isPromoting() should return true.
     */
    @Test
    public void testIsPromoting() {
        assertFalse(wPawn.isPromoting());
        assertFalse(bPawn.isPromoting());
        assertTrue(pPawn.isPromoting());
    }


    /**
     * This method tests a Pawn's possible moves
     * 'possible' = list of moves a piece can make if it were the only piece in the board
     * Since wPawn has an initial Position, its list should be of size 2 (1 move & 2 moves)
     * Since bPawn has an illegal Position, its list should be empty
     * Since pPawn is in the final rank, its list should also be empty
     */
    @Test
    public void testPossibleMoves() {
        assertTrue(wPawn.possibleMoves().size() == 2);
        assertTrue(bPawn.possibleMoves().size() == 0);
        assertTrue(pPawn.possibleMoves().size() == 0);
    }


    /**
     * This method tests the return value for toString().
     * This might be a redundant test. It's primarily written for code-coverage
     */
    @Test
    public void testToString() {
        assertTrue(wPawn.toString().equals("p"));
        assertTrue(bPawn.toString().equals("p"));
        assertTrue(pPawn.toString().equals("p"));
    }


}