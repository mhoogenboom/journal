package com.robinfinch.journal.domain;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.robinfinch.journal.app.persistence.OrganisationContract;
import com.robinfinch.journal.app.persistence.RecruiterContract;

import static com.robinfinch.journal.app.util.Utils.differs;

/**
 * Recruiter.
 *
 * @organisation Mark Hoogenboom
 */
public class Recruiter extends SyncableObject implements NamedObject {

    private String name;
    
    private long organisationId;

    private transient Organisation organisation;

    private String phoneNumber;

    public static Recruiter from(Cursor cursor, String prefix) {
        Recruiter recruiter = new Recruiter();
        int i;

        i = cursor.getColumnIndexOrThrow(BaseColumns._ID);
        long id = cursor.getLong(i);
        recruiter.setId(id);

        i = cursor.getColumnIndexOrThrow(prefix + RecruiterContract.COL_REMOTE_ID);
        long remoteId = cursor.getLong(i);
        recruiter.setRemoteId(remoteId);

        i = cursor.getColumnIndexOrThrow(prefix + RecruiterContract.COL_NAME);
        String name = cursor.getString(i);
        recruiter.setName(name);

        i = cursor.getColumnIndexOrThrow(prefix + RecruiterContract.COL_ORGANISATION_ID);
        long organisationId = cursor.getLong(i);
        if (organisationId == 0) {
            recruiter.setOrganisation(null);
        } else {
            Organisation organisation = Organisation.from(cursor, OrganisationContract.NAME + "_");
            recruiter.setOrganisation(organisation);
        }

        i = cursor.getColumnIndexOrThrow(prefix + RecruiterContract.COL_PHONE_NUMBER);
        String phoneNumber = cursor.getString(i);
        recruiter.setPhoneNumber(phoneNumber);

        i = cursor.getColumnIndex(prefix + RecruiterContract.COL_LOG_ID);
        if (i != -1) {
            long logId = cursor.getLong(i);
            recruiter.setLogId(logId);
        }

        return recruiter;
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

    public long getOrganisationId() {
        return organisationId;
    }

    public void setOrganisationId(long organisationId) {
        this.organisationId = organisationId;
    }

    public Organisation getOrganisation() {
        return organisation;
    }

    public void setOrganisation(Organisation organisation) {
        if (differs(this.organisation, organisation)) {
            this.organisation = organisation;
            this.changed = true;
        }
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (differs(this.phoneNumber, phoneNumber)) {
            this.phoneNumber = phoneNumber;
            this.changed = true;
        }
    }

    @Override
    public ContentValues toValues() {
        ContentValues values = new ContentValues();
        values.put(RecruiterContract.COL_NAME, name);
        values.put(RecruiterContract.COL_ORGANISATION_ID, (getOrganisation() == null) ? 0 : getOrganisation().getId());
        values.put(RecruiterContract.COL_PHONE_NUMBER, phoneNumber);
        return values;
    }

    @Override
    public boolean prepareBeforeSend() {
        if (organisation == null) {
            organisationId = 0;
        } else {
            if (organisation.getRemoteId() == 0) {
                return false;
            } else {
                organisationId = organisation.getRemoteId();
            }
        }
        return super.prepareBeforeSend();
    }

    @Override
    public boolean prepareAfterReceive(SQLiteDatabase db) {
        if (organisationId == 0) {
            organisation = null;
        } else {
            Cursor cursor = db.query(OrganisationContract.NAME, OrganisationContract.COLS,
                    OrganisationContract.COL_REMOTE_ID + "=" + organisationId, null, null, null, null, null);

            if (cursor.moveToFirst()) {
                organisation = Organisation.from(cursor, "");
            } else {
                return false;
            }
        }
        return super.prepareAfterReceive(db);
    }

    @Override
    public String toString() {
        return "com.robinfinch.journal.domain.Recruiter[id=" + getId()
                + ";remoteId=" + getRemoteId()
                + ";name=" + getName()
                + ";organisationId=" + getOrganisationId()
                + ";organisation=" + getOrganisation()
                + ";phoneNumber=" + getPhoneNumber()
                + ";logId=" + getLogId()
                + "]";
    }
}
