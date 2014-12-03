package com.robinfinch.journal.dabbler.sequences;

import com.robinfinch.journal.dabbler.sequences.rational.RandomSequence;
import com.robinfinch.journal.dabbler.sequences.rational.Sequence;

/**
 * Segment width constants.
 *
 * @author Mark Hoogenboom
 */
public interface SegmentWidth {

    Sequence THIN = new RandomSequence(0.0, 10.0, 2.0);

    Sequence NORMAL_WIDTH = new RandomSequence(0.0, 20.0, 4.0);

    Sequence THICK = new RandomSequence(0.0, 60.0, 10.0);
}
