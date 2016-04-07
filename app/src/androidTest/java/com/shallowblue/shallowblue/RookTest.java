package com.shallowblue.shallowblue;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;

/**
 * This class implements junit tests for Rook.java
 * Created by Mohammad on 4/7/16.
 */
public class RookTest {

    Rook rook0 = new Rook(new Position(0,7), Color.WHITE);
    Rook rook1 = new Rook(new Position(3,3), Color.BLACK);
    Rook rook2 = new Rook(new Position(0,-15), Color.WHITE);

    /**
     * A method to test hasMoved() for Rook.
     * It is set to UNUSED, since we only need it for Castling and Promotion.
     * Therefore, it should always return false.
     */
    @Test
    public void testHasMoved() {
        assertFalse(rook0.hasMoved());
        assertFalse(rook1.hasMoved());
        assertFalse(rook2.hasMoved());
    }

    /**
     * A method to test isPromoting() for Rook.
     * It is set to UNUSED, since we only need it for Promotion
     * (see Pawn.java for more info)
     * Therefore, it should always return false.
     */
    @Test
    public void isPromoting() {
        assertFalse(rook0.isPromoting());
        assertFalse(rook1.isPromoting());
        assertFalse(rook2.isPromoting());
    }


    /**
     * This is a method to test possible moves for Rook.
     * Expected output:
     *
     *  rook0 (0,7):
     *      This is an original white right rook (H1), so its moves are:
     *      A1, B1, C1, D1, E1, F1, G1  (entire row)
     *      H1, H2, H3, H4, H5, H6, H7  (entire column)
     *
     *      {
     *          (0,0), (0,1), (0,2), (0,3), (0,4), (0,5), (0,6),
     *          (1,7), (2,7), (3,7), (4,7), (5,7), (6,7), (7,7),
     *      }
     *      See: http://i.imgur.com/hzNdPCJ.jpg
     *
     *
     *  rook1 (3,3):
     *      This rook is in D4, so its moves are:
     *      A4, B4, C4, E4, F4, G4, H4  (entire row)
     *      D0, D1, D2, D3, D5, D6, D7  (entire column)
     *
     *      {
     *          (3,0), (3,1), (3,2), (3,4), (3,5), (3,6), (3,7),
     *          (0,3), (1,3), (2,3), (4,3), (5,3), (6,3), (7,3),
     *      }
     *      See: http://i.imgur.com/YFSH05K.jpg
     *
     *
     *  rook2 (0,-15):
     *      Since this rook is out of bounds, it should have no moves.
     *
     */
    @Test
    public void testPossibleMoves() {
        ArrayList<Position> possibleMoves;

        //1- rook0 (0,7):
        possibleMoves = rook0.possibleMoves();

        assertTrue(possibleMoves.contains(new Position(0,0)));
        assertTrue(possibleMoves.contains(new Position(0,1)));
        assertTrue(possibleMoves.contains(new Position(0,2)));
        assertTrue(possibleMoves.contains(new Position(0,3)));
        assertTrue(possibleMoves.contains(new Position(0,4)));
        assertTrue(possibleMoves.contains(new Position(0,5)));
        assertTrue(possibleMoves.contains(new Position(0,6)));

        assertTrue(possibleMoves.contains(new Position(1,7)));
        assertTrue(possibleMoves.contains(new Position(2,7)));
        assertTrue(possibleMoves.contains(new Position(3,7)));
        assertTrue(possibleMoves.contains(new Position(4,7)));
        assertTrue(possibleMoves.contains(new Position(5,7)));
        assertTrue(possibleMoves.contains(new Position(6,7)));
        assertTrue(possibleMoves.contains(new Position(7,7)));


        //2- rook1 (3,3):
        possibleMoves = rook1.possibleMoves();

        assertTrue(possibleMoves.contains(new Position(3,0)));
        assertTrue(possibleMoves.contains(new Position(3,1)));
        assertTrue(possibleMoves.contains(new Position(3,2)));
        assertTrue(possibleMoves.contains(new Position(3,4)));
        assertTrue(possibleMoves.contains(new Position(3,5)));
        assertTrue(possibleMoves.contains(new Position(3,6)));
        assertTrue(possibleMoves.contains(new Position(3,7)));

        assertTrue(possibleMoves.contains(new Position(0,3)));
        assertTrue(possibleMoves.contains(new Position(1,3)));
        assertTrue(possibleMoves.contains(new Position(2,3)));
        assertTrue(possibleMoves.contains(new Position(4,3)));
        assertTrue(possibleMoves.contains(new Position(5,3)));
        assertTrue(possibleMoves.contains(new Position(6,3)));
        assertTrue(possibleMoves.contains(new Position(7,3)));



        //3- rook2 (0,-15):
        possibleMoves = rook2.possibleMoves();
        assertTrue(possibleMoves.isEmpty());


    }


    /**
     * A method to test toString() for Rook
     */
    @Test
    public void testToString() {
        assertTrue(rook0.toString().equals("r"));
        assertTrue(rook1.toString().equals("r"));
        assertTrue(rook2.toString().equals("r"));

    }


}
