package com.robinfinch.journal.domain;

import javax.persistence.Entity;

import static com.robinfinch.journal.server.util.Utils.isEmpty;

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
    public CharSequence toPrettyString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Walked");
        if (!isEmpty(location)) {
            sb.append(" near ");
            sb.append(location);
        }
        sb.append(".");
        return sb;
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
