package com.robinfinch.journal.app.dabbler;

import com.robinfinch.journal.dabbler.brush.Polygon;

/**
 * Implementation of {@link com.robinfinch.journal.dabbler.brush.Polygon polygon} for Android.
 *
 * @author Mark Hoogenboom
 */
public class AndroidPolygon implements Polygon {

    private android.graphics.Path polygon;

    public AndroidPolygon() {
        polygon = new android.graphics.Path();
    }

    @Override
    public void addPoint(int x, int y) {
        if (polygon.isEmpty()) {
            polygon.moveTo(x, y);
        } else {
            polygon.lineTo(x, y);
        }
    }

    android.graphics.Path getNativePolygon() {
        return polygon;
    }
}
