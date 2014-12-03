package com.robinfinch.journal.app.dabbler;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import com.robinfinch.journal.dabbler.Painter;
import com.robinfinch.journal.dabbler.strokes.Stroke;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static com.robinfinch.journal.app.util.Constants.LOG_TAG;

/**
 * Implementation of {@link com.robinfinch.journal.dabbler.Painter painter} for Android.
 *
 * @author Mark Hoogenboom
 */
public class AndroidPainter extends Painter<Paint, Rect, AndroidPolygon> {

    public AndroidPainter(int canvasWidth, int canvasHeight) {
        super(canvasWidth, canvasHeight);
    }

    public void paint(List<Stroke> strokes, File out) {

        Bitmap bitmap = Bitmap.createBitmap(canvasWidth, canvasHeight, Bitmap.Config.ARGB_8888);
        paint(strokes, new AndroidCanvas(bitmap));

        try {
            FileOutputStream os = new FileOutputStream(out);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);

            Log.i(LOG_TAG, "Saved image " + out.getAbsolutePath());
        } catch (IOException e) {
            Log.e(LOG_TAG, "Can't save image", e);
        }
    }
}
