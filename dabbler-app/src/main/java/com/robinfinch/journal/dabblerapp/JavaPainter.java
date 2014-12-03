package com.robinfinch.journal.dabblerapp;

import com.robinfinch.journal.dabbler.Painter;
import com.robinfinch.journal.dabbler.strokes.Stroke;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import static com.robinfinch.journal.dabblerapp.Utils.LOG_TAG;

/**
 * Implementation of {@link com.robinfinch.journal.dabbler.Painter painter} for Java SE.
 *
 * @author Mark Hoogenboom
 */
public class JavaPainter extends Painter<Color, Rectangle, JavaPolygon> {

    public JavaPainter(int canvasWidth, int canvasHeight) {
        super(canvasWidth, canvasHeight);
    }

    public void paint(List<Stroke> strokes, File out) {

        BufferedImage im = new BufferedImage(canvasWidth, canvasHeight, BufferedImage.TYPE_INT_ARGB);
        paint(strokes, new JavaCanvas(im));

        try {
            ImageIO.write(im, "png", out);

            Logger.getLogger(LOG_TAG).log(Level.INFO, "Saved image " + out.getAbsolutePath());
        } catch (IOException e) {
            Logger.getLogger(LOG_TAG).log(Level.WARNING, "Can't save image", e);
        }
    }
}
