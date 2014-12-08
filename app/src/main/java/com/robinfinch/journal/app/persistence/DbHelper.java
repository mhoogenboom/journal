package com.robinfinch.journal.app.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static com.robinfinch.journal.app.util.Constants.LOG_TAG;

/**
 * Creates, upgrades and provides access to the SQLiteDatabase.
 *
 * @author Mark Hoogenboom
 */
public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "journal.db";
    public static final int DATABASE_VERSION = 12;

    /*
     * Revision
     */
    private static final String SQL_CREATE_REVISION =
            "CREATE TABLE " + RevisionContract.NAME + " (" +
                    RevisionContract.COL_DATA_VERSION + " INTEGER" +
                    ")";

    /*
     * Study entry
     */
    private static final String SQL_CREATE_STUDY_ENTRY =
            "CREATE TABLE " + StudyEntryContract.NAME + " (" +
                    StudyEntryContract.COL_ID + " INTEGER PRIMARY KEY," +
                    StudyEntryContract.COL_REMOTE_ID + " INTEGER," +
                    StudyEntryContract.COL_DAY_OF_ENTRY + " INTEGER," +
                    StudyEntryContract.COL_COURSE_ID + " INTEGER," +
                    StudyEntryContract.COL_DESCRIPTION + " TEXT," +
                    StudyEntryContract.COL_LOG_ID + " INTEGER" +
                    ")";

    /*
     * Course
     */
    private static final String SQL_CREATE_COURSE =
            "CREATE TABLE " + CourseContract.NAME + " (" +
                    CourseContract.COL_ID + " INTEGER PRIMARY KEY," +
                    CourseContract.COL_REMOTE_ID + " INTEGER," +
                    CourseContract.COL_NAME + " TEXT," +
                    CourseContract.COL_LOG_ID + " INTEGER" +
                    ")";

    /*
     * Read entry
     */
    private static final String SQL_CREATE_READ_ENTRY =
            "CREATE TABLE " + ReadEntryContract.NAME + " (" +
                    ReadEntryContract.COL_ID + " INTEGER PRIMARY KEY," +
                    ReadEntryContract.COL_REMOTE_ID + " INTEGER," +
                    ReadEntryContract.COL_DAY_OF_ENTRY + " INTEGER," +
                    ReadEntryContract.COL_TITLE_ID + " INTEGER," +
                    ReadEntryContract.COL_PART + " TEXT," +
                    ReadEntryContract.COL_LOG_ID + " INTEGER" +
                    ")";    

    /*
     * Title
     */
    private static final String SQL_CREATE_TITLE =
            "CREATE TABLE " + TitleContract.NAME + " (" +
                    TitleContract.COL_ID + " INTEGER PRIMARY KEY," +
                    TitleContract.COL_REMOTE_ID + " INTEGER," +
                    TitleContract.COL_TITLE + " TEXT," +
                    TitleContract.COL_AUTHOR_ID + " INTEGER," +
                    TitleContract.COL_YEAR + " TEXT," +
                    TitleContract.COL_LOG_ID + " INTEGER" +
                    ")";

    /*
     * Author
     */
    private static final String SQL_CREATE_AUTHOR =
            "CREATE TABLE " + AuthorContract.NAME + " (" +
                    AuthorContract.COL_ID + " INTEGER PRIMARY KEY," +
                    AuthorContract.COL_REMOTE_ID + " INTEGER," +
                    AuthorContract.COL_NAME + " TEXT," +
                    AuthorContract.COL_LOG_ID + " INTEGER" +
                    ")";

    /*
     * Application entry
     */
    private static final String SQL_CREATE_APPLICATION_ENTRY =
            "CREATE TABLE " + ApplicationEntryContract.NAME + " (" +
                    ApplicationEntryContract.COL_ID + " INTEGER PRIMARY KEY," +
                    ApplicationEntryContract.COL_REMOTE_ID + " INTEGER," +
                    ApplicationEntryContract.COL_DAY_OF_ENTRY + " INTEGER," +
                    ApplicationEntryContract.COL_APPLICATION_ID + " INTEGER," +
                    ApplicationEntryContract.COL_ACTION_ID + " INTEGER," +
                    ApplicationEntryContract.COL_LOG_ID + " INTEGER" +
                    ")";

    /*
     * Application
     */
    private static final String SQL_CREATE_APPLICATION =
            "CREATE TABLE " + ApplicationContract.NAME + " (" +
                    ApplicationContract.COL_ID + " INTEGER PRIMARY KEY," +
                    ApplicationContract.COL_REMOTE_ID + " INTEGER," +
                    ApplicationContract.COL_RECRUITER_ID + " INTEGER," +
                    ApplicationContract.COL_CLIENT_ID + " INTEGER," +
                    ApplicationContract.COL_START + " TEXT," +
                    ApplicationContract.COL_RATE + " TEXT," +
                    ApplicationContract.COL_STATE_ID + " INTEGER," +
                    ApplicationContract.COL_LOG_ID + " INTEGER" +
                    ")";

    /*
     * Recruiter
     */
    private static final String SQL_CREATE_RECRUITER =
            "CREATE TABLE " + RecruiterContract.NAME + " (" +
                    RecruiterContract.COL_ID + " INTEGER PRIMARY KEY," +
                    RecruiterContract.COL_REMOTE_ID + " INTEGER," +
                    RecruiterContract.COL_NAME + " TEXT," +
                    RecruiterContract.COL_ORGANISATION_ID + " INTEGER," +
                    RecruiterContract.COL_PHONE_NUMBER + " TEXT," +
                    RecruiterContract.COL_LOG_ID + " INTEGER" +
                    ")";

    /*
     * Organisation
     */
    private static final String SQL_CREATE_ORGANISATION =
            "CREATE TABLE " + OrganisationContract.NAME + " (" +
                    OrganisationContract.COL_ID + " INTEGER PRIMARY KEY," +
                    OrganisationContract.COL_REMOTE_ID + " INTEGER," +
                    OrganisationContract.COL_NAME + " TEXT," +
                    OrganisationContract.COL_LOG_ID + " INTEGER" +
                    ")";
    
    /*
     * Travel entry
     */
    private static final String SQL_CREATE_TRAVEL_ENTRY =
            "CREATE TABLE " + TravelEntryContract.NAME + " (" +
                    TravelEntryContract.COL_ID + " INTEGER PRIMARY KEY," +
                    TravelEntryContract.COL_REMOTE_ID + " INTEGER," +
                    TravelEntryContract.COL_DAY_OF_ENTRY + " INTEGER," +
                    TravelEntryContract.COL_AWAY + " BOOLEAN," +
                    TravelEntryContract.COL_PLACE + " TEXT," +
                    TravelEntryContract.COL_LOG_ID + " INTEGER" +
                    ")";    
    
    /*
     * Walk entry
     */
    private static final String SQL_CREATE_WALK_ENTRY =
            "CREATE TABLE " + WalkEntryContract.NAME + " (" +
                    WalkEntryContract.COL_ID + " INTEGER PRIMARY KEY," +
                    WalkEntryContract.COL_REMOTE_ID + " INTEGER," +
                    WalkEntryContract.COL_DAY_OF_ENTRY + " INTEGER," +
                    WalkEntryContract.COL_LOCATION + " TEXT," +
                    WalkEntryContract.COL_LOG_ID + " INTEGER" +
                    ")";

    /*
     * Run entry
     */
    private static final String SQL_CREATE_RUN_ENTRY =
            "CREATE TABLE " + RunEntryContract.NAME + " (" +
                    RunEntryContract.COL_ID + " INTEGER PRIMARY KEY," +
                    RunEntryContract.COL_REMOTE_ID + " INTEGER," +
                    RunEntryContract.COL_DAY_OF_ENTRY + " INTEGER," +
                    RunEntryContract.COL_DISTANCE + " INTEGER," +
                    RunEntryContract.COL_TIME_TAKEN + " INTEGER," +
                    RunEntryContract.COL_LOG_ID + " INTEGER" +
                    ")";

    /*
     * Sync log
     */
    private static final String SQL_CREATE_SYNC_LOG =
            "CREATE TABLE " + SyncLogContract.NAME + " (" +
                    SyncLogContract.COL_ID + " INTEGER PRIMARY KEY," +
                    SyncLogContract.COL_ENTITY_NAME + " TEXT," +
                    SyncLogContract.COL_ENTITY_ID + " INTEGER," +
                    SyncLogContract.COL_ENTITY_REMOTE_ID + " INTEGER" +
                    ")";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, "Create " + DATABASE_NAME + " version " + DATABASE_VERSION);
        db.execSQL(SQL_CREATE_REVISION);
        db.execSQL(SQL_CREATE_STUDY_ENTRY);
        db.execSQL(SQL_CREATE_COURSE);
        db.execSQL(SQL_CREATE_READ_ENTRY);
        db.execSQL(SQL_CREATE_TITLE);
        db.execSQL(SQL_CREATE_AUTHOR);
        db.execSQL(SQL_CREATE_APPLICATION_ENTRY);
        db.execSQL(SQL_CREATE_APPLICATION);
        db.execSQL(SQL_CREATE_RECRUITER);
        db.execSQL(SQL_CREATE_ORGANISATION);
        db.execSQL(SQL_CREATE_TRAVEL_ENTRY);
        db.execSQL(SQL_CREATE_WALK_ENTRY);
        db.execSQL(SQL_CREATE_RUN_ENTRY);
        db.execSQL(SQL_CREATE_SYNC_LOG);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if ((oldVersion == 8) && (8 < newVersion)) {
            Log.d(LOG_TAG, "Upgrade " + DATABASE_NAME + " version " + oldVersion);
            db.execSQL(SQL_CREATE_AUTHOR);
            oldVersion++;
        }

        if ((oldVersion == 9) && (9 < newVersion)) {
            Log.d(LOG_TAG, "Upgrade " + DATABASE_NAME + " version " + oldVersion);
            db.execSQL(SQL_CREATE_TITLE);
            oldVersion++;
        }

        if ((oldVersion == 10) && (10 < newVersion)) {
            Log.d(LOG_TAG, "Upgrade " + DATABASE_NAME + " version " + oldVersion);
            db.execSQL(SQL_CREATE_READ_ENTRY);
            oldVersion++;
        }

        if ((oldVersion == 11) && (11 < newVersion)) {
            Log.d(LOG_TAG, "Upgrade " + DATABASE_NAME + " version " + oldVersion);
            db.execSQL(SQL_CREATE_APPLICATION_ENTRY);
            db.execSQL(SQL_CREATE_APPLICATION);
            db.execSQL(SQL_CREATE_RECRUITER);
            db.execSQL(SQL_CREATE_ORGANISATION);
            oldVersion++;
        }

    }
}
