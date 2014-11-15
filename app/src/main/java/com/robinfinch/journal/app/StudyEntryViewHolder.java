package com.robinfinch.journal.app;

import android.view.View;
import android.widget.TextView;

import com.robinfinch.journal.app.util.Formatter;
import com.robinfinch.journal.domain.StudyEntry;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Study entry list item view holder.
 *
 * @author Mark Hoogenboom
 */
class StudyEntryViewHolder {

    @InjectView(R.id.studyentry_dayofstudy)
    protected TextView dayOfStudyView;

    @InjectView(R.id.studyentry_course)
    protected TextView courseView;

    @InjectView(R.id.studyentry_description)
    protected TextView descriptionView;

    public StudyEntryViewHolder(View view) {
        ButterKnife.inject(this, view);
    }

    public void bind(StudyEntry entry) {
        dayOfStudyView.setText(Formatter.formatDay(entry.getDayOfEntry()));
        courseView.setText(Formatter.formatCourse(entry.getCourse()));
        descriptionView.setText(entry.getDescription());
    }
}
