package com.robinfinch.journal.domain;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.robinfinch.journal.app.persistence.ApplicationContract;
import com.robinfinch.journal.app.persistence.OrganisationContract;
import com.robinfinch.journal.app.persistence.RecruiterContract;

import static com.robinfinch.journal.app.util.Utils.differs;

/**
 * Written work.
 *
 * @recruiter Mark Hoogenboom
 */
public class Application extends SyncableObject implements NamedObject {

    private long recruiterId;

    private transient Recruiter recruiter;

    private long clientId;

    private transient Organisation client;

    private String start;

    private String rate;

    private long stateId;

    public static Application from(Cursor cursor, String prefix) {
        Application application = new Application();
        int i;

        i = cursor.getColumnIndexOrThrow(BaseColumns._ID);
        long id = cursor.getLong(i);
        application.setId(id);

        i = cursor.getColumnIndexOrThrow(prefix + ApplicationContract.COL_REMOTE_ID);
        long remoteId = cursor.getLong(i);
        application.setRemoteId(remoteId);

        i = cursor.getColumnIndex(prefix + ApplicationContract.COL_RECRUITER_ID);
        if (i != -1) {
            long recruiterId = cursor.getLong(i);
            if (recruiterId == 0) {
                application.setRecruiter(null);
            } else {
                Recruiter recruiter = Recruiter.from(cursor, RecruiterContract.NAME + "_");
                application.setRecruiter(recruiter);
            }
        }

        i = cursor.getColumnIndexOrThrow(prefix + ApplicationContract.COL_CLIENT_ID);
        long clientId = cursor.getLong(i);
        if (clientId == 0) {
            application.setClient(null);
        } else {
            Organisation client = Organisation.from(cursor, OrganisationContract.NAME + "_");
            application.setClient(client);
        }

        i = cursor.getColumnIndexOrThrow(prefix + ApplicationContract.COL_START);
        String start = cursor.getString(i);
        application.setStart(start);

        i = cursor.getColumnIndexOrThrow(prefix + ApplicationContract.COL_RATE);
        String rate = cursor.getString(i);
        application.setRate(rate);

        i = cursor.getColumnIndex(prefix + ApplicationContract.COL_STATE_ID);
        if (i != -1) {
            long stateId = cursor.getLong(i);
            application.setStateId(stateId);
        }

        i = cursor.getColumnIndex(prefix + ApplicationContract.COL_LOG_ID);
        if (i != -1) {
            long logId = cursor.getLong(i);
            application.setLogId(logId);
        }

        return application;
    }

    public long getRecruiterId() {
        return recruiterId;
    }

    public void setRecruiterId(long recruiterId) {
        this.recruiterId = recruiterId;
    }

    public Recruiter getRecruiter() {
        return recruiter;
    }

    public void setRecruiter(Recruiter recruiter) {
        if (differs(this.recruiter, recruiter)) {
            this.recruiter = recruiter;
            this.changed = true;
        }
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public Organisation getClient() {
        return client;
    }

    public void setClient(Organisation client) {
        if (differs(this.client, client)) {
            this.client = client;
            this.changed = true;
        }
    }    
    
    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        if (differs(this.start, start)) {
            this.start = start;
            this.changed = true;
        }
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        if (differs(this.rate, rate)) {
            this.rate = rate;
            this.changed = true;
        }
    }

    public long getStateId() {
        return stateId;
    }

    public void setStateId(long stateId) {
        if (differs(this.stateId, stateId)) {
            this.stateId = stateId;
            this.changed = true;
        }
    }

    @Override
    public CharSequence getName() {
        StringBuilder sb = new StringBuilder();
        if (client != null) {
            sb.append(client.getName());
        }
        if (start != null) {
            sb.append(" - ");
            sb.append(start);
        }
        if (rate != null) {
            sb.append(" - ");
            sb.append(rate);
        }
        return sb;
    }
    
    @Override
    public ContentValues toValues() {
        ContentValues values = new ContentValues();
        values.put(ApplicationContract.COL_RECRUITER_ID, (getRecruiter() == null) ? 0 : getRecruiter().getId());
        values.put(ApplicationContract.COL_CLIENT_ID, (getClient() == null) ? 0 : getClient().getId());
        values.put(ApplicationContract.COL_START, start);
        values.put(ApplicationContract.COL_RATE, rate);
        values.put(ApplicationContract.COL_STATE_ID, stateId);
        return values;
    }

    @Override
    public boolean prepareBeforeSend() {
        if (recruiter == null) {
            recruiterId = 0;
        } else {
            if (recruiter.getRemoteId() == 0) {
                return false;
            } else {
                recruiterId = recruiter.getRemoteId();
            }
        }

        if (client == null) {
            clientId = 0;
        } else {
            if (client.getRemoteId() == 0) {
                return false;
            } else {
                clientId = client.getRemoteId();
            }
        }        
        return super.prepareBeforeSend();
    }

    @Override
    public boolean prepareAfterReceive(SQLiteDatabase db) {
        if (recruiterId == 0) {
            recruiter = null;
        } else {
            Cursor cursor = db.query(RecruiterContract.NAME + RecruiterContract.JOINS, RecruiterContract.COLS,
                    RecruiterContract.NAME + "_" + RecruiterContract.COL_REMOTE_ID + "=" + recruiterId, null, null, null, null, null);

            if (cursor.moveToFirst()) {
                recruiter = Recruiter.from(cursor, RecruiterContract.NAME + "_");
            } else {
                return false;
            }
        }

        if (clientId == 0) {
            client = null;
        } else {
            Cursor cursor = db.query(OrganisationContract.NAME, OrganisationContract.COLS,
                    OrganisationContract.COL_REMOTE_ID + "=" + clientId, null, null, null, null, null);

            if (cursor.moveToFirst()) {
                client = Organisation.from(cursor, "");
            } else {
                return false;
            }
        }        
        return super.prepareAfterReceive(db);
    }

    @Override
    public String toString() {
        return "com.robinfinch.journal.domain.Application[id=" + getId()
                + ";remoteId=" + getRemoteId()
                + ";recruiterId=" + getRecruiterId()
                + ";recruiter=" + getRecruiter()
                + ";clientId=" + getClientId()
                + ";client=" + getClient()
                + ";start=" + getStart()
                + ";rate=" + getRate()
                + ";stateId=" + getStateId()
                + ";logId=" + getLogId()
                + "]";
    }
}
