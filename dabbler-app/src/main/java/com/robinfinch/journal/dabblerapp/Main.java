package com.robinfinch.journal.dabblerapp;

import com.robinfinch.journal.dabbler.strokes.Stroke;
import com.robinfinch.journal.dabbler.strokes.StrokeFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.robinfinch.journal.dabblerapp.Utils.LOG_TAG;

/**
 * Main class.
 *
 * @author Mark Hoogenboom
 */
public class Main {

    public static void main(String[] args) {

        Properties props = new Properties();
        try {
            props.load(Main.class.getResourceAsStream("/dabbler.properties"));
        } catch (IOException e) {
            Logger.getLogger(LOG_TAG).log(Level.WARNING, "Can't load properties", e);
        }

        int w = Integer.parseInt(props.getProperty("canvas.width"));
        int h = Integer.parseInt(props.getProperty("canvas.height"));
        File destDir = new File(props.getProperty("dest.dir"));

        long seed = System.currentTimeMillis();

        StrokeFactory factory = new StrokeFactory(seed);

        List<Stroke> strokes = new ArrayList<>();

        int i = 0;

        while (i++ < 45) {
            strokes.add(factory.createLongThinDarkRedStroke());
        }

        while (i++ < 60) {
            strokes.add(factory.createShortThickLightRedStroke());
        }

        new JavaPainter(w, h).paint(strokes, new File(destDir, "no_" + seed + ".png"));
    }
}
