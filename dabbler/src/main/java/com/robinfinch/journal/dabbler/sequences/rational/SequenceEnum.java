package com.robinfinch.journal.dabbler.sequences.rational;

import java.util.Random;

/**
 * Base class for sequence enumerations.
 *
 * @author Mark Hoogenboom
 */
public abstract class SequenceEnum implements Sequence {

    private final Sequence delegate;

    public SequenceEnum(Sequence delegate) {
        assert (delegate != null);

        this.delegate = delegate;
    }

    @Override
    public void setRandom(Random random) {
        delegate.setRandom(random);
    }

    @Override
    public double start() {
        return delegate.start();
    }

    @Override
    public double next(double x) {
        return delegate.next(x);
    }
}
