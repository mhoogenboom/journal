package com.robinfinch.journal.app.util;

import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQuery;
import android.util.Log;

import static com.robinfinch.journal.app.util.Constants.LOG_TAG;

/**
 * Logs the query for a cursor.
 *
 * @author Mark Hoogenboom
 */
public class LoggingCursorFactory implements SQLiteDatabase.CursorFactory {

    @Override
    public Cursor newCursor(SQLiteDatabase db, SQLiteCursorDriver masterQuery,
                            String editTable, SQLiteQuery query) {

        Log.d(LOG_TAG, query.toString());

        return new SQLiteCursor(masterQuery, editTable, query);
    }
}
