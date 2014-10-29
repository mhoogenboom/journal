package com.robinfinch.journal.app.persistence;

import com.robinfinch.journal.app.util.DirUriType;
import com.robinfinch.journal.app.util.ItemUriType;

import static com.robinfinch.journal.app.util.Utils.aliasedId;

/**
 * Data definition for {@link com.robinfinch.journal.domain.RunEntry}.
 *
 * @author Mark Hoogenboom
 */
public interface RunEntryContract extends JournalEntryContract {

    String NAME = "runentry";

    String COL_DISTANCE = "distance";
    String COL_TIME_TAKEN = "time_taken";

    String[] COLS = {
            aliasedId(NAME, COL_ID), COL_REMOTE_ID, COL_DAY_OF_ENTRY, COL_DISTANCE, COL_TIME_TAKEN, COL_LOG_ID
    };

    DirUriType DIR_URI_TYPE = new DirUriType(NAME, "");

    ItemUriType ITEM_URI_TYPE = new ItemUriType(NAME, "");
}

