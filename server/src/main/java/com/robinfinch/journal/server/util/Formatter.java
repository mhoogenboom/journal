package com.robinfinch.journal.server.util;

/**
 * Collection of formatting methods.
 *
 * @author Mark Hoogenboom
 */
public class Formatter {

    public static final CharSequence formatDistance(int distance) {
        return String.format("%1.1f km", distance / 1000f);
    }

    public static final CharSequence formatTime(int time) {
        int s = time % 60;
        time /= 60;
        int m = time % 60;
        time /= 60;
        if (time > 0) {
            return String.format("%1$d:%2$02d:%3$02d", time, m, s);
        } else {
            return String.format("%1$d:%2$02d", m, s);
        }
    }

    public static final CharSequence formatTravelDescription(boolean away, String place) {
        if (away) {
            return "to " + place;
        } else {
            return "from " + place;
        }
    }
}
