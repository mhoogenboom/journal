package com.robinfinch.journal.domain;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import com.robinfinch.journal.app.persistence.ReadEntryContract;
import com.robinfinch.journal.app.persistence.TitleContract;

import java.util.Date;

import static com.robinfinch.journal.app.util.Constants.LOG_TAG;
import static com.robinfinch.journal.app.util.Utils.*;

/**
 * Journal entry describing reading a title.
 *
 * @author Mark Hoogenboom
 */
public class ReadEntry extends JournalEntry {

    private long titleId;

    private transient Title title;

    private String part;

    public static ReadEntry from(Cursor cursor, String prefix) {
        ReadEntry entry = new ReadEntry();
        int i;

        i = cursor.getColumnIndexOrThrow(BaseColumns._ID);
        long id = cursor.getLong(i);
        entry.setId(id);

        i = cursor.getColumnIndexOrThrow(prefix + ReadEntryContract.COL_REMOTE_ID);
        long remoteId = cursor.getLong(i);
        entry.setRemoteId(remoteId);

        i = cursor.getColumnIndexOrThrow(ReadEntryContract.COL_DAY_OF_ENTRY);
        long dayOfEntry = cursor.getLong(i);
        entry.setDayOfEntry((dayOfEntry == 0) ? null : new Date(dayOfEntry));

        i = cursor.getColumnIndexOrThrow(prefix + ReadEntryContract.COL_TITLE_ID);
        long titleId = cursor.getLong(i);
        if (titleId == 0) {
            entry.setTitle(null);
        } else {
            Title title = Title.from(cursor, TitleContract.NAME + "_");
            entry.setTitle(title);
        }

        i = cursor.getColumnIndexOrThrow(prefix + ReadEntryContract.COL_PART);
        String part = cursor.getString(i);
        entry.setPart(part);

        i = cursor.getColumnIndexOrThrow(prefix + ReadEntryContract.COL_LOG_ID);
        long logId = cursor.getLong(i);
        entry.setLogId(logId);

        return entry;
    }

    public long getTitleId() {
        return titleId;
    }

    public void setTitleId(long titleId) {
        this.titleId = titleId;
    }

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        if (differs(this.title, title)) {
            this.title = title;
            this.changed = true;
        }
    }

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        if (differs(this.part, part)) {
            this.part = part;
            this.changed = true;
        }
    }

    @Override
    public ContentValues toValues() {
        ContentValues values = new ContentValues();
        values.put(ReadEntryContract.COL_DAY_OF_ENTRY, (getDayOfEntry() == null) ? 0 : getDayOfEntry().getTime());
        values.put(ReadEntryContract.COL_TITLE_ID, (getTitle() == null) ? 0 : getTitle().getId());
        values.put(ReadEntryContract.COL_PART, part);
        return values;
    }

    @Override
    public boolean prepareBeforeSend() {
        if (title == null) {
            titleId = 0;
        } else {
            if (title.getRemoteId() == 0) {
                return false;
            } else {
                titleId = title.getRemoteId();
            }
        }
        return super.prepareBeforeSend();
    }

    @Override
    public void prepareAfterReceive(SQLiteDatabase db) {
        if (titleId == 0) {
            title = null;
        } else {
            Cursor cursor = db.query(TitleContract.NAME + TitleContract.JOINS, TitleContract.COLS,
                   TitleContract.NAME + "_" + TitleContract.COL_REMOTE_ID + "=" + titleId, null, null, null, null, null);

            if (cursor.moveToFirst()) {
                title = Title.from(cursor, TitleContract.NAME + "_");
            } else {
                Log.d(LOG_TAG, "Received " + this + ", title unknown.");
                title = null;
            }
        }
    }

    @Override
    public String toString() {
        return "com.robinfinch.journal.domain.StudyEntry[id=" + getId()
                + ";remoteId=" + getRemoteId()
                + ";dayOfEntry=" + getDayOfEntry()
                + ";titleId=" + getTitleId()
                + ";title=" + getTitle()
                + ";part=" + getPart()
                + ";logId=" + getLogId()
                + "]";
    }
}
