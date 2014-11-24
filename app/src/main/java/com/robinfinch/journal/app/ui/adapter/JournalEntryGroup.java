package com.robinfinch.journal.app.ui.adapter;

/**
 * Grouping of journal entries in an expandable list adapter.
 *
 * @author Mark Hoogenboom
 */
public class JournalEntryGroup {

    public final int year;
    public final int month;
    public final int offset;
    public final int length;

    public JournalEntryGroup(int year, int month, int offset, int length) {
        this.year = year;
        this.month = month;
        this.offset = offset;
        this.length = length;
    }
}
