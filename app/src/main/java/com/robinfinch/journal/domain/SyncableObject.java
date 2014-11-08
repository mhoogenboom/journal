package com.robinfinch.journal.domain;

import android.database.sqlite.SQLiteDatabase;

/**
 * Base class of all classes that are synced to the server.
 *
 * @author Mark Hoogenboom
 */
public abstract class SyncableObject extends PersistableObject {

    private transient Long remoteId;
    private transient Long logId;
    protected transient boolean changed;

    public Long getRemoteId() {
        return remoteId;
    }

    public void setRemoteId(Long remoteId) {
        this.remoteId = remoteId;
    }

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public void resetChanged() {
        changed = false;
    }

    public boolean hasChanged() {
        return changed;
    }

    public boolean prepareBeforeSend() {
        return true;
    }

    public void prepareAfterReceive(SQLiteDatabase db) {}
}
