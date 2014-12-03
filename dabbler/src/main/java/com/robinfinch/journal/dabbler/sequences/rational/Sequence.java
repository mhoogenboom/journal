package com.robinfinch.journal.dabbler.sequences.rational;

import java.util.Random;

/**
 * A sequence of rational numbers.
 *
 * @author Mark Hoogenboom
 */
public interface Sequence {

    void setRandom(Random random);

    double start();

    double next(double x);
}
