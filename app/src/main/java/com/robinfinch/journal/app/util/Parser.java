package com.robinfinch.journal.app.util;

import android.text.Editable;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.robinfinch.journal.app.util.Constants.LOG_TAG;

/**
 * Collection of parsing methods.
 *
 * @author Mark Hoogenboom
 */
public class Parser {

    static final DateFormat DAY_INPUT = new SimpleDateFormat("dd/MM/yyyy");

    public static Date parseDay(Editable text) {
        Date day = null;
        try {
            day = DAY_INPUT.parse(text.toString());
        } catch (ParseException e) {
            Log.e(LOG_TAG, "Failed to parse day " + text, e);
        }
        return day;
    }

    public static int parseDistance(Editable text) {
        int distance = 0;
        try {
            distance = Integer.parseInt(text.toString());
        } catch (NumberFormatException e) {
            Log.e(LOG_TAG, "Failed to parse distance " + text, e);
        }
        return distance;
    }

    public static int parseTime(Editable text) {
        int time = 0;
        try {
            String[] parts = text.toString().split(":");
            for (String part : parts) {
                time = (time * 60) + Integer.parseInt(part);
            }
        } catch (NumberFormatException e) {
            Log.e(LOG_TAG, "Failed to parse time " + text, e);
        }
        return time;
    }
}