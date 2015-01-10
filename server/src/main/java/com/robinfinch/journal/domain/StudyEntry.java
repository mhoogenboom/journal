package com.robinfinch.journal.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import static com.robinfinch.journal.server.util.Utils.appendIfNotEmpty;

/**
 * Journal entry describing a study session.
 *
 * @author Mark Hoogenboom
 */
@Entity
public class StudyEntry extends JournalEntry {

    @Transient
    private Long courseId;

    @ManyToOne
    private Course course;

    private String description;

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    @JsonIgnore
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
    public void prepareBeforeSend() {
        super.prepareBeforeSend();

        if (course == null) {
            courseId = 0L;
        } else {
            courseId = course.getId();
        }
    }

    @Override
    public void prepareAfterReceive(EntityManager em, JournalOwner owner) {
        super.prepareAfterReceive(em, owner);

        if (courseId == 0L) {
            course = null;
        } else {
            course = em.find(Course.class, courseId);
        }
    }

    @Override
    public CharSequence toPrettyString() {
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
                + ";owner=" + getOwner()
                + ";dayOfEntry=" + getDayOfEntry()
                + ";courseId=" + getCourseId()
                + ";course=" + getCourse()
                + ";description=" + getDescription()
                + "]";
    }
}
