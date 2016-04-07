package com.shallowblue.shallowblue;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;

/**
 * This is a class to run junit testing on Knight.java
 * Created by Mohammad on 4/7/16.
 */
public class KnightTest {

    Knight wKnight = new Knight(new Position(0,0), Color.WHITE);
    Knight bKnight = new Knight(new Position(-6,0), Color.BLACK);
    Knight mKnight = new Knight(new Position(3,3), Color.WHITE);

    /**
     * A method to test hasMoved() for Knight.
     * It is set to UNUSED, since we only need it for Castling and Promotion.
     * Therefore, it should always return false.
     */
    @Test
    public void testHasMoved() {
        assertFalse(wKnight.hasMoved());
        assertFalse(bKnight.hasMoved());
        assertFalse(mKnight.hasMoved());
    }

    /**
     * A method to test isPromoting() for Knight.
     * It is set to UNUSED, since we only need it for Promotion
     * (see Pawn.java for more info)
     * Therefore, it should always return false.
     */
    @Test
    public void isPromoting() {
        assertFalse(wKnight.isPromoting());
        assertFalse(bKnight.isPromoting());
        assertFalse(mKnight.isPromoting());
    }


    /**
     * A method to test possibleMoves() for Knight.
     *
     * Expected output:
     *  wKnight (0,0):
     *      Since the white knight is in A1, its possible moves should be:
     *      C2 (1,2) and B3 (2,1)
     *      See: http://i.imgur.com/vBXoqiC.jpg
     *
     *  bKnight (-6,0):
     *      Since the black knight is out of bounds, it should return nothing
     *
     *  mKnight (3,3):
     *      Since the black knight is in D4, it should return:
     *         B3,     B5,   C6,    C2,    E6,    E2,    F5,    F3    or
     *      { (2,1), (4,1), (5,2), (1,2), (5,4), (1,4), (4,5), (2,5) }
     *      See: http://i.imgur.com/8bKQvqL.jpg
     */
    @Test
    public void testPossibleMoves() {
        ArrayList<Position> possibleMoves;

        //1- wKnight
        possibleMoves = wKnight.possibleMoves();
        assertTrue(possibleMoves.contains(new Position(1,2)));
        assertTrue(possibleMoves.contains(new Position(2,1)));


        //2- bKnight
        possibleMoves = bKnight.possibleMoves();
        assertTrue(possibleMoves.isEmpty());

        //3- mKnight
        possibleMoves = mKnight.possibleMoves();
        assertTrue(possibleMoves.contains(new Position(2,1)));
        assertTrue(possibleMoves.contains(new Position(4,1)));
        assertTrue(possibleMoves.contains(new Position(5,2)));
        assertTrue(possibleMoves.contains(new Position(1,2)));
        assertTrue(possibleMoves.contains(new Position(5,4)));
        assertTrue(possibleMoves.contains(new Position(1,4)));
        assertTrue(possibleMoves.contains(new Position(4,5)));
        assertTrue(possibleMoves.contains(new Position(2,5)));
    }


    /**
     * A method to test toString() for Knight.
     */
    @Test
    public void testToString() {
        assertTrue(wKnight.toString().equals("n"));
        assertTrue(bKnight.toString().equals("n"));
        assertTrue(mKnight.toString().equals("n"));

    }

}
