package com.robinfinch.journal.domain;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.robinfinch.journal.app.persistence.CourseContract;
import com.robinfinch.journal.app.persistence.StudyEntryContract;

import java.util.Date;

import static com.robinfinch.journal.app.util.Utils.alias;
import static com.robinfinch.journal.app.util.Utils.appendIfNotEmpty;
import static com.robinfinch.journal.app.util.Utils.differs;

/**
 * Journal entry describing a study session.
 *
 * @author Mark Hoogenboom
 */
public class StudyEntry extends JournalEntry {

    private long courseId;

    private transient Course course;

    private String description;

    public static StudyEntry from(Cursor cursor) {
        StudyEntry entry = new StudyEntry();
        int i;

        i = cursor.getColumnIndexOrThrow(alias(StudyEntryContract.NAME, StudyEntryContract.COL_ID));
        long id = cursor.getLong(i);
        entry.setId(id);

        i = cursor.getColumnIndexOrThrow(alias(StudyEntryContract.NAME, StudyEntryContract.COL_REMOTE_ID));
        long remoteId = cursor.getLong(i);
        entry.setRemoteId(remoteId);

        i = cursor.getColumnIndexOrThrow(alias(StudyEntryContract.NAME, StudyEntryContract.COL_DAY_OF_ENTRY));
        long dayOfEntry = cursor.getLong(i);
        entry.setDayOfEntry((dayOfEntry == 0) ? null : new Date(dayOfEntry));

        i = cursor.getColumnIndexOrThrow(alias(StudyEntryContract.NAME, StudyEntryContract.COL_COURSE_ID));
        long courseId = cursor.getLong(i);
        if (courseId == 0) {
            entry.setCourse(null);
        } else {
            Course course = Course.from(cursor);
            entry.setCourse(course);
        }

        i = cursor.getColumnIndexOrThrow(alias(StudyEntryContract.NAME, StudyEntryContract.COL_DESCRIPTION));
        String description = cursor.getString(i);
        entry.setDescription(description);

        i = cursor.getColumnIndexOrThrow(alias(StudyEntryContract.NAME, StudyEntryContract.COL_LOG_ID));
        long logId = cursor.getLong(i);
        entry.setLogId(logId);

        return entry;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
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
    public boolean prepareBeforeSend() {
        if (course == null) {
            courseId = 0;
        } else {
            if (course.getRemoteId() == 0) {
                return false;
            } else {
                courseId = course.getRemoteId();
            }
        }
        return super.prepareBeforeSend();
    }

    @Override
    public boolean prepareAfterReceive(SQLiteDatabase db) {
        if (courseId == 0) {
            course = null;
        } else {
            Cursor cursor = db.query(CourseContract.NAME, CourseContract.COLS,
                    CourseContract.COL_REMOTE_ID + "=" + courseId, null, null, null, null, null);

            if (cursor.moveToFirst()) {
                course = Course.from(cursor);
            } else {
                return false;
            }
        }
        return super.prepareAfterReceive(db);
    }

    @Override
    public CharSequence toShareString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Studied");
        if (course != null) {
            sb.append(" ");
            sb.append(course.getName());

            appendIfNotEmpty(sb, ", ", description);
        } else {
            appendIfNotEmpty(sb, " ", description);
        }
        sb.append(".");
        return sb;
    }

    @Override
    public String toString() {
        return "com.robinfinch.journal.domain.StudyEntry[id=" + getId()
                + ";remoteId=" + getRemoteId()
                + ";dayOfEntry=" + getDayOfEntry()
                + ";courseId=" + getCourseId()
                + ";course=" + getCourse()
                + ";description=" + getDescription()
                + ";logId=" + getLogId()
                + "]";
    }
}
