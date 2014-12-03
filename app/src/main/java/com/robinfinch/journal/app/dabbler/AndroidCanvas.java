package com.robinfinch.journal.app.dabbler;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;

import com.robinfinch.journal.dabbler.brush.Canvas;

/**
 * Implementation of {@link com.robinfinch.journal.dabbler.brush.Canvas canvas} for Android.
 *
 * @author Mark Hoogenboom
 */
public class AndroidCanvas implements Canvas<Paint, Rect, AndroidPolygon> {

    private final android.graphics.Canvas canvas;

    public AndroidCanvas(Bitmap bitmap) {
        this.canvas = new android.graphics.Canvas(bitmap);
    }

    @Override
    public Paint newColor(int red, int green, int blue, int transparency) {
        Paint paint = new Paint();
        paint.setARGB(transparency, red, green, blue);
        paint.setStyle(Paint.Style.FILL);
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        return paint;
    }

    @Override
    public Rect newRect(int x, int y, int w, int h) {
        return new Rect(x, y, w, h);
    }

    @Override
    public void fillRect(Paint color, Rect rect) {
        canvas.drawRect(rect, color);
    }

    @Override
    public AndroidPolygon newPolygon() {
        return new AndroidPolygon();
    }

    @Override
    public void fillPolygon(Paint color, AndroidPolygon polygon) {
        Path p = polygon.getNativePolygon();
        canvas.drawPath(p, color);
    }
}
