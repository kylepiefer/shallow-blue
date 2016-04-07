package com.shallowblue.shallowblue;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;


/**
 * This class implements junit testing on Bishop.java
 * Created by Mohammad on 4/7/16.
 */
public class BishopTest {

    Bishop bishop0 = new Bishop(new Position(0,0),Color.WHITE);
    Bishop bishop1 = new Bishop(new Position(9,10),Color.BLACK);
    Bishop bishop2 = new Bishop(new Position(-3,-5),Color.WHITE);


    /**
     * A method to test hasMoved() for Bishop.
     * It is set to UNUSED, since we only need it for Castling and Promotion.
     * Therefore, it should always return false.
     */
    @Test
    public void testHasMoved() {
        assertFalse(bishop0.hasMoved());
        assertFalse(bishop1.hasMoved());
        assertFalse(bishop2.hasMoved());
    }

    /**
     * A method to test isPromoting() for Bishop.
     * It is set to UNUSED, since we only need it for Promotion
     * (see Pawn.java for more info)
     * Therefore, it should always return false.
     */
    @Test
    public void isPromoting() {
        assertFalse(bishop0.isPromoting());
        assertFalse(bishop1.isPromoting());
        assertFalse(bishop2.isPromoting());

    }

    /**
     * This is a method to test possibleMoves() for Bishop.
     *
     * Expected output:
     *  bishop0 (0,0):
     *      This bishop's position translates to A1.
     *      Therefore, possibleMoves should return the diagonal moves leading to H8:
     *      { (1,1), (2,2), (3,3), (4,4), (5,5), (6,6), (7,7) }
     *      See: http://i.imgur.com/PcNxPQH.jpg
     *
     *  bishop1 (9,10):
     *      This bishop is out of bounds, so its possibleMoves should be empty.
     *
     *  bishop2 (-3,-5):
     *      This bishop is illegal! its possibleMoves should also be empty.
     *
     *
     */
    @Test
    public void testPossibleMoves() {
        ArrayList<Position> possibleMoves;


        //1- bishop0 (0,0):
        possibleMoves = bishop0.possibleMoves();

        assertTrue(possibleMoves.contains(new Position(1,1)));
        assertTrue(possibleMoves.contains(new Position(2,2)));
        assertTrue(possibleMoves.contains(new Position(3,3)));
        assertTrue(possibleMoves.contains(new Position(4,4)));
        assertTrue(possibleMoves.contains(new Position(5,5)));
        assertTrue(possibleMoves.contains(new Position(6,6)));
        assertTrue(possibleMoves.contains(new Position(7,7)));


        //2- bishop1 (9,10):
        possibleMoves = bishop1.possibleMoves();
        assertTrue(possibleMoves.isEmpty());


        //3- bishop2 (-3,-5):
        possibleMoves = bishop2.possibleMoves();
        assertTrue(possibleMoves.isEmpty());

    }

    /**
     * A method to test toString() for Bishop
     */
    @Test
    public void testToString() {
        assertTrue(bishop0.toString().equals("b"));
        assertTrue(bishop1.toString().equals("b"));
        assertTrue(bishop2.toString().equals("b"));

    }



}
