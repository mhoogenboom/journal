package com.robinfinch.journal.domain;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

import com.robinfinch.journal.app.persistence.CourseContract;
import com.robinfinch.journal.app.persistence.StudyEntryContract;

import java.util.Date;

import static com.robinfinch.journal.app.util.Utils.differs;

/**
 * Journal entry describing a study session.
 *
 * @author Mark Hoogenboom
 */
public class StudyEntry extends JournalEntry {

    private Course course;

    private String description;

    public static StudyEntry from(Cursor cursor, String prefix) {
        StudyEntry entry = new StudyEntry();
        int i;

        i = cursor.getColumnIndexOrThrow(BaseColumns._ID);
        long id = cursor.getLong(i);
        entry.setId(id);

        i = cursor.getColumnIndexOrThrow(prefix + StudyEntryContract.COL_REMOTE_ID);
        long remoteId = cursor.getLong(i);
        entry.setRemoteId(remoteId);

        i = cursor.getColumnIndexOrThrow(StudyEntryContract.COL_DAY_OF_ENTRY);
        long dayOfEntry = cursor.getLong(i);
        entry.setDayOfEntry((dayOfEntry == 0) ? null : new Date(dayOfEntry));

        i = cursor.getColumnIndexOrThrow(prefix + StudyEntryContract.COL_COURSE_ID);
        long courseId = cursor.getLong(i);
        if (courseId == 0) {
            entry.setCourse(null);
        } else {
            Course course = Course.from(cursor, CourseContract.NAME + "_");
            entry.setCourse(course);
        }

        i = cursor.getColumnIndexOrThrow(prefix + StudyEntryContract.COL_DESCRIPTION);
        String description = cursor.getString(i);
        entry.setDescription(description);

        i = cursor.getColumnIndexOrThrow(prefix + StudyEntryContract.COL_LOG_ID);
        long logId = cursor.getLong(i);
        entry.setLogId(logId);

        return entry;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        if (differs(this.course, course)) {
            this.course = course;
            this.changed = true;
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (differs(this.description, description)) {
            this.description = description;
            this.changed = true;
        }
    }

    @Override
    public ContentValues toValues() {
        ContentValues values = new ContentValues();
        values.put(StudyEntryContract.COL_DAY_OF_ENTRY, (getDayOfEntry() == null) ? 0 : getDayOfEntry().getTime());
        values.put(StudyEntryContract.COL_COURSE_ID, (getCourse() == null) ? 0 : getCourse().getId());
        values.put(StudyEntryContract.COL_DESCRIPTION, description);
        return values;
    }

    @Override
    public String toString() {
        return "com.robinfinch.journal.domain.StudyEntry[id=" + getId()
                + ";remoteId=" + getRemoteId()
                + ";dayOfEntry=" + getDayOfEntry()
                + ";course=" + getCourse()
                + ";description=" + getDescription()
                + ";logId=" + getLogId()
                + "]";
    }
}