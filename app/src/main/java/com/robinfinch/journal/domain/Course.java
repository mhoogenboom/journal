package com.robinfinch.journal.domain;

import android.content.ContentValues;
import android.database.Cursor;

import com.robinfinch.journal.app.persistence.CourseContract;

import static com.robinfinch.journal.app.util.Utils.alias;
import static com.robinfinch.journal.app.util.Utils.appendIfNotEmpty;
import static com.robinfinch.journal.app.util.Utils.differs;

/**
 * Study course.
 *
 * @author Mark Hoogenboom
 */
public class Course extends SyncableObject implements NamedObject {

    private String name;

    public static Course from(Cursor cursor) {
        Course course = new Course();
        int i;

        i = cursor.getColumnIndexOrThrow(alias(CourseContract.NAME, CourseContract.COL_ID));
        long id = cursor.getLong(i);
        course.setId(id);

        i = cursor.getColumnIndexOrThrow(alias(CourseContract.NAME, CourseContract.COL_REMOTE_ID));
        long remoteId = cursor.getLong(i);
        course.setRemoteId(remoteId);

        i = cursor.getColumnIndexOrThrow(alias(CourseContract.NAME, CourseContract.COL_NAME));
        String name = cursor.getString(i);
        course.setName(name);

        i = cursor.getColumnIndex(alias(CourseContract.NAME, CourseContract.COL_LOG_ID));
        if (i != -1) {
            long logId = cursor.getLong(i);
            course.setLogId(logId);
        }

        return course;
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
        values.put(CourseContract.COL_NAME, getName());
        return values;
    }

    @Override
    public CharSequence toShareString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Studying");
        appendIfNotEmpty(sb, " ", name);
        sb.append(".");
        return sb;
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
