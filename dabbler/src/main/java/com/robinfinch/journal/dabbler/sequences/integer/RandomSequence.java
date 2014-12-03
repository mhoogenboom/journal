package com.robinfinch.journal.dabbler.sequences.integer;

import java.util.Random;

/**
 * A sequence of integer values between min and max which is adjusted by
 * random delta between -step/2 and +step/2.
 *
 * @author Mark Hoogenboom
 */
public class RandomSequence implements Sequence {

    private final int min;
    private final int max;
    private final int step;
    private Random random;

    public RandomSequence(int min, int max, int step) {
        assert (min < max) && (step > 0);

        this.min = min;
        this.max = max;
        this.step = step;
    }

    @Override
    public void setRandom(Random random) {
        this.random = random;
    }

    @Override
    public int start() {
        return min + random.nextInt(max - min + 1);
    }

    @Override
    public int next(int x) {
        return clamp(x + delta());
    }

    private int clamp(int x) {
        if (x < min) {
            return min;
        }
        if (x > max) {
            return max;
        }
        return x;
    }

    private int delta() {
        return random.nextInt(step + 1) - step / 2;
    }

    @Override
    public String toString() {
        return "RandomSequence[min=" + min
                + ";max=" + max
                + ";step=" + step
                + "]";
    }
}
