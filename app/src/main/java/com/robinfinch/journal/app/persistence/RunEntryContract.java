package com.robinfinch.journal.app.persistence;

import com.robinfinch.journal.app.util.DirUriType;
import com.robinfinch.journal.app.util.ItemUriType;

import static com.robinfinch.journal.app.util.Utils.aliased;
import static com.robinfinch.journal.app.util.Utils.aliasedId;

/**
 * Data definition for {@link com.robinfinch.journal.domain.RunEntry}.
 *
 * @author Mark Hoogenboom
 */
public interface RunEntryContract extends JournalEntryContract {

    String NAME = "runentry";

    String COL_DISTANCE = "distance";
    String COL_NOTE = "note";
    String COL_TIME_TAKEN = "time_taken";

    String[] COLS = {
            aliasedId(NAME, COL_ID),
            aliased(NAME, COL_ID),
            aliased(NAME, COL_REMOTE_ID),
            aliased(NAME, COL_DAY_OF_ENTRY),
            aliased(NAME, COL_DISTANCE),
            aliased(NAME, COL_NOTE),
            aliased(NAME, COL_TIME_TAKEN),
            aliased(NAME, COL_LOG_ID)
    };

    DirUriType DIR_URI_TYPE = new DirUriType(NAME, "");

    ItemUriType ITEM_URI_TYPE = new ItemUriType(NAME, "");
}

