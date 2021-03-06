package com.robinfinch.journal.app.rest;

import com.robinfinch.journal.app.sync.Revision;
import com.robinfinch.journal.domain.SyncableObject;

import java.util.List;

/**
 * Response to a downward sync request, contains changed objects, the ids of deleted objects and
 * the latest included revision.
 *
 * @author Mark Hoogenboom
 */
public class DiffResponse {

    private Revision latestRevision;
    private List<SyncableObject> changes;
    private List<SyncableObject> deletes;

    public Revision getLatestRevision() {
        return latestRevision;
    }

    public List<SyncableObject> getChanges() {
        return changes;
    }

    public List<SyncableObject> getDeletes() {
        return deletes;
    }

    @Override
    public String toString() {
        return "com.robinfinch.journal.app.rest.DiffResponse[latestRevision=" + getLatestRevision()
                + ";changes=" + getChanges()
                + ";deletes=" + getDeletes()
                + "]";
    }
}
