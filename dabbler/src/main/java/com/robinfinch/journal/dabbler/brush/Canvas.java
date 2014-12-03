package com.robinfinch.journal.dabbler.brush;

/**
 *
 * @author Mark Hoogenboom
 */
public interface Canvas<COLOR, RECT, POLYGON> {

    COLOR newColor(int red, int green, int blue, int transparency);

    RECT newRect(int x, int y, int w, int h);

    void fillRect(COLOR color, RECT rect);

    POLYGON newPolygon();

    void fillPolygon(COLOR color, POLYGON polygon);
}
