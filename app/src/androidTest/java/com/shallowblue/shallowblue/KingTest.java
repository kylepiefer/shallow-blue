package com.shallowblue.shallowblue;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;


/**
 * Created by Mohammad on 4/7/16.
 * This is a class to implement junit testing on King.java
 */
public class KingTest {

    //white king in its proper location
    King wKing = new King(new Position(0,4), Color.WHITE);

    //an out-of-bounds black king
    King bKing = new King(new Position(-7,-7), Color.BLACK);

    //white king in (roughly) the middle of the board (d4)
    King mKing = new King(new Position(3,3), Color.WHITE);


    /**
     * A method to test hasMoved()
     * hasMoved() = whether or not a king has moved from its original position
     * Since they are newly instantiated, all kings should return false.
     */
    @Test
    public void testHasMoved() {
        assertFalse(wKing.hasMoved());
        assertFalse(bKing.hasMoved());
        assertFalse(mKing.hasMoved());
    }


    /**
     * A method to test isPromoting()
     * isPromoting() = whether or not a King's current position promotes him
     * Since Kings can't promote, all kings should return false.
     */
    @Test
    public void testIsPromoting() {
        assertFalse(wKing.isPromoting());
        assertFalse(bKing.isPromoting());
        assertFalse(mKing.isPromoting());
    }


    /**
     * A method to test possibleMoves()
     * possibleMoves() = arraylist of moves a king can make if it were the only
     * piece on the board. Moves here are defined as ArrayList<Position>
     *
     * Expected output:
     *  wKing:
     *      Since it's in its original position, its possible moves should be:
     *      Size 5 (forward, right, left, diagonal right, diagonal left)
     *      See: http://i.imgur.com/st064Pj.jpg
     *
     *  bKing:
     *      Since it's in an illegal position (out of bounds), its output should be:
     *      Empty arraylist (no moves to make)
     *
     *  mKing:
     *      Since it's in the middle of the board, its possible moves should be:
     *      Size 8 (all directions)
     *      See: http://i.imgur.com/bOZclfU.jpg
     */
    @Test
    public void testPossibleMoves() {
        ArrayList<Position> possibleMoves;


        /*** wKing ***/
        possibleMoves = wKing.possibleMoves();

        assertTrue(possibleMoves.size() == 5);
        assertTrue(possibleMoves.contains(new Position(1,4)));
        assertTrue(possibleMoves.contains(new Position(0,5)));
        assertTrue(possibleMoves.contains(new Position(0,3)));
        assertTrue(possibleMoves.contains(new Position(1,5)));
        assertTrue(possibleMoves.contains(new Position(1, 3)));


        /*** bKing ***/
        possibleMoves = bKing.possibleMoves();

        assertTrue(possibleMoves.isEmpty());


        /*** mKing ***/
        possibleMoves = mKing.possibleMoves();

        assertTrue(possibleMoves.size() == 8);

        assertTrue(possibleMoves.contains(new Position(4,2)));
        assertTrue(possibleMoves.contains(new Position(4,3)));
        assertTrue(possibleMoves.contains(new Position(4,4)));

        assertTrue(possibleMoves.contains(new Position(3,2)));
        assertTrue(possibleMoves.contains(new Position(3,4)));

        assertTrue(possibleMoves.contains(new Position(2,2)));
        assertTrue(possibleMoves.contains(new Position(2,3)));
        assertTrue(possibleMoves.contains(new Position(2,4)));


    }

    /**
     * A method to test toString()
     * Might be insignificant. Created primarily for thoroughness
     */
    @Test
    public void testToString() {
        assertTrue(wKing.toString().equals("p"));
        assertTrue(bKing.toString().equals("p"));
        assertTrue(mKing.toString().equals("p"));

    }



}
