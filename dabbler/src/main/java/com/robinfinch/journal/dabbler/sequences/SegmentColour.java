package com.robinfinch.journal.dabbler.sequences;

import com.robinfinch.journal.dabbler.sequences.integer.ConstantSequence;
import com.robinfinch.journal.dabbler.sequences.integer.RandomSequence;
import com.robinfinch.journal.dabbler.sequences.integer.Sequence;

/**
 * Segment colour constants.
 *
 * @author Mark Hoogenboom
 */
public interface SegmentColour {

    Sequence C0 = new ConstantSequence(0);

    Sequence DARK = new RandomSequence(0, 63, 8);

    Sequence FULL = new RandomSequence(0, 255, 16);

    Sequence LIGHT = new RandomSequence(192, 255, 8);
}
