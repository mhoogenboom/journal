package com.robinfinch.journal.app;

import android.widget.RemoteViews;

import com.robinfinch.journal.app.util.Formatter;
import com.robinfinch.journal.domain.StudyEntry;

/**
 * Study entry remote view holder.
 *
 * @author Mark Hoogenboom
 */
public class StudyEntryRemoteViewHolder {

    private final RemoteViews views;

    public StudyEntryRemoteViewHolder(RemoteViews views) {
        this.views = views;
    }

    public void bind(StudyEntry entry) {
        views.setTextViewText(R.id.studyentry_dayofstudy, Formatter.formatDay(entry.getDayOfEntry()));
        views.setTextViewText(R.id.studyentry_course, Formatter.formatCourse(entry.getCourse()));
        views.setTextViewText(R.id.studyentry_description, entry.getDescription());
    }
}
