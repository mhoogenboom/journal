package com.robinfinch.journal.dabbler.sequences.integer;

import java.util.Random;

/**
 * A constant sequence of integers.
 *
 * @author Mark Hoogenboom
 */
public class ConstantSequence implements Sequence {

    private final int c;

    public ConstantSequence(int c) {
        this.c = c;
    }

    @Override
    public void setRandom(Random random) {
    }

    @Override
    public int start() {
        return c;
    }

    @Override
    public int next(int x) {
        return c;
    }

    @Override
    public String toString() {
        return "ConstantSequence[c=" + c
                + "]";
    }
}
