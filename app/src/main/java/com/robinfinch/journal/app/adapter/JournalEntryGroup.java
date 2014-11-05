package com.robinfinch.journal.app.adapter;

/**
 * Grouping of journal entries in an expandable list adapter.
 *
 * @author Mark Hoogenboom
 */
public class JournalEntryGroup {

    public final int year;
    public final int offset;
    public final int length;

    public JournalEntryGroup(int year, int offset, int length) {
        this.year = year;
        this.offset = offset;
        this.length = length;
    }
}
