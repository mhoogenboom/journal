package com.robinfinch.journal.dabbler.strokes;

/**
 * A sequence of segments.
 *
 * @author Mark Hoogenboom
 */
public interface Stroke {

    double startX(double canvasWidth);

    double startY(double canvasHeight);

    double startAngle();

    double nextAngle(double angle);

    double startLength();

    double nextLength(double length);

    double startWidth();

    double nextWidth(double width);

    int startRed();

    int nextRed(int red);

    int startGreen();

    int nextGreen(int green);

    int startBlue();

    int nextBlue(int blue);

    boolean continues();
}
