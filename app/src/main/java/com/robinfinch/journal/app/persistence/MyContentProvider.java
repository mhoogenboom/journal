package com.robinfinch.journal.app.persistence;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import com.robinfinch.journal.app.ContextModule;
import com.robinfinch.journal.app.util.DirUriType;
import com.robinfinch.journal.app.util.ItemUriType;
import com.robinfinch.journal.app.util.UriType;

import javax.inject.Inject;

import dagger.ObjectGraph;

import static com.robinfinch.journal.app.util.Constants.LOG_TAG;

/**
 * Content provider that stores content in SQLite database.
 *
 * @author Mark Hoogenboom
 */
public class MyContentProvider extends ContentProvider {

    private static final UriType[] URI_TYPES = {
            StudyEntryContract.DIR_URI_TYPE,
            StudyEntryContract.ITEM_URI_TYPE,
            CourseContract.DIR_URI_TYPE,
            CourseContract.ITEM_URI_TYPE,
            TitleContract.DIR_URI_TYPE,
            TitleContract.ITEM_URI_TYPE,
            AuthorContract.DIR_URI_TYPE,
            AuthorContract.ITEM_URI_TYPE,
            WalkEntryContract.DIR_URI_TYPE,
            WalkEntryContract.ITEM_URI_TYPE,
            RunEntryContract.DIR_URI_TYPE,
            RunEntryContract.ITEM_URI_TYPE,
            TravelEntryContract.DIR_URI_TYPE,
            TravelEntryContract.ITEM_URI_TYPE
    };

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        for (int code = 0; code < URI_TYPES.length; code++) {
            URI_MATCHER.addURI(UriType.AUTHORITY, URI_TYPES[code].match(), code);
        }
    }

    @Inject
    DbHelper dbHelper;

    @Override
    public boolean onCreate() {
        ObjectGraph.create(
                new ContextModule(getContext()),
                new PersistenceModule()
        ).inject(this);
        return true;
    }

    @Override
    public String getType(Uri uri) {
        int code = URI_MATCHER.match(uri);
        return URI_TYPES[code].toString();
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Log.d(LOG_TAG, "Query " + uri);

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        int code = URI_MATCHER.match(uri);

        UriType type = URI_TYPES[code];

        queryBuilder.setTables(type.getEntityName() + type.getJoinedEntities());

        if (type instanceof ItemUriType) {
            queryBuilder.appendWhere(BaseColumns._ID + "=" + uri.getLastPathSegment());
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.d(LOG_TAG, "Insert " + uri);

        int code = URI_MATCHER.match(uri);

        DirUriType type = (DirUriType) URI_TYPES[code];

        values.put(SyncableObjectContract.COL_LOG_ID, 0);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long id;
        try {
            db.beginTransaction();

            id = db.insert(type.getEntityName(), null, values);
            log(db, type.getEntityName(), id, 0);

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        getContext().getContentResolver().notifyChange(uri, null, true);
        return type.toItemType().uri(id);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        Log.d(LOG_TAG, "Update " + uri);

        int code = URI_MATCHER.match(uri);

        ItemUriType type = (ItemUriType) URI_TYPES[code];

        long id = ContentUris.parseId(uri);
        long logId;

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int rowsUpdated = 0;
        try {
            db.beginTransaction();

            logId = log(db, type.getEntityName(), id, 0);
            values.put(SyncableObjectContract.COL_LOG_ID, logId);
            rowsUpdated = db.update(type.getEntityName(), values,
                    SyncableObjectContract.COL_ID + "=" + id, null);

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        getContext().getContentResolver().notifyChange(uri, null, true);
        return rowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.d(LOG_TAG, "Delete " + uri);

        int code = URI_MATCHER.match(uri);

        ItemUriType type = (ItemUriType) URI_TYPES[code];

        long id = ContentUris.parseId(uri);
        long remoteId = Long.parseLong(selection);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int rowsDeleted = 0;
        try {
            db.beginTransaction();

            rowsDeleted = db.delete(type.getEntityName(),
                    SyncableObjectContract.COL_ID + "=" + id, null);
            log(db, type.getEntityName(), id, remoteId);

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        getContext().getContentResolver().notifyChange(uri, null, true);
        return rowsDeleted;
    }

    private long log(SQLiteDatabase db, String entityName, long id, long remoteId) {
        ContentValues values = new ContentValues();
        values.put(SyncLogContract.COL_ENTITY_NAME, entityName);
        values.put(SyncLogContract.COL_ENTITY_ID, id);
        values.put(SyncLogContract.COL_ENTITY_REMOTE_ID, remoteId);
        return db.insert(SyncLogContract.NAME, null, values);
    }
}
