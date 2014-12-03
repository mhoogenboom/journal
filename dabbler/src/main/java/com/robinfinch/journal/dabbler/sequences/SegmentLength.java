package com.robinfinch.journal.dabbler.sequences;

import com.robinfinch.journal.dabbler.sequences.rational.RandomSequence;
import com.robinfinch.journal.dabbler.sequences.rational.Sequence;

/**
 * Segment length constants.
 *
 * @author Mark Hoogenboom
 */
public interface SegmentLength {

    Sequence LIMP = new RandomSequence(3.0, 15.0, 3.0);

    Sequence NORMAL_LENGTH = new RandomSequence(6.0, 30.0, 6.0);

    Sequence STIFF = new RandomSequence(12.0, 60.0, 12.0);
}
