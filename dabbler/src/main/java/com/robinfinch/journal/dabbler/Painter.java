package com.robinfinch.journal.dabbler;

import com.robinfinch.journal.dabbler.brush.Brush;
import com.robinfinch.journal.dabbler.brush.Canvas;
import com.robinfinch.journal.dabbler.brush.Polygon;
import com.robinfinch.journal.dabbler.strokes.Stroke;

import java.util.List;

/**
 * Uses a {@link com.robinfinch.journal.dabbler.brush.Brush brush} to execute
 * {@link com.robinfinch.journal.dabbler.strokes.Stroke strokes}.
 *
 * @author Mark Hoogenboom
 */
public class Painter<COLOR, RECT, POLYGON extends Polygon> {

    protected final int canvasWidth;
    protected final int canvasHeight;

    public Painter(int canvasWidth, int canvasHeight) {
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
    }

    protected void paint(List<Stroke> strokes, Canvas<COLOR, RECT, POLYGON> canvas) {

        COLOR color = canvas.newColor(0xff, 0xff, 0xff, 0xff);

        RECT rect = canvas.newRect(0, 0, canvasWidth, canvasHeight);

        canvas.fillRect(color, rect);

        Brush<COLOR, RECT, POLYGON> brush = new Brush<>(canvas, canvasWidth, canvasHeight);

        for (Stroke stroke : strokes) {
            brush.execute(stroke);
        }
    }
}
