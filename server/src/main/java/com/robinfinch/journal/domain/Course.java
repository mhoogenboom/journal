package com.robinfinch.journal.domain;

import javax.persistence.Entity;

/**
 * Study course.
 *
 * @author Mark Hoogenboom
 */
@Entity
public class Course extends SyncableObject {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "com.robinfinch.journal.domain.Course[id=" + getId()
                + ";owner=" + getOwner()
                + ";name=" + getName()
                + "]";
    }
}
