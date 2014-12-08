package com.robinfinch.journal.domain;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.robinfinch.journal.app.persistence.ApplicationContract;
import com.robinfinch.journal.app.persistence.ApplicationEntryContract;

import java.util.Date;

import static com.robinfinch.journal.app.util.Utils.differs;

/**
 * Journal entry describing applying for a job.
 *
 * @author Mark Hoogenboom
 */
public class ApplicationEntry extends JournalEntry {

    private long applicationId;
    
    private Application application;

    private long actionId;

    public static ApplicationEntry from(Cursor cursor, String prefix) {
        ApplicationEntry entry = new ApplicationEntry();
        int i;

        i = cursor.getColumnIndexOrThrow(BaseColumns._ID);
        long id = cursor.getLong(i);
        entry.setId(id);

        i = cursor.getColumnIndexOrThrow(prefix + ApplicationEntryContract.COL_REMOTE_ID);
        long remoteId = cursor.getLong(i);
        entry.setRemoteId(remoteId);

        i = cursor.getColumnIndexOrThrow(ApplicationEntryContract.COL_DAY_OF_ENTRY);
        long dayOfEntry = cursor.getLong(i);
        entry.setDayOfEntry((dayOfEntry == 0) ? null : new Date(dayOfEntry));

        i = cursor.getColumnIndexOrThrow(prefix + ApplicationEntryContract.COL_APPLICATION_ID);
        long applicationId = cursor.getLong(i);
        if (applicationId == 0) {
            entry.setApplication(null);
        } else {
            Application application = Application.from(cursor, ApplicationContract.NAME + "_");
            entry.setApplication(application);
        }

        i = cursor.getColumnIndexOrThrow(prefix + ApplicationEntryContract.COL_ACTION_ID);
        long actionId = cursor.getLong(i);
        entry.setActionId(actionId);

        i = cursor.getColumnIndexOrThrow(prefix + ApplicationEntryContract.COL_LOG_ID);
        long logId = cursor.getLong(i);
        entry.setLogId(logId);

        return entry;
    }

    public long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(long applicationId) {
        this.applicationId = applicationId;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        if (differs(this.application, application)) {
            this.application = application;
            this.changed = true;
        }
    }

    public long getActionId() {
        return actionId;
    }

    public void setActionId(long actionId) {
        if (differs(this.actionId, actionId)) {
            this.actionId = actionId;
            this.changed = true;
        }
    }

    @Override
    public ContentValues toValues() {
        ContentValues values = new ContentValues();
        values.put(ApplicationEntryContract.COL_DAY_OF_ENTRY, (getDayOfEntry() == null) ? 0 : getDayOfEntry().getTime());
        values.put(ApplicationEntryContract.COL_APPLICATION_ID, (getApplication() == null) ? 0 : getApplication().getId());
        values.put(ApplicationEntryContract.COL_ACTION_ID, actionId);
        return values;
    }

    @Override
    public boolean prepareBeforeSend() {
        if (application == null) {
            applicationId = 0;
        } else {
            if (application.getRemoteId() == 0) {
                return false;
            } else {
                applicationId = application.getRemoteId();
            }
        }
        return super.prepareBeforeSend();
    }

    @Override
    public boolean prepareAfterReceive(SQLiteDatabase db) {
        if (applicationId == 0) {
            application = null;
        } else {
            Cursor cursor = db.query(ApplicationContract.NAME + ApplicationContract.JOINS, ApplicationContract.COLS,
                   ApplicationContract.NAME + "_" + ApplicationContract.COL_REMOTE_ID + "=" + applicationId, null, null, null, null, null);

            if (cursor.moveToFirst()) {
                application = Application.from(cursor, ApplicationContract.NAME + "_");
            } else {
                return false;
            }
        }
        return super.prepareAfterReceive(db);
    }

    @Override
    public String toString() {
        return "com.robinfinch.journal.domain.Applicationentry[id=" + getId()
                + ";remoteId=" + getRemoteId()
                + ";dayOfEntry=" + getDayOfEntry()
                + ";applicationId=" + getApplicationId()
                + ";application=" + getApplication()
                + ";actionId=" + getActionId()
                + ";logId=" + getLogId()
                + "]";
    }
}
