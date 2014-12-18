package com.robinfinch.journal.domain;

import android.content.ContentValues;
import android.database.Cursor;

import com.robinfinch.journal.app.persistence.WalkEntryContract;

import java.util.Date;

import static com.robinfinch.journal.app.util.Utils.alias;
import static com.robinfinch.journal.app.util.Utils.differs;

/**
 * Journal entry describing a walk.
 *
 * @author Mark Hoogenboom
 */
public class WalkEntry extends JournalEntry {

    private String location;

    public static WalkEntry from(Cursor cursor) {
        WalkEntry entry = new WalkEntry();
        int i;

        i = cursor.getColumnIndexOrThrow(alias(WalkEntryContract.NAME, WalkEntryContract.COL_ID));
        long id = cursor.getLong(i);
        entry.setId(id);

        i = cursor.getColumnIndexOrThrow(alias(WalkEntryContract.NAME, WalkEntryContract.COL_REMOTE_ID));
        long remoteId = cursor.getLong(i);
        entry.setRemoteId(remoteId);

        i = cursor.getColumnIndexOrThrow(alias(WalkEntryContract.NAME, WalkEntryContract.COL_DAY_OF_ENTRY));
        long dayOfEntry = cursor.getLong(i);
        entry.setDayOfEntry((dayOfEntry == 0) ? null : new Date(dayOfEntry));

        i = cursor.getColumnIndexOrThrow(alias(WalkEntryContract.NAME, WalkEntryContract.COL_LOCATION));
        String location = cursor.getString(i);
        entry.setLocation(location);

        i = cursor.getColumnIndexOrThrow(alias(WalkEntryContract.NAME, WalkEntryContract.COL_LOG_ID));
        long logId = cursor.getLong(i);
        entry.setLogId(logId);

        return entry;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        if (differs(this.location, location)) {
            this.location = location;
            this.changed = true;
        }
    }

    @Override
    public ContentValues toValues() {
        ContentValues values = new ContentValues();
        values.put(WalkEntryContract.COL_DAY_OF_ENTRY, (getDayOfEntry() == null) ? 0 : getDayOfEntry().getTime());
        values.put(WalkEntryContract.COL_LOCATION, getLocation());
        return values;
    }

    @Override
    public String toString() {
        return "com.robinfinch.journal.domain.WalkEntry[id=" + getId()
                + ";remoteId=" + getRemoteId()
                + ";dayOfEntry=" + getDayOfEntry()
                + ";location=" + getLocation()
                + ";logId=" + getLogId()
                + "]";
    }
}
