package com.robinfinch.journal.app.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.robinfinch.journal.domain.RunEntry;
import com.robinfinch.journal.domain.StudyEntry;
import com.robinfinch.journal.domain.TravelEntry;
import com.robinfinch.journal.domain.WalkEntry;

import java.util.HashMap;
import java.util.Map;

import static com.robinfinch.journal.app.util.Constants.LOG_TAG;

/**
 * Creates, upgrades and provides access to the SQLiteDatabase.
 *
 * @author Mark Hoogenboom
 */
public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "journal.db";
    private static final int DATABASE_VERSION = 8;

    public static Map<String, Class> CLASSES_BY_NAME = new HashMap<>();
    static {
        CLASSES_BY_NAME.put(".StudyEntry", StudyEntry.class);
        CLASSES_BY_NAME.put(".WalkEntry", WalkEntry.class);
        CLASSES_BY_NAME.put(".RunEntry", RunEntry.class);
        CLASSES_BY_NAME.put(".TravelEntry", TravelEntry.class);
    }

    public static final Map<Class, String> NAMES_BY_CLASS = new HashMap<>();
    static {
        NAMES_BY_CLASS.put(StudyEntry.class, StudyEntryContract.NAME);
        NAMES_BY_CLASS.put(WalkEntry.class, WalkEntryContract.NAME);
        NAMES_BY_CLASS.put(RunEntry.class, RunEntryContract.NAME);
        NAMES_BY_CLASS.put(TravelEntry.class, TravelEntryContract.NAME);
    }

    // Revision

    private static final String SQL_CREATE_REVISION =
            "CREATE TABLE " + RevisionContract.NAME + " (" +
                    RevisionContract.COL_LATEST_REVISION + " INTEGER" +
                    ")";

    private static final String SQL_DROP_REVISION =
            "DROP TABLE IF EXISTS " + RevisionContract.NAME;

    // Study entry

    private static final String SQL_CREATE_STUDY_ENTRY =
            "CREATE TABLE " + StudyEntryContract.NAME + " (" +
                    StudyEntryContract.COL_ID + " INTEGER PRIMARY KEY," +
                    StudyEntryContract.COL_REMOTE_ID + " INTEGER," +
                    StudyEntryContract.COL_DAY_OF_ENTRY + " INTEGER," +
                    StudyEntryContract.COL_COURSE_ID + " INTEGER," +
                    StudyEntryContract.COL_DESCRIPTION + " TEXT," +
                    StudyEntryContract.COL_LOG_ID + " INTEGER" +
                    ")";

    private static final String SQL_DROP_STUDY_ENTRY =
            "DROP TABLE IF EXISTS " + StudyEntryContract.NAME;

    // Course

    private static final String SQL_CREATE_COURSE =
            "CREATE TABLE " + CourseContract.NAME + " (" +
                    CourseContract.COL_ID + " INTEGER PRIMARY KEY," +
                    CourseContract.COL_REMOTE_ID + " INTEGER," +
                    CourseContract.COL_NAME + " TEXT," +
                    CourseContract.COL_LOG_ID + " INTEGER" +
                    ")";

    private static final String SQL_DROP_COURSE =
            "DROP TABLE IF EXISTS " + CourseContract.NAME;

    // Walk entry

    private static final String SQL_CREATE_WALK_ENTRY =
            "CREATE TABLE " + WalkEntryContract.NAME + " (" +
                    WalkEntryContract.COL_ID + " INTEGER PRIMARY KEY," +
                    WalkEntryContract.COL_REMOTE_ID + " INTEGER," +
                    WalkEntryContract.COL_DAY_OF_ENTRY + " INTEGER," +
                    WalkEntryContract.COL_LOCATION + " TEXT," +
                    WalkEntryContract.COL_LOG_ID + " INTEGER" +
                    ")";

    private static final String SQL_DROP_WALK_ENTRY =
            "DROP TABLE IF EXISTS " + WalkEntryContract.NAME;

    // Run entry

    private static final String SQL_CREATE_RUN_ENTRY =
            "CREATE TABLE " + RunEntryContract.NAME + " (" +
                    RunEntryContract.COL_ID + " INTEGER PRIMARY KEY," +
                    RunEntryContract.COL_REMOTE_ID + " INTEGER," +
                    RunEntryContract.COL_DAY_OF_ENTRY + " INTEGER," +
                    RunEntryContract.COL_DISTANCE + " INTEGER," +
                    RunEntryContract.COL_TIME_TAKEN + " INTEGER," +
                    RunEntryContract.COL_LOG_ID + " INTEGER" +
                    ")";

    private static final String SQL_DROP_RUN_ENTRY =
            "DROP TABLE IF EXISTS " + RunEntryContract.NAME;

    // Travel entry

    private static final String SQL_CREATE_TRAVEL_ENTRY =
            "CREATE TABLE " + TravelEntryContract.NAME + " (" +
                    TravelEntryContract.COL_ID + " INTEGER PRIMARY KEY," +
                    TravelEntryContract.COL_REMOTE_ID + " INTEGER," +
                    TravelEntryContract.COL_DAY_OF_ENTRY + " INTEGER," +
                    TravelEntryContract.COL_AWAY + " BOOLEAN," +
                    TravelEntryContract.COL_PLACE + " TEXT," +
                    TravelEntryContract.COL_LOG_ID + " INTEGER" +
                    ")";

    private static final String SQL_DROP_TRAVEL_ENTRY =
            "DROP TABLE IF EXISTS " + TravelEntryContract.NAME;

    // Sync log

    private static final String SQL_CREATE_SYNC_LOG =
            "CREATE TABLE " + SyncLogContract.NAME + " (" +
                    SyncLogContract.COL_ID + " INTEGER PRIMARY KEY," +
                    SyncLogContract.COL_ENTITY_NAME + " TEXT," +
                    SyncLogContract.COL_ENTITY_ID + " INTEGER," +
                    SyncLogContract.COL_ENTITY_REMOTE_ID + " INTEGER" +
                    ")";

    private static final String SQL_DROP_ENTRY_LOG =
            "DROP TABLE IF EXISTS " + SyncLogContract.NAME;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, "Create " + DATABASE_NAME + " version " + DATABASE_VERSION);
        db.execSQL(SQL_CREATE_REVISION);
        db.execSQL(SQL_CREATE_STUDY_ENTRY);
        db.execSQL(SQL_CREATE_COURSE);
        db.execSQL(SQL_CREATE_WALK_ENTRY);
        db.execSQL(SQL_CREATE_RUN_ENTRY);
        db.execSQL(SQL_CREATE_TRAVEL_ENTRY);
        db.execSQL(SQL_CREATE_SYNC_LOG);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(LOG_TAG, "Drop " + DATABASE_NAME + " version " + oldVersion);
        db.execSQL(SQL_DROP_REVISION);
        db.execSQL(SQL_DROP_STUDY_ENTRY);
        db.execSQL(SQL_DROP_COURSE);
        db.execSQL(SQL_DROP_WALK_ENTRY);
        db.execSQL(SQL_DROP_RUN_ENTRY);
        db.execSQL(SQL_DROP_TRAVEL_ENTRY);
        db.execSQL(SQL_DROP_ENTRY_LOG);

        onCreate(db);
    }
}
