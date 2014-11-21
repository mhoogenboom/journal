package com.robinfinch.journal.domain;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

import com.robinfinch.journal.app.persistence.CourseContract;

import static com.robinfinch.journal.app.util.Utils.differs;

/**
 * Study course.
 *
 * @author Mark Hoogenboom
 */
public class Course extends SyncableObject implements NamedObject {

    private String name;

    public static Course from(Cursor cursor, String prefix) {
        Course course = new Course();
        int i;

        i = cursor.getColumnIndexOrThrow(prefix.equals("") ? BaseColumns._ID : prefix + CourseContract.COL_ID);
        long id = cursor.getLong(i);
        course.setId(id);

        i = cursor.getColumnIndexOrThrow(prefix + CourseContract.COL_REMOTE_ID);
        long remoteId = cursor.getLong(i);
        course.setRemoteId(remoteId);

        i = cursor.getColumnIndexOrThrow(prefix + CourseContract.COL_NAME);
        String name = cursor.getString(i);
        course.setName(name);

        i = cursor.getColumnIndexOrThrow(prefix + CourseContract.COL_LOG_ID);
        long logId = cursor.getLong(i);
        course.setLogId(logId);

        return course;
    }

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
        values.put(CourseContract.COL_NAME, getName());
        return values;
    }

    @Override
    public String toString() {
        return "com.robinfinch.journal.domain.Course[id=" + getId()
                + ";remoteId=" + getRemoteId()
                + ";name=" + getName()
                + ";logId=" + getLogId()
                + "]";
    }
}
