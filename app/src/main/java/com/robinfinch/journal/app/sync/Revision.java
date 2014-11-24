package com.robinfinch.journal.app.sync;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.util.Log;

import com.robinfinch.journal.app.persistence.DbHelper;
import com.robinfinch.journal.app.persistence.RevisionContract;

import static com.robinfinch.journal.app.util.Constants.LOG_TAG;

/**
 * Contains versioning information for the sync.
 *
 * @author Mark Hoogenboom
 */
public class Revision {

    public static Revision from(Context context, Cursor cursor) {
        Revision revision = new Revision(context);
        int i;

        i = cursor.getColumnIndexOrThrow(RevisionContract.COL_DATA_VERSION);
        long dataVersion = cursor.getLong(i);
        revision.setDataVersion(dataVersion);

        return revision;
    }

    private long codeVersion;

    private long dataDefinitionVersion;

    private long dataVersion;

    public Revision() {

    }

    public Revision(Context context) {

        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            setCodeVersion(info.versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(LOG_TAG, "Can't get version code", e);
        }

        setDataDefinitionVersion(DbHelper.DATABASE_VERSION);
    }

    public long getCodeVersion() {
        return codeVersion;
    }

    public void setCodeVersion(long codeVersion) {
        this.codeVersion = codeVersion;
    }

    public long getDataDefinitionVersion() {
        return dataDefinitionVersion;
    }

    public void setDataDefinitionVersion(long dataDefinitionVersion) {
        this.dataDefinitionVersion = dataDefinitionVersion;
    }

    public long getDataVersion() {
        return dataVersion;
    }

    public void setDataVersion(long dataVersion) {
        this.dataVersion = dataVersion;
    }

    @Override
    public String toString() {
        return "com.robinfinch.journal.app.rest.Revision[codeVersion=" + getCodeVersion()
                + ";dataDefinitionVersion=" + getDataDefinitionVersion()
                + ";dataVersion=" + getDataVersion()
                + "]";
    }
}
