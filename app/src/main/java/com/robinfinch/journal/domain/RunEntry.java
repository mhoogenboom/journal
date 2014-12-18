package com.robinfinch.journal.domain;

import android.content.ContentValues;
import android.database.Cursor;

import com.robinfinch.journal.app.persistence.RunEntryContract;

import java.util.Date;

import static com.robinfinch.journal.app.util.Utils.alias;
import static com.robinfinch.journal.app.util.Utils.differs;

/**
 * Journal entry describing a run.
 *
 * @author Mark Hoogenboom
 */
public class RunEntry extends JournalEntry {

    private int distance; // in m

    private int timeTaken; // in s

    public static RunEntry from(Cursor cursor) {
        RunEntry entry = new RunEntry();
        int i;

        i = cursor.getColumnIndexOrThrow(alias(RunEntryContract.NAME, RunEntryContract.COL_ID));
        long id = cursor.getLong(i);
        entry.setId(id);

        i = cursor.getColumnIndexOrThrow(alias(RunEntryContract.NAME, RunEntryContract.COL_REMOTE_ID));
        long remoteId = cursor.getLong(i);
        entry.setRemoteId(remoteId);

        i = cursor.getColumnIndexOrThrow(alias(RunEntryContract.NAME, RunEntryContract.COL_DAY_OF_ENTRY));
        long dayOfEntry = cursor.getLong(i);
        entry.setDayOfEntry((dayOfEntry == 0) ? null : new Date(dayOfEntry));

        i = cursor.getColumnIndexOrThrow(alias(RunEntryContract.NAME, RunEntryContract.COL_DISTANCE));
        int distance = cursor.getInt(i);
        entry.setDistance(distance);

        i = cursor.getColumnIndexOrThrow(alias(RunEntryContract.NAME, RunEntryContract.COL_TIME_TAKEN));
        int timeTaken = cursor.getInt(i);
        entry.setTimeTaken(timeTaken);

        i = cursor.getColumnIndexOrThrow(alias(RunEntryContract.NAME, RunEntryContract.COL_LOG_ID));
        long logId = cursor.getLong(i);
        entry.setLogId(logId);

        return entry;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        if (differs(this.distance, distance)) {
            this.distance = distance;
            this.changed = true;
        }
    }

    public int getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(int timeTaken) {
        if (differs(this.timeTaken, timeTaken)) {
            this.timeTaken = timeTaken;
            this.changed = true;
        }
    }

    public int getAvgPace() {
        return (distance == 0) ? 0 : timeTaken * 1000 / distance;
    }

    @Override
    public ContentValues toValues() {
        ContentValues values = new ContentValues();
        values.put(RunEntryContract.COL_DAY_OF_ENTRY, (getDayOfEntry() == null) ? 0 : getDayOfEntry().getTime());
        values.put(RunEntryContract.COL_DISTANCE, getDistance());
        values.put(RunEntryContract.COL_TIME_TAKEN, getTimeTaken());
        return values;
    }

    @Override
    public String toString() {
        return "com.robinfinch.journal.domain.RunEntry[id=" + getId()
                + ";remoteId=" + getRemoteId()
                + ";dayOfEntry=" + getDayOfEntry()
                + ";distance=" + getDistance()
                + ";timeTaken=" + getTimeTaken()
                + ";logId=" + getLogId()
                + "]";
    }
}
