package com.robinfinch.journal.app.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;

import com.robinfinch.journal.app.notifications.MyNotificationManager;
import com.robinfinch.journal.app.persistence.CourseContract;
import com.robinfinch.journal.app.persistence.DbHelper;
import com.robinfinch.journal.app.persistence.RevisionContract;
import com.robinfinch.journal.app.persistence.RunEntryContract;
import com.robinfinch.journal.app.persistence.StudyEntryContract;
import com.robinfinch.journal.app.persistence.SyncLogContract;
import com.robinfinch.journal.app.persistence.SyncableObjectContract;
import com.robinfinch.journal.app.persistence.TravelEntryContract;
import com.robinfinch.journal.app.persistence.WalkEntryContract;
import com.robinfinch.journal.app.rest.DiffResponse;
import com.robinfinch.journal.app.rest.JournalApi;
import com.robinfinch.journal.app.rest.SyncableObjectWrapper;
import com.robinfinch.journal.app.util.DirUriType;
import com.robinfinch.journal.app.util.Function;
import com.robinfinch.journal.domain.Course;
import com.robinfinch.journal.domain.RunEntry;
import com.robinfinch.journal.domain.StudyEntry;
import com.robinfinch.journal.domain.SyncLog;
import com.robinfinch.journal.domain.SyncableObject;
import com.robinfinch.journal.domain.TravelEntry;
import com.robinfinch.journal.domain.WalkEntry;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import retrofit.RetrofitError;

import static com.robinfinch.journal.app.util.Constants.LOG_TAG;

/**
 * Sync adapter.
 * Sends and receives changes and deletes to and from the server.
 *
 * @author Mark Hoogenboom
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {

    private static final Map<Class, DirUriType> URI_TYPES_BY_CLASS = new HashMap<>();
    static {
        URI_TYPES_BY_CLASS.put(StudyEntry.class, StudyEntryContract.DIR_URI_TYPE);
        URI_TYPES_BY_CLASS.put(Course.class, CourseContract.DIR_URI_TYPE);
        URI_TYPES_BY_CLASS.put(WalkEntry.class, WalkEntryContract.DIR_URI_TYPE);
        URI_TYPES_BY_CLASS.put(RunEntry.class, RunEntryContract.DIR_URI_TYPE);
        URI_TYPES_BY_CLASS.put(TravelEntry.class, TravelEntryContract.DIR_URI_TYPE);
    }

    public static final Set<Class> SYNCABLE_OBJECT_CLASSES = URI_TYPES_BY_CLASS.keySet();

    public static final String AUTH_TOKEN_TYPE_SYNC = "com.robinfinch.sync";

    private DbHelper dbHelper;
    private ConnectivityChecker connectivityChecker;
    private JournalApi api;
    private int maxEntriesToSend;
    private MyNotificationManager notificationManager;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    public SyncAdapter(Context context, boolean autoInitialize,
                       boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
    }

    public SyncAdapter config(
            ConnectivityChecker connectivityChecker,
            JournalApi api,
            int maxEntriesToSend,
            MyNotificationManager notificationManager) {
        this.dbHelper = new DbHelper(getContext());
        this.connectivityChecker = connectivityChecker;
        this.api = api;
        this.maxEntriesToSend = maxEntriesToSend;
        this.notificationManager = notificationManager;
        return this;
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.i(LOG_TAG, "Perform sync");

        if (!connectivityChecker.isOk()) {
            Log.w(LOG_TAG, "Sync postponed because of connectivity");
            return;
        }

        AccountManager accountManager = AccountManager.get(getContext());
        AccountManagerFuture<Bundle> futureResult = accountManager.getAuthToken(account, AUTH_TOKEN_TYPE_SYNC, null, true, null, null);

        Bundle result;
        try {
            result = futureResult.getResult(2, TimeUnit.MINUTES);
        } catch (OperationCanceledException | AuthenticatorException | IOException e) {
            Log.w(LOG_TAG, "Failed to get authentication token, cancelled sync", e);
            return;
        }

        String token = result.getString(AccountManager.KEY_AUTHTOKEN);

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        int entriesToSend = maxEntriesToSend;

        while (entriesToSend > 0) {
            Cursor cursor = db.query(SyncLogContract.NAME, SyncLogContract.COLS,
                    null, null, null, null, SyncLogContract.COL_ID + " ASC", "1");

            if (cursor.moveToFirst()) {
                SyncLog log = SyncLog.from(cursor);
                send(db, log, token);
                entriesToSend--;
            } else {
                cursor = db.query(RevisionContract.NAME, RevisionContract.COLS,
                        null, null, null, null, null, null);

                Revision latestRevision;
                if (cursor.moveToFirst()) {
                    latestRevision = Revision.from(cursor);
                } else {
                    latestRevision = new Revision();
                }

                receive(latestRevision, token);
                entriesToSend = 0;
            }
        }

        Log.i(LOG_TAG, "Sync done");
    }

    private void send(SQLiteDatabase db, SyncLog log, String token) {
        Log.d(LOG_TAG, "Send " + log);

        try {
            Cursor cursor;
            Function<Cursor, SyncableObject> from;

            switch (log.getEntityName()) {
                case StudyEntryContract.NAME:
                    cursor = query(db, StudyEntryContract.DIR_URI_TYPE, StudyEntryContract.COLS, log.getEntityId());

                    from = new Function<Cursor, SyncableObject>() {
                        @Override
                        public SyncableObject apply(Cursor cursor) {
                            return StudyEntry.from(cursor, StudyEntryContract.NAME + "_");
                        }
                    };
                    break;

                case CourseContract.NAME:
                    cursor = query(db, CourseContract.DIR_URI_TYPE, CourseContract.COLS, log.getEntityId());

                    from = new Function<Cursor, SyncableObject>() {
                        @Override
                        public SyncableObject apply(Cursor cursor) {
                            return Course.from(cursor, "");
                        }
                    };
                    break;

                case WalkEntryContract.NAME:
                    cursor = query(db, WalkEntryContract.DIR_URI_TYPE, WalkEntryContract.COLS, log.getEntityId());

                    from = new Function<Cursor, SyncableObject>() {
                        @Override
                        public SyncableObject apply(Cursor cursor) {
                            return WalkEntry.from(cursor);
                        }
                    };
                    break;

                case RunEntryContract.NAME:
                    cursor = query(db, RunEntryContract.DIR_URI_TYPE, RunEntryContract.COLS, log.getEntityId());

                    from = new Function<Cursor, SyncableObject>() {
                        @Override
                        public SyncableObject apply(Cursor cursor) {
                            return RunEntry.from(cursor);
                        }
                    };
                    break;

                case TravelEntryContract.NAME:
                    cursor = query(db, TravelEntryContract.DIR_URI_TYPE, TravelEntryContract.COLS, log.getEntityId());

                    from = new Function<Cursor, SyncableObject>() {
                        @Override
                        public SyncableObject apply(Cursor cursor) {
                            return TravelEntry.from(cursor);
                        }
                    };
                    break;

                default:
                    throw new IllegalArgumentException("Unknown entity " + log.getEntityName());
            }

            if (cursor.moveToFirst()) {
                SyncableObject obj = from.apply(cursor);
                if (obj.getRemoteId() == 0) {
                    sendCreate(log, obj, token);
                } else {
                    sendUpdate(log, obj, token);
                }
            } else {
                sendDelete(log, token);
            }
        } catch (RetrofitError e) {
            Log.w(LOG_TAG, "Send failed", e);
        }
    }

    private Cursor query(SQLiteDatabase db, DirUriType type, String[] cols, long id) {
        return db.query(type.getEntityName() + type.getJoinedEntities(), cols,
                BaseColumns._ID + "=" + id, null, null, null, null, null);
    }

    private void sendCreate(SyncLog log, SyncableObject entity, String token) {
        Log.d(LOG_TAG, "Send create");

        long remoteId = api.create(token, log.getEntityName(), new SyncableObjectWrapper(entity));

        long logId = log.getId();
        if (logId < entity.getLogId()) {
            logId = entity.getLogId();
        }

        ContentValues values = new ContentValues();
        values.put(SyncableObjectContract.COL_REMOTE_ID, remoteId);

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            db.beginTransaction();

            int rowsUpdated = db.update(log.getEntityName(), values, SyncableObjectContract.COL_ID + "= ?",
                    new String[]{Long.toString(log.getEntityId())});

            if (rowsUpdated == 0) {
                Log.d(LOG_TAG, log.getEntityName() + " deleted while waiting for create request");

                ContentValues logValues = new ContentValues();
                logValues.put(SyncLogContract.COL_ENTITY_REMOTE_ID, remoteId);

                db.update(SyncLogContract.NAME, logValues,
                        SyncLogContract.COL_ENTITY_NAME + " = ? AND " +
                        SyncLogContract.COL_ENTITY_ID + " = ? AND " +
                        SyncLogContract.COL_ID + " > ?",
                        new String[]{log.getEntityName(), Long.toString(log.getEntityId()), Long.toString(logId)}
                );
            }

            db.delete(SyncLogContract.NAME,
                    SyncLogContract.COL_ENTITY_NAME + " = ? AND " +
                    SyncLogContract.COL_ENTITY_ID + " = ? AND " +
                    SyncLogContract.COL_ID + " <= ?",
                    new String[]{log.getEntityName(), Long.toString(log.getEntityId()), Long.toString(logId)}
            );

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    private void sendUpdate(SyncLog log, SyncableObject entity, String token) {
        Log.d(LOG_TAG, "Send update");

        api.update(token, log.getEntityName(), entity.getRemoteId(), new SyncableObjectWrapper(entity));

        long logId = log.getId();
        if (logId < entity.getLogId()) {
            logId = entity.getLogId();
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            db.beginTransaction();

            db.delete(SyncLogContract.NAME,
                    SyncLogContract.COL_ENTITY_NAME + " = ? AND " +
                    SyncLogContract.COL_ENTITY_ID + " = ? AND " +
                    SyncLogContract.COL_ID + " <= ?",
                    new String[]{log.getEntityName(), Long.toString(log.getEntityId()), Long.toString(logId)}
            );

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    private void sendDelete(SyncLog log, String token) {
        Log.d(LOG_TAG, "Send delete");

        long logId = log.getId();

        if (log.getEntityRemoteId() == 0) {
            // create/update log, or delete log for entity never send to server
        } else {
            api.delete(token, log.getEntityName(), log.getEntityRemoteId());
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            db.beginTransaction();

            db.delete(SyncLogContract.NAME,
                    SyncLogContract.COL_ENTITY_NAME + " = ? AND " +
                    SyncLogContract.COL_ENTITY_ID + " = ? AND " +
                    SyncLogContract.COL_ID + " <= ?",
                    new String[]{log.getEntityName(), Long.toString(log.getEntityId()), Long.toString(logId)}
            );

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    private void receive(Revision revision, String token) {
        Log.d(LOG_TAG, "Receive sync");

        try {
            DiffResponse response = api.sync(token, revision.getLatestRevision());

            if (response.getLatestRevision() == revision.getLatestRevision()) {
                Log.d(LOG_TAG, "No remote changes to sync");
            } else {
                receiveChanges(revision.getLatestRevision(), response);

                Log.d(LOG_TAG, "Synced to revision " + response.getLatestRevision());

                notificationManager.onChangesReceived();
            }
        } catch (RetrofitError e) {
            Log.w(LOG_TAG, "Receive failed", e);
        }
    }

    private void receiveChanges(long previousRevision, DiffResponse response) {
        ContentValues values;
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            db.beginTransaction();

            for (SyncableObject entity : response.getChanges()) {

                DirUriType uriType = URI_TYPES_BY_CLASS.get(entity.getClass());
                long remoteId = entity.getId();

                values = entity.toValues();

                int updatedRows = db.update(uriType.getEntityName(), values, SyncableObjectContract.COL_REMOTE_ID + "= ?",
                        new String[]{Long.toString(remoteId)});

                if (updatedRows == 0) {
                    values.put(SyncableObjectContract.COL_REMOTE_ID, remoteId);
                    db.insert(uriType.getEntityName(), null, values);
                }

                getContext().getContentResolver().notifyChange(uriType.uri(), null);
            }

            for (SyncableObject entity : response.getDeletes()) {

                DirUriType uriType = URI_TYPES_BY_CLASS.get(entity.getClass());
                long remoteId = entity.getId();

                db.delete(uriType.getEntityName(), SyncableObjectContract.COL_REMOTE_ID + "= ?",
                        new String[]{Long.toString(remoteId)});

                getContext().getContentResolver().notifyChange(uriType.uri(), null);
            }

            values = new ContentValues();
            values.put(RevisionContract.COL_LATEST_REVISION, response.getLatestRevision());

            if (previousRevision == 0) {
                db.insert(RevisionContract.NAME, null, values);
            } else {
                db.update(RevisionContract.NAME, values, null, null);
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }
}

