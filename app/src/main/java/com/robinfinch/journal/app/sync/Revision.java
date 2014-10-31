package com.robinfinch.journal.app.sync;

import android.database.Cursor;

import com.robinfinch.journal.app.persistence.RevisionContract;

/**
 * The latest revision of the remote data set that has been received.
 *
 * @author Mark Hoogenboom
 */
public class Revision {

    public static Revision from(Cursor cursor) {
        Revision revision = new Revision();
        int i;

        i = cursor.getColumnIndexOrThrow(RevisionContract.COL_LATEST_REVISION);
        long latestRevision = cursor.getLong(i);
        revision.setLatestRevision(latestRevision);

        return revision;
    }

    private long latestRevision;

    public long getLatestRevision() {
        return latestRevision;
    }

    public void setLatestRevision(long latestRevision) {
        this.latestRevision = latestRevision;
    }

    @Override
    public String toString() {
        return "com.robinfinch.journal.app.rest.Revision[latestRevision=" + getLatestRevision()
                + "]";
    }
}
