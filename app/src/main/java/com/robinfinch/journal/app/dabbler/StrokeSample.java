package com.robinfinch.journal.app.dabbler;

import com.robinfinch.journal.dabbler.strokes.Stroke;

/**
 * Sample of a {@link com.robinfinch.journal.dabbler.strokes.Stroke}.
 *
 * @author Mark Hoogenboom
 */
public class StrokeSample {

    private final Stroke stroke;
    private int count;
    private final int sampleResId;

    public StrokeSample(Stroke stroke, int sampleResId) {
        this.stroke = stroke;
        this.sampleResId = sampleResId;
    }

    public Stroke getStroke() {
        return stroke;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getSampleResId() {
        return sampleResId;
    }
}
