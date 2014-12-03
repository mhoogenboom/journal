package com.robinfinch.journal.dabbler.strokes;

import java.util.Random;

/**
 * A stroke defined by sequences.
 *
 * @author Mark Hoogenboom
 */
class SequencesStroke implements Stroke {

    private final Random random;
    private int numberOfSegments;
    private final com.robinfinch.journal.dabbler.sequences.rational.Sequence angle;
    private final com.robinfinch.journal.dabbler.sequences.rational.Sequence length;
    private final com.robinfinch.journal.dabbler.sequences.rational.Sequence width;
    private final com.robinfinch.journal.dabbler.sequences.integer.Sequence red;
    private final com.robinfinch.journal.dabbler.sequences.integer.Sequence green;
    private final com.robinfinch.journal.dabbler.sequences.integer.Sequence blue;

    private SequencesStroke(Builder builder) {

        random = new Random(builder.seed);

        this.numberOfSegments = builder.numberOfSegments;

        this.angle = builder.angle;
        this.angle.setRandom(random);

        this.length = builder.length;
        this.length.setRandom(random);

        this.width = builder.width;
        this.width.setRandom(random);

        this.red = builder.red;
        this.red.setRandom(random);

        this.green = builder.green;
        this.green.setRandom(random);

        this.blue = builder.blue;
        this.blue.setRandom(random);
    }

    @Override
    public double startX(double canvasWidth) {
        return random.nextDouble() * canvasWidth;
    }

    @Override
    public double startY(double canvasHeight) {
        return random.nextDouble() * canvasHeight;
    }

    @Override
    public double startAngle() {
        return angle.start();
    }

    @Override
    public double nextAngle(double a) {
        return angle.next(a);
    }

    @Override
    public double startLength() {
        return length.start();
    }

    @Override
    public double nextLength(double l) {
        return length.next(l);
    }

    @Override
    public double startWidth() {
        return width.start();
    }

    @Override
    public double nextWidth(double w) {
        return width.next(w);
    }

    @Override
    public int startRed() {
        return red.start();
    }

    @Override
    public int nextRed(int r) {
        return red.next(r);
    }

    @Override
    public int startGreen() {
        return green.start();
    }

    @Override
    public int nextGreen(int g) {
        return green.next(g);
    }

    @Override
    public int startBlue() {
        return blue.start();
    }

    @Override
    public int nextBlue(int b) {
        return blue.next(b);
    }

    @Override
    public boolean continues() {
        return numberOfSegments-- > 0;
    }
        
    @Override
    public String toString() {
        return "SequencesStroke[numberOfSegments=" + numberOfSegments
                + ";angle=" + angle
                + ";length=" + length
                + ";width=" + width
                + ";red=" + red
                + ";green=" + green
                + ";blue=" + blue
                + "]";
    }

    public static class Builder {

        private long seed;
        private int numberOfSegments;
        private com.robinfinch.journal.dabbler.sequences.rational.Sequence angle;
        private com.robinfinch.journal.dabbler.sequences.rational.Sequence length;
        private com.robinfinch.journal.dabbler.sequences.rational.Sequence width;
        private com.robinfinch.journal.dabbler.sequences.integer.Sequence red;
        private com.robinfinch.journal.dabbler.sequences.integer.Sequence green;
        private com.robinfinch.journal.dabbler.sequences.integer.Sequence blue;
        
        public Builder withSeed(long seed) {
            this.seed = seed;
            return this;
        }

        public Builder withNumberOfSegments(int numberOfSegments) {
            this.numberOfSegments = numberOfSegments;
            return this;
        }

        public Builder withAngle(com.robinfinch.journal.dabbler.sequences.rational.Sequence angle) {
            this.angle = angle;
            return this;
        }

        public Builder withLength(com.robinfinch.journal.dabbler.sequences.rational.Sequence length) {
            this.length = length;
            return this;
        }

        public Builder withWidth(com.robinfinch.journal.dabbler.sequences.rational.Sequence width) {
            this.width = width;
            return this;
        }

        public Builder withRed(com.robinfinch.journal.dabbler.sequences.integer.Sequence red) {
            this.red = red;
            return this;
        }

        public Builder withGreen(com.robinfinch.journal.dabbler.sequences.integer.Sequence green) {
            this.green = green;
            return this;
        }

        public Builder withBlue(com.robinfinch.journal.dabbler.sequences.integer.Sequence blue) {
            this.blue = blue;
            return this;
        }

        public SequencesStroke build() {
            return new SequencesStroke(this);
        }
    }
}
