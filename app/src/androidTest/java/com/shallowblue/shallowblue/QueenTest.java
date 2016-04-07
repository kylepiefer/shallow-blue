package com.shallowblue.shallowblue;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;

/**
 * This class is created to junit-test Queen.java
 * Created by Mohammad on 4/7/16.
 */
public class QueenTest {

    Queen wQueen = new Queen(new Position(0,3), Color.WHITE);
    Queen bQueen = new Queen(new Position(-7,0), Color.BLACK);

    /**
     * A method to test hasMoved() for Queen.
     * It is set to UNUSED, since we only need it for Castling and Promotion.
     * Therefore, it should always return false.
     */
    @Test
    public void testHasMoved() {
        assertFalse(wQueen.hasMoved());
        assertFalse(bQueen.hasMoved());
    }

    /**
     * A method to test isPromoting() for Queen.
     * It is set to UNUSED, since we only need it for Promotion
     * (see Pawn.java for more info)
     * Therefore, it should always return false.
     */
    @Test
    public void isPromoting() {
        assertFalse(wQueen.isPromoting());
        assertFalse(bQueen.isPromoting());
    }



    /**
     * This is a method to test possible moves for Queen.
     *
     * Expected output:
     *
     *      wQueen (0,3):
     *          This white queen is in its original starting position (D1)
     *          So its possible moves should be:
     *
     *          The entire row:               A1, B1, C1, E1, F1, G1, H1.
     *          The entire column:  D2, D3,   D4, D5, D6, D7, D8.
     *          top-left diagonal until A4:   C2, B3, A4.
     *          top-right diagonal until H5:  E2, F3, G4, H5.
     *
     *          This translates to:
     *          {
     *          (0,0), (0,1), (0,2), (0,4), (0,5), (0,6), (0,7),
     *          (1,3), (2,3), (3,3), (4,3), (5,3), (6,3), (7,3),
     *          (1,2), (2,1), (3,0),
     *          (1,4), (2,5), (3,6), (4,7)
     *          }
     *
     *          See: http://i.imgur.com/oUc9fch.jpg
     *
     *      bQueen (-7,0):
     *          Since this queen is out of bounds, it should return nothing.
     *
     *
     */
    @Test
    public void testPossibleMoves() {

        ArrayList<Position> possibleMoves;

        //1- wQueen
        possibleMoves = wQueen.possibleMoves();

        assertTrue(possibleMoves.contains(new Position(0,0)));
        assertTrue(possibleMoves.contains(new Position(0,1)));
        assertTrue(possibleMoves.contains(new Position(0,2)));
        assertTrue(possibleMoves.contains(new Position(0,4)));
        assertTrue(possibleMoves.contains(new Position(0,5)));
        assertTrue(possibleMoves.contains(new Position(0,6)));
        assertTrue(possibleMoves.contains(new Position(0,7)));

        assertTrue(possibleMoves.contains(new Position(1,3)));
        assertTrue(possibleMoves.contains(new Position(2,3)));
        assertTrue(possibleMoves.contains(new Position(3,3)));
        assertTrue(possibleMoves.contains(new Position(4,3)));
        assertTrue(possibleMoves.contains(new Position(5,3)));
        assertTrue(possibleMoves.contains(new Position(6,3)));
        assertTrue(possibleMoves.contains(new Position(7,3)));

        assertTrue(possibleMoves.contains(new Position(1,2)));
        assertTrue(possibleMoves.contains(new Position(2,1)));
        assertTrue(possibleMoves.contains(new Position(3,0)));

        assertTrue(possibleMoves.contains(new Position(1,4)));
        assertTrue(possibleMoves.contains(new Position(2,5)));
        assertTrue(possibleMoves.contains(new Position(3,6)));
        assertTrue(possibleMoves.contains(new Position(4,7)));




        //2- bQueen
        possibleMoves = bQueen.possibleMoves();
        assertTrue(possibleMoves.isEmpty());


    }

    /**
     * A method to test toString() for Queen
     */
    @Test
    public void testToString() {
        assertTrue(wQueen.toString().equals("q"));
        assertTrue(bQueen.toString().equals("q"));
    }

}
