package com.robinfinch.journal.domain.draughts;

import java.util.Arrays;

/**
 * Move in a game of draughts.
 *
 * @author Mark Hoogenboom
 */
public class Move {

    private final int[] path;

    public Move(int[] path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "Move[path=" + Arrays.toString(path)
                + "]";
    }
}
