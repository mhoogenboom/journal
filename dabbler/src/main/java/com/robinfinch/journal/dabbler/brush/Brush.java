package com.robinfinch.journal.dabbler.brush;

import com.robinfinch.journal.dabbler.strokes.Stroke;

/**
 * Draws the segments in a {@link com.robinfinch.journal.dabbler.strokes.Stroke stroke}.
 *
 * @author Mark Hoogenboom
 */
public class Brush<COLOR, RECT, POLYGON extends Polygon> {

    private final Canvas<COLOR, RECT, POLYGON> canvas;
    private final double canvasWidth;
    private final double canvasHeight;

    private double x;
    private double y;
    private double angle;
    private double length;
    private double width;
    private int red;
    private int green;
    private int blue;

    private Point p1;
    private Point p2;

    public Brush(Canvas<COLOR, RECT, POLYGON> canvas, double canvasWidth, double canvasHeight) {
        this.canvas = canvas;
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
    }

    public void execute(Stroke stroke) {
        x = stroke.startX(canvasWidth);
        y = stroke.startY(canvasHeight);
        angle = stroke.startAngle();
        length = stroke.startLength();
        width = stroke.startWidth();
        red = stroke.startRed();
        green = stroke.startGreen();
        blue = stroke.startBlue();

        p1 = null;
        p2 = null;

        while (stroke.continues() && executeSegment(stroke));
    }

    private boolean executeSegment(Stroke stroke) {
        double endX = x + length * Math.cos(angle);
        double endY = y + length * Math.sin(angle);

        if ((endX < 0) || (endX > canvasWidth) || (endY < 0) || (endY > canvasHeight)) {
            return false;
        }

        COLOR color = canvas.newColor(red, green, blue, 191);

        double w = width / 2;

        double dx = w * Math.cos(angle - Math.PI / 2);
        double dy = w * Math.sin(angle - Math.PI / 2);

        POLYGON polygon = canvas.newPolygon();
        polygon.addPoint((int) (x - dx), (int) (y - dy));
        if (p1 == null) {
            p1 = new Point();
        } else {
            polygon.addPoint(p1.x, p1.y);
        }
        if (p2 == null) {
            p2 = new Point();
        } else {
            polygon.addPoint(p2.x, p2.y);
        }
        polygon.addPoint((int) (x + dx), (int) (y + dy));

        p2.x = (int) (endX + dx);
        p2.y = (int) (endY + dy);
        polygon.addPoint(p2.x, p2.y);

        p1.x = (int) (endX - dx);
        p1.y = (int) (endY - dy);
        polygon.addPoint(p1.x, p1.y);

        canvas.fillPolygon(color, polygon);

        x = endX;
        y = endY;
        angle = stroke.nextAngle(angle);
        length = stroke.nextLength(length);
        width = stroke.nextWidth(width);
        red = stroke.nextRed(red);
        green = stroke.nextGreen(green);
        blue = stroke.nextBlue(blue);

        return true;
    }

    @Override
    public String toString() {
        return "Brush[x=" + x
                + ";y=" + y
                + ";angle=" + angle
                + ";length=" + length
                + ";width=" + width
                + ";red=" + red
                + ";green=" + green
                + ";blue=" + blue
                + "]";
    }
}
