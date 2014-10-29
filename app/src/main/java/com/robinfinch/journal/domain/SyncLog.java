package com.robinfinch.journal.domain;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

import com.robinfinch.journal.app.persistence.SyncLogContract;

/**
 * Keeps track of changes to be synced.
 *
 * @author Mark Hoogenboom
 */
public class SyncLog extends PersistableObject {

    private String entityName;
    private Long entityId;
    private Long entityRemoteId;

    public static SyncLog from(Cursor cursor) {
        SyncLog syncLog = new SyncLog();
        int i;

        i = cursor.getColumnIndexOrThrow(BaseColumns._ID);
        long id = cursor.getLong(i);
        syncLog.setId(id);

        i = cursor.getColumnIndexOrThrow(SyncLogContract.COL_ENTITY_NAME);
        String entityName = cursor.getString(i);
        syncLog.setEntityName(entityName);

        i = cursor.getColumnIndexOrThrow(SyncLogContract.COL_ENTITY_ID);
        long entityId = cursor.getLong(i);
        syncLog.setEntityId(entityId);

        i = cursor.getColumnIndexOrThrow(SyncLogContract.COL_ENTITY_REMOTE_ID);
        long entityRemoteId = cursor.getLong(i);
        syncLog.setEntityRemoteId(entityRemoteId);

        return syncLog;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getEntityRemoteId() {
        return entityRemoteId;
    }

    public void setEntityRemoteId(Long entityRemoteId) {
        this.entityRemoteId = entityRemoteId;
    }

    public ContentValues toValues() {
        ContentValues values = new ContentValues();
        values.put(SyncLogContract.COL_ENTITY_NAME, getEntityName());
        values.put(SyncLogContract.COL_ENTITY_ID, getEntityId());
        values.put(SyncLogContract.COL_ENTITY_REMOTE_ID, getEntityRemoteId());
        return values;
    }

    @Override
    public String toString() {
        return "com.robinfinch.journal.domain.SyncLog[id=" + getId()
                + ";entityName=" + getEntityName()
                + ";entityId=" + getEntityId()
                + ";entityRemoteId=" + getEntityRemoteId()
                + "]";
    }
}
