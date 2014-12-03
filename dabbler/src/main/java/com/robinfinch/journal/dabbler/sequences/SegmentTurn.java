package com.robinfinch.journal.dabbler.sequences;

import com.robinfinch.journal.dabbler.sequences.rational.RandomSequence;
import com.robinfinch.journal.dabbler.sequences.rational.Sequence;

/**
 * Segment turn constants.
 *
 * @author Mark Hoogenboom
 */
public interface SegmentTurn {

    Sequence STANDARD = new RandomSequence(-Math.PI, Math.PI, Math.PI / 3);

    Sequence BLOCKY = new RandomSequence(-Math.PI, Math.PI, Math.PI);
}
