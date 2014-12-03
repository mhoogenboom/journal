package com.robinfinch.journal.dabbler.sequences.rational;

import java.util.Random;

/**
 * A sequence of rational values between min and max which is adjusted by
 * random delta between -step/2 and +step/2.
 *
 * @author Mark Hoogenboom
 */
public class RandomSequence implements Sequence {

    private final double min;
    private final double max;
    private final double step;
    private Random random;

    public RandomSequence(double min, double max, double step) {
        assert (min < max) && (step > 0.0);

        this.min = min;
        this.max = max;
        this.step = step;
    }

    @Override
    public void setRandom(Random random) {
        this.random = random;
    }

    @Override
    public double start() {
        return min + random.nextDouble() * (max - min);
    }

    @Override
    public double next(double x) {
        return clamp(x + delta());
    }

    private double clamp(double x) {
        if (x < min) {
            return min;
        }
        if (x > max) {
            return max;
        }
        return x;
    }

    private double delta() {
        return (random.nextDouble() - 0.5) * step;
    }

    @Override
    public String toString() {
        return "RandomSequence[min=" + min
                + ";max=" + max
                + ";step=" + step
                + "]";
    }
}
