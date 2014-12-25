package com.robinfinch.journal.app.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static com.robinfinch.journal.app.util.Constants.LOG_TAG;

/**
 * Collection of utility methods.
 *
 * @author Mark Hoogenboom
 */
public class Utils {

    public static <T> boolean differs(T o1, T o2) {
        if (o1 == null) {
            return (o2 != null);
        } else {
            return !o1.equals(o2);
        }
    }

    public static long getDefaultDayOfEntry(Context context) {
        Date day = null;

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        String monthYearOfEntry = prefs.getString("prefs_entry_month_year", "");
        if (!TextUtils.isEmpty(monthYearOfEntry)) {
            day = Parser.parseMonthYear(monthYearOfEntry);
        }

        return (day == null) ? getToday() : day.getTime();
    }

    public static long getToday() {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("Europe/London"), Locale.UK);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTimeInMillis();
    }
	
    public static String hash(String s, String algorithm) {
        byte[] digest = hash(s.getBytes(), algorithm);

        StringBuffer digestHex = new StringBuffer();
        for (int i = 0; i < digest.length; i++) {
            String h = Integer.toHexString(0xff & digest[i]);
            if (h.length() == 1) {
                digestHex.append("0");
            }
            digestHex.append(h);
        }

        return digestHex.toString();
    }

    public static byte[] hash(byte[] b, String algorithm) {
        byte[] result;
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            digest.update(b);
            result = digest.digest();
        } catch (NoSuchAlgorithmException e) {
            Log.e(LOG_TAG, "Failed to hash", e);
            result = null;
        }
        return result;
    }

    /**
     * Defines an alias for an id column (to use in cursor adapters).
     */
    public static String aliasedId(String tableName, String columnName) {
        StringBuilder sb = new StringBuilder();
        sb.append(tableName);
        sb.append(".");
        sb.append(columnName);
        sb.append(" AS ");
        sb.append(BaseColumns._ID);
        return sb.toString();
    }

    /**
     * Defines an alias for a column.
     */
    public static String aliased(String tableName, String columnName) {
        StringBuilder sb = new StringBuilder();
        sb.append(tableName);
        sb.append(".");
        sb.append(columnName);
        sb.append(" AS ");
        sb.append(tableName);
        sb.append("_");
        sb.append(columnName);
        return sb.toString();
    }

    /**
     * Alias to be used in cursor.getIndex().
     */
    public static String alias(String tableName, String columnName) {
        StringBuilder sb = new StringBuilder();
        sb.append(tableName);
        sb.append("_");
        sb.append(columnName);
        return sb.toString();
    }

    /**
     * Prefixed column name to be used in joins.
     */
    public static String prefixed(String tableName, String columnName) {
        StringBuilder sb = new StringBuilder();
        sb.append(tableName);
        sb.append(".");
        sb.append(columnName);
        return sb.toString();
    }
}
