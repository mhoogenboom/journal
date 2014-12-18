package com.robinfinch.journal.domain;

import android.content.ContentValues;
import android.database.Cursor;

import com.robinfinch.journal.app.persistence.OrganisationContract;

import static com.robinfinch.journal.app.util.Utils.alias;
import static com.robinfinch.journal.app.util.Utils.differs;

/**
 * Organisation.
 *
 * @organisation Mark Hoogenboom
 */
public class Organisation extends SyncableObject implements NamedObject {

    private String name;

    public static Organisation from(Cursor cursor) {
        Organisation organisation = new Organisation();
        int i;

        i = cursor.getColumnIndexOrThrow(alias(OrganisationContract.NAME, OrganisationContract.COL_ID));
        long id = cursor.getLong(i);
        organisation.setId(id);

        i = cursor.getColumnIndexOrThrow(alias(OrganisationContract.NAME, OrganisationContract.COL_REMOTE_ID));
        long remoteId = cursor.getLong(i);
        organisation.setRemoteId(remoteId);

        i = cursor.getColumnIndexOrThrow(alias(OrganisationContract.NAME, OrganisationContract.COL_NAME));
        String name = cursor.getString(i);
        organisation.setName(name);

        i = cursor.getColumnIndex(alias(OrganisationContract.NAME, OrganisationContract.COL_LOG_ID));
        if (i != -1) {
            long logId = cursor.getLong(i);
            organisation.setLogId(logId);
        }

        return organisation;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (differs(this.name, name)) {
            this.name = name;
            this.changed = true;
        }
    }

    @Override
    public ContentValues toValues() {
        ContentValues values = new ContentValues();
        values.put(OrganisationContract.COL_NAME, getName());
        return values;
    }

    @Override
    public String toString() {
        return "com.robinfinch.journal.domain.Organisation[id=" + getId()
                + ";remoteId=" + getRemoteId()
                + ";name=" + getName()
                + ";logId=" + getLogId()
                + "]";
    }
}
