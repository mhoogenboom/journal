package com.robinfinch.journal.app.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;

import com.robinfinch.journal.app.MainActivity;
import com.robinfinch.journal.app.R;
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

    public static final Class[] SYNCABLE_OBJECT_CLASSES = {
            StudyEntry.class,
            Course.class,
            WalkEntry.class,
            RunEntry.class,
            TravelEntry.class
    };

    public static final String AUTH_TOKEN_TYPE_SYNC = "com.robinfinch.sync";

    private static final int SYNC_NOTIFICATION_ID = 300;

    private DbHelper dbHelper;
    private ConnectivityChecker connectivityChecker;
    private JournalApi api;
    private int maxEntriesToSend;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    public SyncAdapter(Context context, boolean autoInitialize,
                       boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
    }

    public SyncAdapter config(JournalApi api, int maxEntriesToSend) {
        this.dbHelper = new DbHelper(getContext());
        this.connectivityChecker = new ConnectivityChecker(getContext());
        this.api = api;
        this.maxEntriesToSend = maxEntriesToSend;
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
                SyncableObject entry = from.apply(cursor);
                if (entry.getRemoteId() == 0) {
                    sendCreate(log, entry, token);
                } else {
                    sendUpdate(log, entry, token);
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
        Log.d(LOG_TAG, "Receive diff");

        try {
            DiffResponse response = api.diff(token, revision.getLatestRevision());

            if (response.getLatestRevision() == revision.getLatestRevision()) {
                Log.d(LOG_TAG, "No remote changes to sync");
            } else {
                receiveChanges(revision.getLatestRevision(), response);

                Log.d(LOG_TAG, "Synced to revision " + response.getLatestRevision());

                notifyChanges();
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

                String entityName = DbHelper.NAMES_BY_CLASS.get(entity.getClass());

                long remoteId = entity.getId();

                values = entity.toValues();

                int updatedRows = db.update(entityName, values, SyncableObjectContract.COL_REMOTE_ID + "= ?",
                        new String[]{Long.toString(remoteId)});

                if (updatedRows == 0) {
                    values.put(SyncableObjectContract.COL_REMOTE_ID, remoteId);
                    db.insert(entityName, null, values);
                }
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

    private void notifyChanges() {
        Intent intent = new Intent(getContext(), MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, intent, 0);

        Notification notification = new Notification.Builder(getContext())
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(getContext().getText(R.string.sync_notification_title))
                .setContentText(getContext().getText(R.string.sync_notification_text))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        NotificationManager notificationManager =
                (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(SYNC_NOTIFICATION_ID, notification);
    }
}

