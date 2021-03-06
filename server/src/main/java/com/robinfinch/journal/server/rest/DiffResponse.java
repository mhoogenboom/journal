package com.robinfinch.journal.server.rest;

import com.robinfinch.journal.domain.SyncLog;
import com.robinfinch.journal.domain.SyncableObject;
import com.robinfinch.journal.server.Revision;

import java.util.ArrayList;
import java.util.List;

/**
 * Response to a downward sync request, contains changed objects, the ids of deleted objects and
 * the latest included revision.
 *
 * @author Mark Hoogenboom
 */
public class DiffResponse {

    private Revision latestRevision;
    private final List<SyncableObject> changes;
    private final List<SyncableObject> deletes;

    public DiffResponse(Revision latestRevision) {
        this.latestRevision = latestRevision;
        this.changes = new ArrayList<>();
        this.deletes = new ArrayList<>();
    }

    public void include(SyncLog log) {
        if (log.getId() > latestRevision.getDataVersion()) {
            latestRevision.setDataVersion(log.getId());
        }
        if (log.getChangedEntity() == null) {
            if (!deletes.contains(log.getDeletedEntity())) {
                deletes.add(log.getDeletedEntity());
            }
        } else {
            if (!changes.contains(log.getChangedEntity())) {
                log.getChangedEntity().prepareBeforeSend();
                changes.add(log.getChangedEntity());
            }
        }
    }

    public Revision getLatestRevision() {
        return latestRevision;
    }

    public List<SyncableObject> getChanges() {
        return changes;
    }

    public List<SyncableObject> getDeletes() {
        return deletes;
    }
}
