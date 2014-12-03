package com.robinfinch.journal.dabblerapp;

import com.robinfinch.journal.dabbler.brush.Polygon;

/**
 * Implementation of {@link com.robinfinch.journal.dabbler.brush.Polygon polygon} for Java SE.
 *
 * @author Mark Hoogenboom
 */
public class JavaPolygon implements Polygon {

    private final java.awt.Polygon polygon;

    public JavaPolygon() {
        polygon = new java.awt.Polygon();
    }

    @Override
    public void addPoint(int x, int y) {
        polygon.addPoint(x, y);
    }

    java.awt.Polygon getNativePolygon() {
        return polygon;
    }
}
