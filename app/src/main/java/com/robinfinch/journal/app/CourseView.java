package com.robinfinch.journal.app;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import com.robinfinch.journal.app.util.Formatter;
import com.robinfinch.journal.domain.Course;

/**
 * View displaying a course.
 *
 * @author Mark Hoogenboom
 */
public class CourseView extends EditText {

    private Course course;

    public CourseView(Context context) {
        super(context);
        setFocusable(false);
    }

    public CourseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFocusable(false);
    }

    public void setCourse(Course course) {
        this.course = course;
        setText(Formatter.formatCourse(course));
    }

    public Course getCourse() {
        return course;
    }

    public long getCourseId() {
        return (course == null) ? 0L : course.getId();
    }
}
