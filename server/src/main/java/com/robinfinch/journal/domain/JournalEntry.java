package com.robinfinch.journal.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;

import static com.robinfinch.journal.server.util.Utils.compare;
import static javax.persistence.TemporalType.DATE;

/**
 * Base class for journal entries.
 *
 * @author Mark Hoogenboom
 */
@Entity
public abstract class JournalEntry extends SyncableObject implements Comparable<JournalEntry> {

    @Temporal(DATE)
    private Date dayOfEntry;

    public Date getDayOfEntry() {
        return dayOfEntry;
    }

    public void setDayOfEntry(Date dayOfEntry) {
        this.dayOfEntry = dayOfEntry;
    }

    @Override
    public int compareTo(JournalEntry that) {
        int c = compare(this.dayOfEntry, that.dayOfEntry);
        if (c == 0) {
            c = (int) (this.getId() - that.getId());
        }
        return c;
    }

    public abstract String toPrettyString();
}
