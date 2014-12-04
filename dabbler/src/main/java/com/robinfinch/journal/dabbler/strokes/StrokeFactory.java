package com.robinfinch.journal.dabbler.strokes;

import com.robinfinch.journal.dabbler.sequences.SegmentColour;
import com.robinfinch.journal.dabbler.sequences.SegmentLength;
import com.robinfinch.journal.dabbler.sequences.SegmentNumber;
import com.robinfinch.journal.dabbler.sequences.SegmentTurn;
import com.robinfinch.journal.dabbler.sequences.SegmentWidth;

/**
 * Creates strokes in terms of short) thick) light) etc.
 *
 * @author Mark Hoogenboom
 */
public class StrokeFactory
        implements SegmentNumber, SegmentTurn, SegmentLength, SegmentWidth, SegmentColour {

    private long seed;

    public StrokeFactory(long initialSeed) {
        this.seed = initialSeed;
    }

    private long seed() {
        seed += 1000;
        return seed;
    }

    public Stroke createBasicStroke() {
        return new SequencesStroke.Builder()
                .withSeed(seed())
                .withNumberOfSegments(REGULAR)
                .withAngle(STANDARD)
                .withLength(NORMAL_LENGTH)
                .withWidth(NORMAL_WIDTH)
                .withRed(FULL)
                .withGreen(FULL)
                .withBlue(FULL)
                .build();
    }

    public Stroke createBlackStroke() {
        return new SequencesStroke.Builder()
                .withSeed(seed())
                .withNumberOfSegments(REGULAR)
                .withAngle(STANDARD)
                .withLength(NORMAL_LENGTH)
                .withWidth(NORMAL_WIDTH)
                .withRed(C0)
                .withGreen(C0)
                .withBlue(C0)
                .build();
    }

    public Stroke createLongThinDarkStroke() {
        return new SequencesStroke.Builder()
                .withSeed(seed())
                .withNumberOfSegments(LONG)
                .withAngle(STANDARD)
                .withLength(NORMAL_LENGTH)
                .withWidth(THIN)
                .withRed(DARK)
                .withGreen(DARK)
                .withBlue(DARK)
                .build();
    }

    public Stroke createLongThinDarkRedStroke() {
        return new SequencesStroke.Builder()
                .withSeed(seed())
                .withNumberOfSegments(LONG)
                .withAngle(STANDARD)
                .withLength(NORMAL_LENGTH)
                .withWidth(THIN)
                .withRed(DARK)
                .withGreen(C0)
                .withBlue(C0)
                .build();
    }

    public Stroke createLongThinDarkGreenStroke() {
        return new SequencesStroke.Builder()
                .withSeed(seed())
                .withNumberOfSegments(LONG)
                .withAngle(STANDARD)
                .withLength(NORMAL_LENGTH)
                .withWidth(THIN)
                .withRed(C0)
                .withGreen(DARK)
                .withBlue(C0)
                .build();
    }

    public Stroke createLongThinDarkBlueStroke() {
        return new SequencesStroke.Builder()
                .withSeed(seed())
                .withNumberOfSegments(LONG)
                .withAngle(STANDARD)
                .withLength(NORMAL_LENGTH)
                .withWidth(THIN)
                .withRed(C0)
                .withGreen(C0)
                .withBlue(DARK)
                .build();
    }

    public Stroke createShortThickLightStroke() {
        return new SequencesStroke.Builder()
                .withSeed(seed())
                .withNumberOfSegments(SHORT)
                .withAngle(STANDARD)
                .withLength(NORMAL_LENGTH)
                .withWidth(THICK)
                .withRed(LIGHT)
                .withGreen(LIGHT)
                .withBlue(LIGHT)
                .build();
    }

    public Stroke createShortThickLightRedStroke() {
        return new SequencesStroke.Builder()
                .withSeed(seed())
                .withNumberOfSegments(SHORT)
                .withAngle(STANDARD)
                .withLength(NORMAL_LENGTH)
                .withWidth(THICK)
                .withRed(LIGHT)
                .withGreen(C0)
                .withBlue(C0)
                .build();
    }

    public Stroke createLimpStroke() {
        return new SequencesStroke.Builder()
                .withSeed(seed())
                .withNumberOfSegments(REGULAR)
                .withAngle(STANDARD)
                .withLength(LIMP)
                .withWidth(NORMAL_WIDTH)
                .withRed(FULL)
                .withGreen(FULL)
                .withBlue(FULL)
                .build();
    }

    public Stroke createStiffStroke() {
        return new SequencesStroke.Builder()
                .withSeed(seed())
                .withNumberOfSegments(REGULAR)
                .withAngle(STANDARD)
                .withLength(STIFF)
                .withWidth(NORMAL_WIDTH)
                .withRed(FULL)
                .withGreen(FULL)
                .withBlue(FULL)
                .build();
    }

    public Stroke createBlockyStroke() {
        return new SequencesStroke.Builder()
                .withSeed(seed())
                .withNumberOfSegments(REGULAR)
                .withAngle(BLOCKY)
                .withLength(STIFF)
                .withWidth(NORMAL_WIDTH)
                .withRed(FULL)
                .withGreen(FULL)
                .withBlue(FULL)
                .build();
    }
}
