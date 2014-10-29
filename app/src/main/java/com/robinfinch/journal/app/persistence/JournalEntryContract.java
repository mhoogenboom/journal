package com.robinfinch.journal.app.persistence;

/**
 * Base class of data definitions for {@link com.robinfinch.journal.domain.JournalEntry}.
 *
 * @author Mark Hoogenboom
 */
public interface JournalEntryContract extends SyncableObjectContract {

    String COL_DAY_OF_ENTRY = "day_of_entry";
}

