package com.robinfinch.journal.dabblerapp;

import com.robinfinch.journal.dabbler.brush.Canvas;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 * Implementation of {@link com.robinfinch.journal.dabbler.brush.Canvas canvas} for Java SE.
 *
 * @author Mark Hoogenboom
 */
public class JavaCanvas implements Canvas<Color, Rectangle, JavaPolygon> {

    private final Graphics2D canvas;

    public JavaCanvas(BufferedImage image) {
        canvas = image.createGraphics();
        canvas.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    @Override
    public Color newColor(int red, int green, int blue, int transparency) {
        return new Color(red, green, blue, transparency);
    }

    @Override
    public Rectangle newRect(int x, int y, int w, int h) {
        return new Rectangle(x, y, w, h);
    }

    @Override
    public void fillRect(Color color, Rectangle rect) {
        canvas.setColor(color);
        canvas.fillRect(rect.x, rect.y, rect.width, rect.height);
    }

    @Override
    public JavaPolygon newPolygon() {
        return new JavaPolygon();
    }

    @Override
    public void fillPolygon(Color color, JavaPolygon polygon) {
        Polygon p = polygon.getNativePolygon();
        canvas.setColor(color);
        canvas.fillPolygon(p);
    }
}
