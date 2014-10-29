package com.robinfinch.journal.domain;

import javax.persistence.Entity;

/**
 * Journal entry describing a walk.
 *
 * @author Mark Hoogenboom
 */
@Entity
public class WalkEntry extends JournalEntry {

    private String location;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toPrettyString() {
        return "Walk near " + location + ".";
    }

    @Override
    public String toString() {
        return "com.robinfinch.journal.domain.WalkEntry[id=" + getId()
                + ";owner=" + getOwner()
                + ";dayOfEntry=" + getDayOfEntry()
                + ";location=" + getLocation()
                + "]";
    }
}
