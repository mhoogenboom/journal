package com.robinfinch.journal.dabbler.sequences.integer;

import java.util.Random;

/**
 * A sequence of integers.
 *
 * @author Mark Hoogenboom
 */
public interface Sequence {

    void setRandom(Random random);

    int start();

    int next(int x);
}
