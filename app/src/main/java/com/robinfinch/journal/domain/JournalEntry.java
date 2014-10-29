package com.robinfinch.journal.domain;

import java.util.Date;

import static com.robinfinch.journal.app.util.Utils.differs;

/**
 * Base class for journal entries.
 *
 * @author Mark Hoogenboom
 */
public abstract class JournalEntry extends SyncableObject {

    private Date dayOfEntry;

    public Date getDayOfEntry() {
        return dayOfEntry;
    }

    public void setDayOfEntry(Date dayOfEntry) {
        if (differs(this.dayOfEntry, dayOfEntry)) {
            this.dayOfEntry = dayOfEntry;
            this.changed = true;
        }
    }
}
