package com.robinfinch.journal.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Journal entry describing a study session.
 *
 * @author Mark Hoogenboom
 */
@Entity
public class StudyEntry extends JournalEntry {

    @ManyToOne
    private Course course;

    private String description;

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toPrettyString() {
        return ((course == null) ? "" : course.getName() + ", ") + description + ".";
    }

    @Override
    public String toString() {
        return "com.robinfinch.journal.domain.StudyEntry[id=" + getId()
                + ";owner=" + getOwner()
                + ";dayOfEntry=" + getDayOfEntry()
                + ";course=" + getCourse()
                + ";description=" + getDescription()
                + "]";
    }
}
