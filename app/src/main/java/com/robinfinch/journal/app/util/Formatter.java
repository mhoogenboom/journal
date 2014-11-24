package com.robinfinch.journal.app.util;

import android.text.TextUtils;

import com.robinfinch.journal.app.ui.adapter.JournalEntryGroup;
import com.robinfinch.journal.domain.NamedObject;

import java.util.Calendar;
import java.util.Date;

import static com.robinfinch.journal.app.util.Parser.DAY_INPUT;

/**
 * Collection of formatting methods
 *
 * @author Mark Hoogenboom
 */
public class Formatter {

    public static final CharSequence formatGroup(JournalEntryGroup group) {
        if (group == null) {
            return "";
        }
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, group.year);
        c.set(Calendar.MONTH, group.month);
        c.set(Calendar.DAY_OF_MONTH, 1);
        return String.format("%1$tB %1$tY", c);
    }

    public static final CharSequence formatDayForInput(Date day) {
        if (day == null) {
            return "";
        }
        return DAY_INPUT.format(day);
    }

    public static final CharSequence formatDay(Date day) {
        if (day == null) {
            return "";
        }
        return String.format("%1$tA %1$td", day);
    }

    public static final CharSequence formatNamedObject(NamedObject obj) {
        if (obj == null) {
            return "";
        }
        return obj.getName();
    }

    public static final CharSequence formatWalkDescription(String place) {
        return "Near " + place;
    }

    public static final CharSequence formatDistanceForInput(int distance) {
        return Integer.toString(distance);
    }

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

    public static final CharSequence formatPace(int pace) {
        int s = pace % 60;
        pace /= 60;
        return String.format("%1$d:%2$02d/km", pace, s);
    }

    public static final CharSequence formatTravelDescription(boolean away, String place) {
        if (TextUtils.isEmpty(place)) {
            return "";
        } else {
            if (away) {
                return "To " + place;
            } else {
                return "From " + place;
            }
        }
    }
}
