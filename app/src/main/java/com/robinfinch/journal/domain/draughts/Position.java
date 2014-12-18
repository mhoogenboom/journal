package com.robinfinch.journal.domain.draughts;

import java.util.Arrays;

/**
 * Position in a game of draughts.
 *
 * @author Mark Hoogenboom
 */
public class Position {

    public static int EMPTY = 1;
    public static int WM = 2;
    public static int WK = 6;
    public static int BM = 0;
    public static int BK = 4;

    private final int[] squares;

    public Position(int[] squares) {
        this.squares = squares;
    }

    public int[] getSquares() {
        return squares;
    }

    @Override
    public String toString() {
        return "Position[squares=" + Arrays.toString(squares)
                + "]";
    }
}
