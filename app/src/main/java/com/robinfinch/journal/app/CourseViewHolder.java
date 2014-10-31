package com.robinfinch.journal.app;

import android.view.View;
import android.widget.TextView;

import com.robinfinch.journal.domain.Course;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Course list item view holder.
 *
 * @author Mark Hoogenboom
 */
class CourseViewHolder {

    @InjectView(R.id.course_name)
    protected TextView nameView;

    public CourseViewHolder(View view) {
        ButterKnife.inject(this, view);
    }

    public void bind(Course course) {

        nameView.setText(course.getName());
    }
}
