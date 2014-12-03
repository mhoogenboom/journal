package com.robinfinch.journal.dabbler.sequences.rational;

import java.util.Random;

/**
 * A constant sequence of rational numbers.
 *
 * @author Mark Hoogenboom
 */
public class ConstantSequence implements Sequence {

    private final double c;

    public ConstantSequence(double c) {
        this.c = c;
    }

    @Override
    public void setRandom(Random random) {
    }

    @Override
    public double start() {
        return c;
    }

    @Override
    public double next(double x) {
        return c;
    }

    @Override
    public String toString() {
        return "ConstantSequence[c=" + c
                + "]";
    }
}
