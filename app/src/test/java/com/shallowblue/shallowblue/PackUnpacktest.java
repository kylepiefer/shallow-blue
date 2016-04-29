package com.shallowblue.shallowblue;
import junit.framework.Assert;

import org.junit.Test;

import java.util.ArrayList;

import dalvik.annotation.TestTargetClass;

/**
 * Created by peter on 4/27/2016.
 */
public class PackUnpacktest {
    @Test

    public void oneMove(){
        GameBoard board1 = new GameBoard();

        board1.move(board1.getAllLegalMoves().get(1));
        board1.move(board1.getAllLegalMoves().get(5));
        board1.move(board1.getAllLegalMoves().get(1));
        board1.move(board1.getAllLegalMoves().get(4));
        board1.move(board1.getAllLegalMoves().get(2));
        board1.move(board1.getAllLegalMoves().get(1));
        board1.move(board1.getAllLegalMoves().get(4));
        board1.move(board1.getAllLegalMoves().get(2));
        board1.move(board1.getAllLegalMoves().get(1));
        board1.move(board1.getAllLegalMoves().get(5));
        board1.move(board1.getAllLegalMoves().get(2));
        board1.move(board1.getAllLegalMoves().get(1));
        board1.move(board1.getAllLegalMoves().get(1));
        board1.move(board1.getAllLegalMoves().get(4));
        board1.move(board1.getAllLegalMoves().get(1));
        board1.move(board1.getAllLegalMoves().get(3));
        board1.move(board1.getAllLegalMoves().get(5));
        board1.move(board1.getAllLegalMoves().get(4));
        board1.move(board1.getAllLegalMoves().get(1));
        board1.move(board1.getAllLegalMoves().get(2));
        board1.move(board1.getAllLegalMoves().get(4));
        board1.move(board1.getAllLegalMoves().get(2));
        board1.move(board1.getAllLegalMoves().get(2));
        board1.move(board1.getAllLegalMoves().get(1));
        board1.move(board1.getAllLegalMoves().get(4));
        board1.move(board1.getAllLegalMoves().get(5));
        board1.move(board1.getAllLegalMoves().get(1));
        board1.move(board1.getAllLegalMoves().get(1));
        board1.move(board1.getAllLegalMoves().get(2));
        board1.move(board1.getAllLegalMoves().get(1));
        board1.move(board1.getAllLegalMoves().get(2));
        board1.move(board1.getAllLegalMoves().get(5));
        board1.move(board1.getAllLegalMoves().get(1));
        board1.move(board1.getAllLegalMoves().get(3));
        board1.move(board1.getAllLegalMoves().get(2));
        board1.move(board1.getAllLegalMoves().get(5));
        board1.move(board1.getAllLegalMoves().get(1));
        board1.move(board1.getAllLegalMoves().get(2));
        board1.move(board1.getAllLegalMoves().get(1));
        board1.move(board1.getAllLegalMoves().get(2));
        board1.move(board1.getAllLegalMoves().get(2));
        board1.move(board1.getAllLegalMoves().get(1));
        board1.move(board1.getAllLegalMoves().get(3));
        board1.move(board1.getAllLegalMoves().get(2));
        board1.move(board1.getAllLegalMoves().get(1));
        board1.move(board1.getAllLegalMoves().get(1));
        board1.move(board1.getAllLegalMoves().get(2));
        board1.move(board1.getAllLegalMoves().get(1));
        board1.move(board1.getAllLegalMoves().get(2));
        board1.move(board1.getAllLegalMoves().get(2));
        board1.move(board1.getAllLegalMoves().get(1));
        board1.move(board1.getAllLegalMoves().get(3));
        board1.move(board1.getAllLegalMoves().get(5));
        board1.move(board1.getAllLegalMoves().get(1));
        board1.move(board1.getAllLegalMoves().get(1));
        board1.move(board1.getAllLegalMoves().get(2));
        board1.move(board1.getAllLegalMoves().get(1));
        board1.move(board1.getAllLegalMoves().get(4));
        board1.move(board1.getAllLegalMoves().get(2));
        board1.move(board1.getAllLegalMoves().get(1));
        board1.move(board1.getAllLegalMoves().get(4));
        board1.move(board1.getAllLegalMoves().get(2));
        board1.move(board1.getAllLegalMoves().get(1));
        board1.move(board1.getAllLegalMoves().get(1));
        board1.move(board1.getAllLegalMoves().get(2));
        board1.move(board1.getAllLegalMoves().get(1));
        board1.move(board1.getAllLegalMoves().get(2));
        board1.move(board1.getAllLegalMoves().get(4));
        board1.move(board1.getAllLegalMoves().get(1));
        board1.move(board1.getAllLegalMoves().get(3));
        board1.move(board1.getAllLegalMoves().get(2));
        board1.move(board1.getAllLegalMoves().get(4));
        board1.move(board1.getAllLegalMoves().get(1));
        board1.move(board1.getAllLegalMoves().get(2));
        board1.move(board1.getAllLegalMoves().get(4));
        board1.move(board1.getAllLegalMoves().get(2));
        board1.move(board1.getAllLegalMoves().get(2));
        board1.move(board1.getAllLegalMoves().get(1));
        board1.move(board1.getAllLegalMoves().get(4));
        board1.move(board1.getAllLegalMoves().get(2));
        board1.move(board1.getAllLegalMoves().get(1));
        board1.move(board1.getAllLegalMoves().get(1));
        board1.move(board1.getAllLegalMoves().get(2));
        board1.move(board1.getAllLegalMoves().get(1));
        board1.move(board1.getAllLegalMoves().get(2));
        board1.move(board1.getAllLegalMoves().get(2));
        board1.move(board1.getAllLegalMoves().get(1));
        board1.move(board1.getAllLegalMoves().get(3));
        board1.move(board1.getAllLegalMoves().get(5));
        board1.move(board1.getAllLegalMoves().get(2));
        board1.move(board1.getAllLegalMoves().get(1));
        board1.move(board1.getAllLegalMoves().get(1));
        board1.move(board1.getAllLegalMoves().get(4));
        board1.move(board1.getAllLegalMoves().get(4));
        board1.move(board1.getAllLegalMoves().get(1));
        board1.move(board1.getAllLegalMoves().get(3));
        board1.move(board1.getAllLegalMoves().get(2));
        board1.move(board1.getAllLegalMoves().get(4));
        board1.move(board1.getAllLegalMoves().get(1));
        board1.move(board1.getAllLegalMoves().get(2));
        board1.move(board1.getAllLegalMoves().get(4));
        board1.move(board1.getAllLegalMoves().get(2));
        board1.move(board1.getAllLegalMoves().get(2));
        board1.move(board1.getAllLegalMoves().get(1));
        board1.move(board1.getAllLegalMoves().get(4));
        board1.move(board1.getAllLegalMoves().get(2));
        board1.move(board1.getAllLegalMoves().get(1));
        board1.move(board1.getAllLegalMoves().get(1));
        board1.move(board1.getAllLegalMoves().get(3));
        board1.move(board1.getAllLegalMoves().get(5));
        board1.move(board1.getAllLegalMoves().get(4));
        board1.move(board1.getAllLegalMoves().get(1));
        board1.move(board1.getAllLegalMoves().get(2));
        board1.move(board1.getAllLegalMoves().get(4));
        board1.move(board1.getAllLegalMoves().get(2));
        board1.move(board1.getAllLegalMoves().get(2));
        board1.move(board1.getAllLegalMoves().get(1));
        board1.move(board1.getAllLegalMoves().get(4));
        board1.move(board1.getAllLegalMoves().get(5));
        board1.move(board1.getAllLegalMoves().get(1));
        board1.move(board1.getAllLegalMoves().get(1));
        board1.move(board1.getAllLegalMoves().get(2));
        board1.move(board1.getAllLegalMoves().get(1));
        board1.move(board1.getAllLegalMoves().get(2));
        board1.move(board1.getAllLegalMoves().get(1));
        board1.move(board1.getAllLegalMoves().get(1));
        board1.move(board1.getAllLegalMoves().get(2));
        board1.move(board1.getAllLegalMoves().get(1));
        board1.move(board1.getAllLegalMoves().get(2));
        board1.move(board1.getAllLegalMoves().get(2));
        board1.move(board1.getAllLegalMoves().get(1));
        board1.move(board1.getAllLegalMoves().get(3));
        board1.move(board1.getAllLegalMoves().get(2));
        board1.move(board1.getAllLegalMoves().get(1));
        board1.move(board1.getAllLegalMoves().get(1));
        board1.move(board1.getAllLegalMoves().get(2));
        board1.move(board1.getAllLegalMoves().get(1));
        board1.move(board1.getAllLegalMoves().get(2));
        board1.move(board1.getAllLegalMoves().get(2));
        board1.move(board1.getAllLegalMoves().get(1));
        board1.move(board1.getAllLegalMoves().get(3));
        board1.move(board1.getAllLegalMoves().get(2));
        board1.move(board1.getAllLegalMoves().get(1));
        String packedString = board1.pack();
      
        GameBoard board2 = new GameBoard(packedString);
        String packedString2 = board2.pack();

        Assert.assertEquals(packedString, packedString2);

    }

    public void twoMove(){
        GameBoard board1 = new GameBoard();
        board1.move(board1.getAllLegalMoves().get(1));
        board1.move(board1.getAllLegalMoves().get(1));
        String packedString = board1.pack();
        GameBoard board2 = new GameBoard(packedString);
        Assert.assertEquals(packedString, board2.pack());

    }

}
